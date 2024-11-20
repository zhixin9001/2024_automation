public class Context
{
    private readonly Dictionary<Type, Func<object?>> _provider = new();

    private readonly Dictionary<Type, bool> _initMark = new();

    public void Bind<T>(T type, object instance) where T : Type
    {
        _provider.Add(type, () => instance);
    }

    public void Bind<T1, T2>(T1 type, T2 instanceType) where T1 : Type where T2 : Type, T1
    {
        _provider.Add(type, () => Invoke1<T1, T2>(instanceType));
    }

    private object? Invoke1<T1,T2>(T2 instanceType) where T2 : Type, T1
    {
        if (_initMark.ContainsKey(instanceType.GetType()))
        {
            _initMark[instanceType.GetType()] = true;
        }
        else
        {
            _initMark.Add(instanceType.GetType(), true);
        }
        var constructors = ((Type)instanceType).GetConstructors();
        if (constructors.Length > 1) throw new ApplicationException("Too many constructors");
        var constructor = ((Type)instanceType).GetConstructors().First();
        var parameters = constructor.GetParameters();
        var parameterInstances = parameters.Select(p => Get<object>(p.ParameterType)).ToArray();
        if (parameterInstances.Any(a => a is null)) throw new EntryPointNotFoundException();
        return constructor.Invoke(parameterInstances);
    }

    public T? Get<T>(Type type) where T : class
    {
        _provider.TryGetValue(type, out var func);
        return func?.Invoke() as T;
    }
}