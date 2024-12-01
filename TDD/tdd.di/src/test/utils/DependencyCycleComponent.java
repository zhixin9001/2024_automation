package test.utils;

import main.Inject;

public class DependencyCycleComponent implements Dependency {
    @Inject
    public DependencyCycleComponent(Component component) {
    }
}
