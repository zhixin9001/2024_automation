package main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.*;

public class ContextConfig {
    private final Map<Class<?>, Provider<?>> providers = new HashMap<>();
    private final Map<Class<?>, List<Class<?>>> dependencies = new HashMap<>();

    public <T> void bind(Class<T> type, T instance) {
        providers.put(type, (Provider<T>) _ -> instance);
        dependencies.put(type, List.of());
    }

    public <T, TImp extends T> void bind(Class<T> type, Class<TImp> imp) throws IllegalComponentException {
        Constructor<TImp> constructor = getConstructor(imp);
        providers.put(type, new ConstructorInjectionProvider<TImp>(type, constructor));
        dependencies.put(type, stream(constructor.getParameters()).map(Parameter::getType).collect(Collectors.toList()));
    }

    private static <T> Constructor<T> getConstructor(Class<T> imp) {
        List<Constructor<?>> constructors = stream(imp.getDeclaredConstructors()).filter(c -> c.isAnnotationPresent(Inject.class)).toList();
        if (constructors.size() > 1) throw new IllegalComponentException();
        return (Constructor<T>) constructors.stream().findFirst().orElseGet(() -> {
            try {
                return imp.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new IllegalComponentException();
            }
        });
    }

    public Context getContext() {
        dependencies.keySet().forEach(c->checkDependencies(c, new Stack<>()));
        return new Context() {
            @Override
            public <T> Optional<T> get(Class<T> type) {
                return Optional.ofNullable(providers.get(type)).map(p -> (T) p.get(this));
            }
        };
    }

    private void checkDependencies(Class<?> component, Stack<Class<?>> visiting) {
        for (Class<?> dependency : dependencies.get(component)) {
            if (!dependencies.containsKey(dependency)) throw new DependencyNotFoundException(component, dependency);
            if (visiting.contains(dependency)) throw new CyclicDependencyException(visiting);
            visiting.push(dependency);
            checkDependencies(dependency, visiting);
            visiting.pop();
        }
    }

    static class ConstructorInjectionProvider<T> implements Provider {
        private final Class<?> componentType;
        private final Constructor<T> constructor;
        private boolean isConstructing;

        public ConstructorInjectionProvider(Class<?> componentType, Constructor<T> constructor) {
            this.componentType = componentType;
            this.constructor = constructor;
        }

        @Override
        public Object get(Context context) {
            if (isConstructing) throw new CyclicDependencyException(componentType);
            try {
                isConstructing = true;
                Object[] dependencies = stream(this.constructor.getParameters())
                        .map(p -> {
                            Class<?> type = p.getType();
                            return context.get(type).orElseThrow(() -> new DependencyNotFoundException(componentType, p.getType()));
                        }).toArray(Object[]::new);
                return this.constructor.newInstance(dependencies);
            } catch (CyclicDependencyException e) {
                throw new CyclicDependencyException(componentType, e);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } finally {
                isConstructing = false;
            }
        }
    }
}


interface Provider<T> {
    T get(Context context);
}
