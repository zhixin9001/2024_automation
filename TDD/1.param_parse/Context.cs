public class Context
{
    private readonly Dictionary<Type, Func<object?>> _provider = new();

    public void Bind<T>(T type, object instance) where T : Type
    {
        _provider.Add(type, () => instance);
    }

    public void Bind<T1, T2>(T1 type, T2 instanceType) where T1 : Type where T2 : Type, T1
    {
        _provider.Add(type, () =>
        {
            var constructor = instanceType.GetConstructors().First();
            var parameters = constructor.GetParameters();
            var parameterInstances = parameters.Select(p => Get<object>(p.ParameterType)).ToArray();
            return constructor.Invoke(parameterInstances);
        });
    }

    public T? Get<T>(Type type) where T : class
    {
        _provider.TryGetValue(type, out var func);
        return func?.Invoke() as T;
    }
}