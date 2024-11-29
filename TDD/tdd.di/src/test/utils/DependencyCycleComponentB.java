package test.utils;

import main.Inject;

public class DependencyCycleComponentB implements DependencyB {
    @Inject
    public DependencyCycleComponentB(Component component) {
    }
}
