package main;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <T> void bind(Class<T> type, T instance) {
        providers.put(type, (Provider<T>) () -> instance);
    }

    public <T, TImp extends T> void bind(Class<T> type, Class<TImp> imp) {
        providers.put(type, (Provider<T>) () -> {
            try {
                return (T) imp.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
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
