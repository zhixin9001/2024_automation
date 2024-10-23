namespace _1.param_parse.UT;
public class UnitTest1
{
    // [Fact(Skip ="")]
    // public void Example1()
    // {
    //     Options options = Args.Parse("-l", "-p", "8080", "-d", "/a/b");
    //     Assert.True(options.logging);
    //     Assert.Equal(8080, options.port);
    // }
    // sample case
    // -l
    // -p 8080
    // -d /a/b
    // completed case
    // -l -p 8080 -d /a/b
    // sad path
    // -l t, -l e w 
    // -p, -p 8080 8081
    // -d, -d /a/b /c/d
    // default value
    // -l, false
    // -p,0
    // -d "" 

    [Fact]
    public void Should_set_logging_to_true_when_logging_present()
    {
        Options options = Args.Parse("-l");
        Assert.True(options.logging);
    }

    [Fact]
    public void Should_set_logging_to_false_when_logging_not_present()
    {
        Options options = Args.Parse();
        Assert.False(options.logging);
    }

}