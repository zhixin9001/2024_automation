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
        Options options = Args.Parse("");
        Assert.False(options.logging);
    }

    [Fact]
    public void Should_throw_error_when_logging_format_error()
    {
        Assert.Throws<ArgumentException>(() => Args.Parse("-l l"));
    }

    [Fact]
    public void Should_parse_port_correctly_when_exist()
    {
        Options options = Args.Parse("-l -p 8080");
        Assert.Equal(8080, options.port);
    }

    [Fact]
    public void Should_throw_error_when_port_format_error()
    {
        Assert.Throws<ArgumentException>(() => Args.Parse("-p 8080 8081"));
    }

    [Fact]
    public void Should_parse_directory_correctly_when_exist()
    {
        Options options = Args.Parse("-l -p 8080 -d /usr/tmp");
        Assert.Equal("/usr/tmp", options.directory);
    }
    
    [Fact]
    public void Should_throw_error_when_directory_format_error()
    {
        Assert.Throws<ArgumentException>(() => Args.Parse("-d /usr /tmp"));
    }

}