package test;

import main.ContextConfig;
import main.CyclicDependencyException;
import test.utils.DependencyCycleComponent;
import main.DependencyNotFoundException;
import main.IllegalComponentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import test.utils.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {
    @Nested
    public class ComponentConstruction {
        ContextConfig config;

        @BeforeEach
        public void setup() {
            config = new ContextConfig();
        }

        @Test
        public void should_bind_type_to_a_specific_instance() throws DependencyNotFoundException {
            Component component = new Component() {
            };

            config.bind(Component.class, component);
            assertSame(component, config.getContext().get(Component.class).get());
        }

        @Nested
        public class ConstructorInjection {
            @Test
            public void should_bind_type_with_default_constructor() throws IllegalComponentException, DependencyNotFoundException {
                config.bind(Component.class, ComponentWithDefaultCtor.class);
                Component instance = config.getContext().get(Component.class).get();
                assertNotNull(instance);
            }

            @Test
            public void should_bind_type_with_injection_constructor() throws IllegalComponentException, DependencyNotFoundException {
                Dependency dependency = new Dependency() {
                };
                config.bind(Dependency.class, dependency);
                config.bind(Component.class, ComponentWithInjectCtor.class);
                ComponentWithInjectCtor instance = (ComponentWithInjectCtor) config.getContext().get(Component.class).get();
                assertNotNull(instance);
                assertSame(instance.dependency, dependency);
            }

            @Test
            public void should_bind_type_with_transitive_dependencies() throws IllegalComponentException, DependencyNotFoundException {
                config.bind(Component.class, ComponentWithInjectCtor.class);
                config.bind(Dependency.class, DependencyWithInjectCtor.class);
                config.bind(String.class, "str-dependency");

                ComponentWithInjectCtor instance = (ComponentWithInjectCtor) config.getContext().get(Component.class).get();
                DependencyWithInjectCtor dependency = (DependencyWithInjectCtor) instance.dependency;
                assertNotNull(instance);
                assertEquals("str-dependency", dependency.dependency);
            }

            @Test
            public void should_throw_error_when_multi_ctor_with_dependency() {
                assertThrows(IllegalComponentException.class, () -> config.bind(Component.class, ComponentWithMultiCtor.class));
            }

            @Test
            public void should_throw_error_when_no_default_ctor() {
                assertThrows(IllegalComponentException.class, () -> config.bind(Component.class, ComponentNoDefaultCtor.class));
            }

            @Test
            public void should_throw_error_when_dependency_not_exist() {
                assertThrows(IllegalComponentException.class, () -> config.bind(Component.class, ComponentNoDefaultCtor.class));
            }

            @Test
            public void should_throw_error_when_dependency_not_found() {
                config.bind(Component.class, ComponentWithInjectCtor.class);
                DependencyNotFoundException exception = assertThrows(DependencyNotFoundException.class, () -> config.getContext());
                assertSame(Dependency.class, exception.getDependency());
                assertSame(Component.class, exception.getComponent());
            }

            @Test
            public void should_throw_error_when_long_dependency_not_found() {
                config.bind(Dependency.class, DependencyWithInjectCtor.class);
                config.bind(Component.class, ComponentWithInjectCtor.class);

                DependencyNotFoundException exception = assertThrows(DependencyNotFoundException.class, () -> config.getContext());
                assertSame(String.class, exception.getDependency());
                assertSame(Dependency.class, exception.getComponent());
            }

            @Test
            public void should_be_empty_when_no_instance() {
                Optional<Component> component = config.getContext().get(Component.class);
                assertTrue(component.isEmpty());
            }

            @Test
            public void should_throw_error_when_cycle_dependency() {
                config.bind(Component.class, ComponentWithInjectCtor.class);
                config.bind(Dependency.class, DependencyCycleComponent.class);

                CyclicDependencyException exception = assertThrows(CyclicDependencyException.class, () -> config.getContext());

                List<Class<?>> components = exception.getComponents();
                assertSame(2, components.size());
            }

            @Test
            public void should_throw_error_when_long_cycle_dependency() {
                config.bind(Component.class, ComponentWithInjectCtor.class);
                config.bind(Dependency.class, DependencyCycleComponentA.class);
                config.bind(DependencyB.class, DependencyCycleComponentB.class);

                CyclicDependencyException exception = assertThrows(CyclicDependencyException.class, () -> config.getContext());

                List<Class<?>> components = exception.getComponents();
                assertSame(3, components.size());
            }
        }

        @Nested
        public class FieldInjection {
            @Test
            public void should_inject_via_field() {
                Dependency dependency = new Dependency() {
                };

                config.bind(Dependency.class, dependency);
                config.bind(ComponentAWithFieldInject.class, ComponentAWithFieldInject.class);

                ComponentAWithFieldInject component = config.getContext().get(ComponentAWithFieldInject.class).get();

                assertSame(component.dependency, dependency);
            }
        }
    }
}

