public class Args
{
  public static Options Parse(string args)
  {
    var argList = args.Split("-").ToList();
    var loggingOption = argList.Find(a => a.StartsWith("l"));

    var portOption = argList.Find(a => a.StartsWith("p"));
    int? port = null;
    if (!string.IsNullOrEmpty(portOption))
    {
      var portString = portOption.Split(" ")[1];
      port = int.Parse(portString);
    }

    return new Options(!string.IsNullOrEmpty(loggingOption), port, "");
  }
}