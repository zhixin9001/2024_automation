package main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class ConstructorInjectionProvider<T> implements Provider {
    private final Constructor<T> constructor;

    public ConstructorInjectionProvider(Class<T> imp) {
        this.constructor = getConstructor(imp);
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

    @Override
    public Object get(Context context) {
        try {
            Object[] dependencies = stream(this.constructor.getParameters())
                    .map(p -> context.get(p.getType()).get()).toArray(Object[]::new);
            return this.constructor.newInstance(dependencies);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Class<?>> getDependencies() {
        return stream(constructor.getParameters()).map(Parameter::getType).collect(Collectors.toList());
    }
}
