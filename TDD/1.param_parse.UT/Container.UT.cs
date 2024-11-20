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
    public void ShouldBindTypeToAClassWithMultiLevelDependency()
    {
        _container.Add<IMultiLevelA, MultiLevelA>();
        _container.Add<IMultiLevelB, MultiLevelB>();
        _container.Add<IMultiLevelC, MultiLevelC>();
        var instance = _container.Get<IMultiLevelA>();
        Assert.NotNull(instance);
        Assert.IsType<MultiLevelA>(instance);
    }

    [Fact]
    public void ShouldThrowExceptionWhenDependencyNotFound()
    {
        _container.Add<IComponentA, ComponentA>();
        Assert.Throws<EntryPointNotFoundException>(() => _container.Get<IComponentA>());
    }

    [Fact]
    public void ShouldThrowExceptionWhenCyclicDependency()
    {
        _container.Add<ICyclicA, CyclicA>();
        _container.Add<ICyclicB, CyclicB>();
        Assert.Throws<SystemException>(() => _container.Get<ICyclicB>());
    }

    [Fact]
    public void ShouldThrowExceptionWhenInterfact()
    {
        Assert.Throws<ArgumentException>(()=> _container.Add<ICyclicA, ICyclicB>());
    }
}

interface ICyclicA
{
}

interface ICyclicB
{
}

class CyclicA : ICyclicA
{
    public CyclicA(ICyclicB cyclicB)
    {
    }
}

class CyclicB : ICyclicB
{
    public CyclicB(ICyclicA cyclicA)
    {
    }
}

interface IMultiLevelA
{
}

interface IMultiLevelB
{
}

interface IMultiLevelC
{
}

class MultiLevelA : IMultiLevelA
{
    public MultiLevelA(IMultiLevelB multiLevelB)
    {
    }
}

class MultiLevelB : IMultiLevelB
{
    public MultiLevelB(IMultiLevelC multiLevelC)
    {
    }
}

class MultiLevelC : IMultiLevelC
{
}