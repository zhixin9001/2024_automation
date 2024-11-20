namespace _1.param_parse.UT;

public class ComponentConstructionTests
{
    private Context _context;

    public ComponentConstructionTests()
    {
        _context = new Context();
    }

    //ConsructorIjection
    //  No args ctor
    //  with dependency
    //  a->b->c
    [Fact]
    public void ShouldBindTypeToASpecificInstance()
    {
        var component = new Component();
        _context.Bind(typeof(IComponent), component);
        Assert.Same(component, _context.Get<IComponent>(typeof(IComponent)));
    }

    [Fact]
    public void ShouldBindTypeToAClassWithDefaultCtor()
    {
        _context.Bind(typeof(IComponent), typeof(Component));
        var instance = _context.Get<IComponent>(typeof(IComponent));
        Assert.NotNull(instance);
        Assert.IsType<Component>(instance);
    }

    [Fact]
    public void ShouldBindTypeToAClassWithDependency()
    {
        _context.Bind(typeof(IComponent), typeof(Component));
        _context.Bind(typeof(IComponentA), typeof(ComponentA));
        var instance = _context.Get<IComponentA>(typeof(IComponentA));
        Assert.NotNull(instance);
        Assert.IsType<ComponentA>(instance);
        Assert.NotNull(((ComponentA)instance).Component);
        Assert.IsType<Component>(((ComponentA)instance).Component);
    }
    //FailedInjection

    [Fact]
    public void ShouldThrowExceptionWhenMultipleCtor()
    {
        _context.Bind(typeof(IComponent), typeof(ComponentWithMultipleCtor));
        Assert.Throws<ApplicationException>(() => _context.Get<IComponent>(typeof(IComponent)));
    }

    [Fact]
    public void ShouldThrowExceptionWhenDependencyNotFound()
    {
        _context.Bind(typeof(IComponentA), typeof(ComponentA));
        Assert.Throws<EntryPointNotFoundException>(() => _context.Get<IComponentA>(typeof(IComponentA)));
    }

    [Fact]
    public void ShouldThrowExceptionWhenCyclicDependencyExists()
    {
        _context.Bind(typeof(IComponentCycle1), typeof(ComponentCycle1));
        _context.Bind(typeof(IComponentCycle2), typeof(ComponentCycle2));
        // Assert.Throws<NotSupportedException>(()=>_context.Get<IComponentCycle1>(typeof(IComponentCycle1)));
    }
    //MethodInjection
}

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

class ComponentWithMultipleCtor : IComponent
{
    public ComponentWithMultipleCtor(string a)
    {
    }

    public ComponentWithMultipleCtor(string a, string b)
    {
    }
}

interface IComponentCycle1
{
}

interface IComponentCycle2
{
}

class ComponentCycle1 : IComponentCycle1
{
    public IComponentCycle2 ComponentCycle2 { get; }

    public ComponentCycle1(IComponentCycle2 componentCycle2)
    {
        ComponentCycle2 = componentCycle2;
    }
}

class ComponentCycle2 : IComponentCycle2
{
    public IComponentCycle1 ComponentCycle1 { get; }

    public ComponentCycle2(IComponentCycle1 componentCycle1)
    {
        ComponentCycle1 = componentCycle1;
    }
}