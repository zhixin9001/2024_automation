namespace _1.param_parse;

public class Container
{
    private Dictionary<Type, Type> _dictionary = new();

    public void Add<TService, TImp>()
    {
        _dictionary.Add(typeof(TService), typeof(TImp));
    }

    private Type Get(Type type)
    {
        if (_dictionary.TryGetValue(type, out var imp) == false)
        {
            throw new EntryPointNotFoundException();
        }

        return imp;
    }

    public T? Get<T>()
    {
        var imp = Get(typeof(T));
        var constructors = imp.GetConstructors();
        if (constructors.Length > 1) throw new ApplicationException("Too many constructors");
        var constructor = constructors.First();
        var parameters = constructor.GetParameters();
        var parameterInstances = parameters.Select(p => Activator.CreateInstance(Get(p.ParameterType))).ToArray();
        return (T?)constructor.Invoke(parameterInstances);
    }
}