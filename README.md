# Baldur's Gate Scripting Helper (博德之门脚本编辑工具)

### 介绍
功能：自动完成宏替换，方便脚本编写。

环境要求：JDK8+。

重要概念和相关说明

- 字典
  在 dict.txt 文件中定义。程序对待字典的方式是，比较字符，直接替换。所以字典的定义需要自行避免重复字段，所以推荐用 @name 的方式
  定义字典变量名。

- 宏
  宏是一段包含参数的方法，用于实现对目标方法参数的自动填充。
  宏文件是 resource/macro，以 key value 方式定义。宏以 $ 符号开头，表示这是一个宏。
  宏

- 模板
  模板表示一大段脚本代码，可以是原生代码，也可以是包含字典定义的自定义脚本代码，也可以是宏。
  模板的定义在 dict.txt 文件中，以~符号开头，作为模板的识别标志。按照 key value 形式定义，以空格分隔。
  模板的脚本数据存放在 resource/module/..目录下。
  在定义模板时，需要保证 value 是不含后缀的文件名。

- 保留函数
  程序自己实现的一些函数，用于实现特定功能。详情见下面 保留函数 部分。
  保留函数的关键字不允许在input等脚本文件中被重新定义。


文件说明：

1. input.txt 你自己写的脚本

2. output.txt 经过命令替换后可在《博德之门》运行的脚本

3. dict.txt 命令映射关系。可以自己修改

4. 建立 resource 目录，其中放模板代码

dict.txt文件的说明：

1. 配置你自己喜欢的命令映射关系。最好用特殊符号作为开头。

2. 行与行之间不要有空行，不然会报错。

3. 格式遵循kv的形式

4. 支持中文

5. 每个映射一行

6. 模板代码的用法。
- k 自定义，需要用$来指明。
- v 用resource目录下的对应模板文件名，不含文件类型后缀。

```
例如：

dict.txt文件中定义

~法术定序3火球 seq3fireball

则程序会在resource目录下找文件名为seq3fireball的代码块，并替换任何脚本中出现的 ~法术定序3火球
这一段。注意大小写。
```

7. 宏命令 MacroCMD 的写法

- 在 resource/macro 文件中定义宏。每个宏一行。宏命令以 $ 符号开头。

- 元素用 var 表示，并在定义中指明每个元素的出现位置。元素列表用 () 包裹起来。

- 参数之间用逗号隔开，遵从一般的方法传参习惯。

- 对参数顺序没有要求，但是需要一一对应。

- 宏命令中，可以不包含元素列表。这样就退化为简单的命令替换。

- 不支持宏命令嵌套，即一个宏里面，嵌套另一个宏。

下面展示一个宏命令的示例：

```
在 macro 文件中定义宏：

    $timer(p1,p2) SetGlobalTimer(p1,"LOCALS",p2)

在脚本代码文件中写如下代码：

    $timer(a,b)

解析结果：

    SetGlobalTimer(a,"LOCALS",b)
```

8. 资源加载顺序

- dict.txt

- resource/macro

- resource/module/..

- input.txt

9. 保留函数

- if 函数
```
if (cond1) {
    #100
    执行代码
}

if (cond1 && cond2) {
    #100
    执行代码
}
```

- or 函数
只用于 IF 语句
```
if (cond1 && OR(cond2 || cond3)) {
    #100
    ...
}
```
效果为
```
IF
    cond1
    OR
        cond2
        cond3
    RESPONSE 100
        ...

```

- loop 函数
用于对大括号内的代码段重复执行

```
loop (index,1,5) {
    @RFS(@<index>NOM,@火球术)
}
```
效果为：
@RFS(@1NOM,@火球术)
@RFS(@2NOM,@火球术)
@RFS(@2NOM,@火球术)
@RFS(@3NOM,@火球术)
@RFS(@3NOM,@火球术)


- lock 函数
```
lock{
    需要完整执行的代码
}
```

- foreach 函数
用于对大括号内的代码段，依次替换[] 中的元素
```
foreach(token,[tk1,tk2,tk3]){
    @RFS(@NEM,<token>)
}
```
效果为：
```
@RFS(@NEM,tk1)
@RFS(@NEM,tk2)
@RFS(@NEM,tk3)
```


### 版本记录

v0.3.6
- 解决语法嵌套的问题
- 实现 foreach 语法 ForeachFunction

v0.3.5
- 实现 lock 语法 LockFunction
- 实现 between 语法 BetweenFunction
- 修复若干 bug

v0.3.4
- 实现宏命令 macro 语法 MacroFunction
- 优化宏命令，简化语法
- 修复模板解析的 bug

v0.3.3
- 实现 OR 语法 OrFunction。
- 修复 OR 语句和 if 语句、loop 语句的兼容问题。

v0.3.2
- 实现 if 语法 IfFunction。

v0.3.1
- 实现 loop 语法 LoopFunction。

v0.2
- 实现模板脚本替换。
- 支持用户自定义配置模板。
- 实现模板解析。

v0.1
- 实现命令替换。
- 支持用户自定义命令。
