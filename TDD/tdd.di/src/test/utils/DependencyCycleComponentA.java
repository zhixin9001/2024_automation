package test.utils;

import main.Inject;

public class DependencyCycleComponentA implements Dependency {
    @Inject
    public DependencyCycleComponentA(DependencyB dependencyB) {
    }
}
