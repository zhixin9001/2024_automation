package test.utils;

import main.Inject;

public class ComponentWithInjectCtor implements Component {
    public final Dependency dependency;

    @Inject
    public ComponentWithInjectCtor(Dependency dependency) {
        this.dependency = dependency;
    }
}
