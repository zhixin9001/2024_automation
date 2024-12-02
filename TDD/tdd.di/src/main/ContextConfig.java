package main;

import java.util.*;

public class ContextConfig {
    private final Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <T> void bind(Class<T> type, T instance) {
        providers.put(type, new Provider<T>() {
            @Override
            public T get(Context context) {
                return instance;
            }

            @Override
            public List<Class<?>> getDependencies() {
                return List.of();
            }
        });
    }

    public <T, TImp extends T> void bind(Class<T> type, Class<TImp> imp) throws IllegalComponentException {
        providers.put(type, new ConstructorInjectionProvider<TImp>(imp));
    }

    public Context getContext() {
        providers.keySet().forEach(c -> checkDependencies(c, new Stack<>()));
        return new Context() {
            @Override
            public <T> Optional<T> get(Class<T> type) {
                return Optional.ofNullable(providers.get(type)).map(p -> (T) p.get(this));
            }
        };
    }

    private void checkDependencies(Class<?> component, Stack<Class<?>> visiting) {
        for (Class<?> dependency : providers.get(component).getDependencies()) {
            if (!providers.containsKey(dependency)) throw new DependencyNotFoundException(component, dependency);
            if (visiting.contains(dependency)) throw new CyclicDependencyException(visiting);
            visiting.push(dependency);
            checkDependencies(dependency, visiting);
            visiting.pop();
        }
    }
}

