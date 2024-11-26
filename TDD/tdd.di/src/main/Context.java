package main;

import test.ContainerTest;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final Map<Class<?>, Object> components = new HashMap<>();
    private final Map<Class<?>, Class<?>> componentImps = new HashMap<>();

    public <T> void bind(Class<T> type, T instance) {
        components.put(type, instance);
    }

    public <T, TImp extends T> void bind(Class<T> type, Class<TImp> imp) {
        componentImps.put(type, imp);
    }

    public <T> T get(Class<T> type) {
        if (components.containsKey(type)) {
            return (T) components.get(type);
        }
        Class<?> imp = componentImps.get(type);
        try {
            return (T) imp.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
