package test.utils;

import main.Inject;

public class ComponentWithMultiCtor implements Component {
    @Inject
    public ComponentWithMultiCtor(String a) {
    }

    @Inject
    public ComponentWithMultiCtor(Integer b) {
    }

}
