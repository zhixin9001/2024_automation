package test;

import main.Context;
import main.DependencyNotFoundException;
import main.IllegalComponentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import test.utils.*;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {
    @Nested
    public class ComponentConstruction {
        Context context;

        @BeforeEach
        public void setup() {
            context = new Context();
        }

        @Test
        public void should_bind_type_to_a_specific_instance() throws DependencyNotFoundException {
            Component component = new Component() {
            };

            context.bind(Component.class, component);
            assertSame(component, context.get(Component.class));
        }

        @Nested
        public class ConstructorInjection {
            @Test
            public void should_bind_type_with_default_constructor() throws IllegalComponentException, DependencyNotFoundException {
                context.bind(Component.class, ComponentWithDefaultCtor.class);
                Component instance = context.get(Component.class);
                assertNotNull(instance);
            }

            @Test
            public void should_bind_type_with_injection_constructor() throws IllegalComponentException, DependencyNotFoundException {
                Dependency dependency = new Dependency() {
                };
                context.bind(Dependency.class, dependency);
                context.bind(Component.class, ComponentWithInjectCtor.class);
                ComponentWithInjectCtor instance = (ComponentWithInjectCtor) context.get(Component.class);
                assertNotNull(instance);
                assertSame(instance.dependency, dependency);
            }

            @Test
            public void should_bind_type_with_transitive_dependencies() throws IllegalComponentException, DependencyNotFoundException {
                context.bind(Component.class, ComponentWithInjectCtor.class);
                context.bind(Dependency.class, DependencyWithInjectCtor.class);
                context.bind(String.class, "str-dependency");

                ComponentWithInjectCtor instance = (ComponentWithInjectCtor) context.get(Component.class);
                DependencyWithInjectCtor dependency = (DependencyWithInjectCtor) instance.dependency;
                assertNotNull(instance);
                assertEquals("str-dependency", dependency.dependency);
            }

            @Test
            public void should_throw_error_when_multi_ctor_with_dependency() {
                assertThrows(IllegalComponentException.class, () -> context.bind(Component.class, ComponentWithMultiCtor.class));
            }

            @Test
            public void should_throw_error_when_no_default_ctor() {
                assertThrows(IllegalComponentException.class, () -> context.bind(Component.class, ComponentNoDefaultCtor.class));
            }

            @Test
            public void should_throw_error_when_dependency_not_exist() {
                assertThrows(IllegalComponentException.class, () -> context.bind(Component.class, ComponentNoDefaultCtor.class));
            }

            @Test
            public void should_throw_error_when_dependency_not_found() {
                context.bind(Component.class, ComponentWithInjectCtor.class);
                assertThrows(RuntimeException.class, () -> context.get(Component.class));
            }
        }
    }
}

