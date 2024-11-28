package main;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.*;

public class Context {
    private final Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <T> void bind(Class<T> type, T instance) {
        providers.put(type, (Provider<T>) () -> instance);
    }

    public <T, TImp extends T> void bind(Class<T> type, Class<TImp> imp) throws IllegalComponentException {
        Constructor<TImp> constructor = getConstructor(imp);
        providers.put(type, (Provider<T>) () -> {
            try {
                Object[] dependencies = stream(constructor.getParameters()).map(p -> {
                    try {
                        return get(p.getType());
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }).toArray(Object[]::new);
                return (T) constructor.newInstance(dependencies);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
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

    public <T> T get(Class<T> type) throws RuntimeException {
        if (!providers.containsKey(type)) throw new RuntimeException("Dependency not found");
        return (T) providers.get(type).get();
    }
}

interface Provider<T> {
    T get();
}
