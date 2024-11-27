package test.utils;

import main.Inject;

public class DependencyWithInjectCtor implements Dependency {
    public final String dependency;

    @Inject
    public DependencyWithInjectCtor(String dependency) {
        this.dependency = dependency;
    }
}
