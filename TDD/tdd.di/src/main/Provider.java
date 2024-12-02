package main;

import java.util.List;

interface Provider<T> {
    T get(Context context);

    List<Class<?>> getDependencies();
}
