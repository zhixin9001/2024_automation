public class Context
{
    private Dictionary<Type, object?> components = new();

    public void Bind<T>(T type, object instance) where T : Type
    {
        components.Add(type, instance);
    }

    public void Bind<T1, T2>(T1 type, T2 instanceType) where T1 : Type where T2 : Type, T1

    {
        var instance = Activator.CreateInstance(instanceType);
        components.Add(type, instance);
    }

    public T? Get<T>(Type type) where T : class
    {
        components.TryGetValue(type, out var instance);
        return instance as T;
    }
}

public class Provider<T> where T : class
{
    private readonly Func<T> _func;

    public Provider(Func<T> func)
    {
        _func = func;
    }

    public virtual T? Get()
    {
        return _func();
    }
}