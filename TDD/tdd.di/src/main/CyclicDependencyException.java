package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CyclicDependencyException extends RuntimeException {
    private List<Class<?>> components = new ArrayList<>();

    public CyclicDependencyException(Class<?> components) {
        this.components.add(components);
    }

    public CyclicDependencyException(Class<?> componentType, CyclicDependencyException e) {
        this.components.add(componentType);
        this.components.addAll(e.getComponents());
    }

    public CyclicDependencyException(Stack<Class<?>> visiting) {
        this.components.addAll(visiting);
    }

    public List<Class<?>> getComponents() {
        return this.components;
    }
}
