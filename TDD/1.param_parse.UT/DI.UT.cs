namespace _1.param_parse.UT;

public class ComponentConstructionTests
{
    interface IComponent
    {
    }

    class Component : IComponent
    {
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
    //FailedInjection

    //MethodInjection
}