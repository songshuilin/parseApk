# parseApk
## 软件介绍
parseApk 是一个APK信息文件提取工具， 基于开源的修改，然后未完成的功能完善并补充。

已实现了功能  
- 获取应用名，支持提取多语言名称  
- 获取包名，版本名，版本号，文件MD5 ,文件大小
- 读取APK图标
- 支持提取APK对应的权限，并且给出权限注释  
- 获取apk的签名信息
## 相关说明  
- 基于开源项目修改 ： https://github.com/ghboke/APKMessenger  
- exe4jtool目录下，  是通过exe4j打包成exe文件。 里面是已弄好的了。 不需要安装jdk和配置环境，里面有使用说明文档。可以直接运行exe
- 开发工具：IDEA  
### 要读取apk信息，直接把文件拖到软件即可
![img](https://github.com/songshuilin/parseApk/blob/master/parseinfo1.png)![img](https://github.com/songshuilin/parseApk/blob/master/parseinfo2.png)
