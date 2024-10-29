public class Args
{
  public static Options Parse(string args)
  {
    var argList = args.Split("-").ToList();
    bool logging = ParseLogging(argList);
    int? port = ParsePort(argList);

    string? dir = ParseDirectory(argList);

    return new Options(logging, port, dir);
  }

  private static string? ParseDirectory(List<string> argList)
  {
    var dirOption = argList.Find(a => a.StartsWith("d"));
    string? dir = null;
    if (!string.IsNullOrEmpty(dirOption))
    {
      var dirSplit = dirOption.Split(" ");

      if (dirSplit.Length > 2 && !string.IsNullOrEmpty(dirSplit[2]))
      {
        throw new ArgumentException(dirOption);
      }
      dir = dirSplit[1];
    }

    return dir;
  }

  private static int? ParsePort(List<string> argList)
  {
    var portOption = argList.Find(a => a.StartsWith("p"));
    int? port = null;
    if (!string.IsNullOrEmpty(portOption))
    {
      var portSplit = portOption.Split(" ");

      if (portSplit.Length > 2 && !string.IsNullOrEmpty(portSplit[2]))
      {
        throw new ArgumentException(portOption);
      }
      port = int.Parse(portSplit[1]);
    }

    return port;
  }

  private static bool ParseLogging(List<string> argList)
  {
    var loggingOption = argList.Find(a => a.StartsWith("l"));
    bool logging = false;
    if (!string.IsNullOrEmpty(loggingOption))
    {
      var loggingSplit = loggingOption.Split(" ");
      if (loggingSplit.Length > 1 && !string.IsNullOrEmpty(loggingSplit[1]))
      {
        throw new ArgumentException(loggingOption);
      }
      logging = true;
    }

    return logging;
  }
}