namespace _1.param_parse.UT;

public class ComponentConstructionTests
{
    interface IComponent
    {
    }

    interface IComponentA
    {
    }

    class Component : IComponent
    {
    }

    class ComponentA : IComponentA
    {
        public IComponent Component { get; }

        public ComponentA(IComponent component)
        {
            Component = component;
        }
    }


    //ConsructorIjection
    //  No args ctor
    //  with dependency
    //  a->b->c
    [Fact]
    public void ShouldBindTypeToASpecificInstance()
    {
        Context context = new();
        var component = new Component();
        context.Bind(typeof(IComponent), component);
        Assert.Same(component, context.Get<IComponent>(typeof(IComponent)));
    }

    [Fact]
    public void ShouldBindTypeToAClassWithDefaultCtor()
    {
        Context context = new();
        context.Bind(typeof(IComponent), typeof(Component));
        var instance = context.Get<IComponent>(typeof(IComponent));
        Assert.NotNull(instance);
        Assert.IsType<Component>(instance);
    }

    [Fact]
    public void ShouldBindTypeToAClassWithDependency()
    {
        Context context = new();
        context.Bind(typeof(IComponent), typeof(Component));
        context.Bind(typeof(IComponentA), typeof(ComponentA));
        var instance = context.Get<IComponentA>(typeof(IComponentA));
        Assert.NotNull(instance);
        Assert.IsType<ComponentA>(instance);
        Assert.NotNull(((ComponentA)instance).Component);
        Assert.IsType<Component>(((ComponentA)instance).Component);
    }
    //FailedInjection

    //MethodInjection
}