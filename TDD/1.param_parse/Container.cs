namespace _1.param_parse;

public class Container
{
    private readonly Dictionary<Type, Type> _dictionary = new();
    private readonly Dictionary<Type, bool> _dependencies = new();

    public void Add<TService, TImp>()
    {
        _dictionary.Add(typeof(TService), typeof(TImp));
    }

    private object Get(Type type)
    {
        if (!_dictionary.TryGetValue(type, out var imp))
        {
            throw new EntryPointNotFoundException();
        }

        if (_dependencies.TryGetValue(type, out var isConstructing))
        {
            if (isConstructing)
            {
                throw new SystemException("Cyclic dependency detected");
            }

            _dependencies[type] = true;
        }
        else
        {
            _dependencies.Add(type, true);
        }

        var constructors = imp.GetConstructors();
        if (constructors.Length > 1) throw new ApplicationException("Too many constructors");
        var constructor = constructors.First();
        var parameters = constructor.GetParameters();
        var parameterInstances = parameters.Select(p => Get(p.ParameterType)).ToArray();
        var result = constructor.Invoke(parameterInstances);
        _dependencies[type] = false;
        return result;
    }

    public T? Get<T>()
    {
        return (T?)Get(typeof(T));
    }
}