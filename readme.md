py -m venv auto

auto/Scripts/activate.ps1

pip3 install -r requirements.txt

start 'C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe' --remote-debugging-port=9222 --user-data-dir="C:\Users\Zhi.Xin\Personal\seleniumEdge"

C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe

taskkill /f /im msedge.exe

TDD DI
Lost 原因
- 心态问题，认为有些功能不实用，比如方法注入 field注入 provider等
- 语言问题，不懂java语法，好多讲解就跟不上了，而且在转换成c#的时候，也遇到不兼容问题
- 心智负担，看的java写的c#

跟着写java，学java，对比，转换成c#，哪个更困难？
    - 目前c#这条路失败了
    - 试试跟着学java
    - 还有什么办法？只看不练？no

## task list
- 组件构造
  - 无需构造的组件，直接返回实例
  - 如果组件不可实例化，抛出异常
    - 抽象类
    - 接口
  - 构造函数注入
    - 无依赖的组件，通过默认构造函数生成组件
    - 有依赖的组件，通过Inject标注的构造函数生成组件实例
    - 如果所以来的组件也有依赖，继续解析
    - 如果多个Inject标注，抛出异常
    - 如果组件的依赖不存在，抛出异常
    - 循环依赖，抛出异常
  - 字段注入
    - 通过Inject标注解析字段
    - 依赖不存在，抛出异常
    - final字段，抛出异常
    - 循环依赖，抛出异常
  - 方法注入
    - 通过Inject标注的方法，参数为依赖组件
    - Inject标注的无参方法会被调用
    - 按照子类规则，覆盖父类中的Inject方法
    - 如果组件的依赖不存在，抛出异常
    - 如果方法有类型参数，抛出异常
    - 循环依赖，抛出异常
- 依赖选择
- 生命周期