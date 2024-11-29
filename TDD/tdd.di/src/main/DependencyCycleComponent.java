package main;

import test.utils.Component;
import test.utils.Dependency;

public class DependencyCycleComponent implements Dependency {
    @Inject
    public DependencyCycleComponent(Component component) {
    }
}
