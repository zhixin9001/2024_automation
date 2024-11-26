package test;

import main.Context;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ContainerTest {
    interface Component {
    }

    static class ComponentWithDefaultCtor implements Component {
        public ComponentWithDefaultCtor() {
        }
    }

    @Nested
    public class ComponentConstruction {

        @Test
        public void should_bind_type_to_a_specific_instance() {
            Context context = new Context();
            Component component = new Component() {
            };

            context.bind(Component.class, component);
            assertSame(component, context.get(Component.class));
        }

        @Nested
        public class ConstructorInjection {
            @Test
            public void should_bind_type_with_default_constructor() {
                Context context = new Context();
                context.bind(Component.class, ComponentWithDefaultCtor.class);
                Component instance = context.get(Component.class);
                assertNotNull(instance);
            }
        }
    }
}
