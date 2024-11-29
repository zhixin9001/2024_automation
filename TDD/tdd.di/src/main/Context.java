package main;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.*;

public class Context {
    private final Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <T> void bind(Class<T> type, T instance) {
        providers.put(type, (Provider<T>) () -> instance);
    }

    public <T, TImp extends T> void bind(Class<T> type, Class<TImp> imp) throws IllegalComponentException {
        Constructor<TImp> constructor = getConstructor(imp);
        providers.put(type, new ConstructorInjectionProvider<TImp>(constructor));
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

    public <T> Optional<T> get(Class<T> type) {
        return Optional.ofNullable(providers.get(type)).map(p -> (T) p.get());
    }


    class ConstructorInjectionProvider<T> implements Provider {
        private final Constructor<T> constructor;
        private boolean isConstructing;

        public ConstructorInjectionProvider(Constructor<T> constructor) {
            this.constructor = constructor;
        }

        @Override
        public Object get() {
            if (isConstructing) throw new RuntimeException("Cyclic dependency found");
            try {
                isConstructing = true;
                Object[] dependencies = stream(this.constructor.getParameters())
                        .map(p -> Context.this.get(p.getType()).orElseThrow(() -> new RuntimeException("Dependency not found"))).toArray(Object[]::new);
                return this.constructor.newInstance(dependencies);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                isConstructing = false;
            }
        }
    }
}


interface Provider<T> {
    T get();
}
