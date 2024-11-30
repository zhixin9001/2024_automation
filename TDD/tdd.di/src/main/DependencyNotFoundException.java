package main;

import test.utils.Component;

public class DependencyNotFoundException extends RuntimeException {
    private Class<?> component;
    private Class<?> dependency;

    public DependencyNotFoundException(Class<?> component, Class<?> dependency) {
        this.component = component;
        this.dependency = dependency;
    }

    public Class<?> getComponent() {
        return component;
    }

    public Class<?> getDependency() {
        return dependency;
    }
}
