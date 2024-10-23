public class Args
{
  public static Options Parse(params string[] args)
  {
    return new Options(args.Contains("-l"), 0, "");
  }
}