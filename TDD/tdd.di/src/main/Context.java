package main;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.*;

public class Context {
    private final Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <T> void bind(Class<T> type, T instance) {
        providers.put(type, (Provider<T>) () -> instance);
    }

    public <T, TImp extends T> void bind(Class<T> type, Class<TImp> imp) {
        providers.put(type, (Provider<T>) () -> {
            try {
                Constructor<TImp> constructor = getConstructor(imp);
                Object[] dependencies = stream(constructor.getParameters()).map(p -> get(p.getType())).toArray(Object[]::new);
                return (T) constructor.newInstance(dependencies);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static <T> Constructor<T> getConstructor(Class<T> imp) {
        Stream<Constructor<?>> constructors = stream(imp.getDeclaredConstructors()).filter(c -> c.isAnnotationPresent(Inject.class));
        return (Constructor<T>) constructors.findFirst().orElseGet(() -> {
            try {
                return imp.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public <T> T get(Class<T> type) {
        return (T) providers.get(type).get();
    }

}

interface Provider<T> {
    T get();
}
