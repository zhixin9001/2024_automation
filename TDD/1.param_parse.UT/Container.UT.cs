namespace _1.param_parse.UT;

public class Container_UT
{
    private Container _container;

    public Container_UT()
    {
        _container = new Container();
    }

    [Fact]
    public void ShouldBindTypeToAClassWithDefaultCtor()
    {
        _container.Add<IComponent, Component>();
        var instance = _container.Get<IComponent>();
        Assert.NotNull(instance);
        Assert.IsType<Component>(instance);
    }

    [Fact]
    public void ShouldThrowExceptionWhenMultipleCtor()
    {
        _container.Add<IComponent, ComponentWithMultipleCtor>();
        Assert.Throws<ApplicationException>(() => _container.Get<IComponent>());
    }

    [Fact]
    public void ShouldBindTypeToAClassWithDependency()
    {
        _container.Add<IComponent, Component>();
        _container.Add<IComponentA, ComponentA>();
        var instance = _container.Get<IComponentA>();
        Assert.NotNull(instance);
        Assert.IsType<ComponentA>(instance);
        Assert.NotNull(((ComponentA)instance).Component);
        Assert.IsType<Component>(((ComponentA)instance).Component);
    }

    [Fact]
    public void ShouldThrowExceptionWhenDependencyNotFound()
    {
        _container.Add<IComponentA, ComponentA>();
        Assert.Throws<EntryPointNotFoundException>(() => _container.Get<IComponentA>());
    }
}