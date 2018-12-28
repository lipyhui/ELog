# ELog
&emsp;&emsp;ELog是一个Android Log管理器。ELog格式美观；没有Log 4K限制；可自由配置参数；可自由扩展。ELog可以实现一个进程一种配置，可满足不同文件需要打印不同日志格式的情况。

## ELog使用配置
### gradle 配置
#### 第一步：添加JitPack仓库到你项目中
&emsp;&emsp;在工程根目录的build.gradle中添加如下maven:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

#### 第二步：添加ELog库
&emsp;&emsp;在应用的build.gradle中添加ELog:
```
dependencies {
	implementation 'com.github.lipyhui:ELog:v1.0.2'
}
```

### maven 配置
#### 第一步：添加JitPack仓库到你项目中
```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```

#### 第二步：添加ELog库
```
<dependency>
	<groupId>com.github.lipyhui</groupId>
	<artifactId>ELog</artifactId>
	<version>v1.0.2</version>
</dependency>
```

## ELog使用说明
### ELog效果图
![图一](https://raw.githubusercontent.com/lipyhui/ELog/master/screenshots/1.png)
![图二](https://raw.githubusercontent.com/lipyhui/ELog/master/screenshots/2.png)
![图三](https://raw.githubusercontent.com/lipyhui/ELog/master/screenshots/3.png)
![图四](https://raw.githubusercontent.com/lipyhui/ELog/master/screenshots/4.png)
![图五](https://raw.githubusercontent.com/lipyhui/ELog/master/screenshots/5.png)

### ELog初始化
&emsp;&emsp;ELog默认会初始化一个Logcat日志打印器，因此不初始化也能正常使用ELog。
当然我们也可以通过ELogConfigs自定义一些配置项来实现个性化定制，同时我们可以在不同进程中使用不同的配置。配置项说明如下：
```
val configs = ELogConfigs.Builder()
  .enableLogcat()                                         //使能logcat日志打印器，当配置项没有使用磁盘缓存（enableDiskLog）和添加logAdapter时，会默认添加一个Logcat日志打印器。默认关闭
  .enableDiskLog()                                        //使能磁盘日志打印器。默认关闭
  .setPrinter(CustomPrinter)                              //自定义日志打印管理器。
  .addLogAdapter(CustomLogAdapters)                       //自定义日志打印适配器。
  .setTag("MyTestConfigs")                                //设置TAG，如果没logcatTag或diskTag，则logcatTag或diskTag使用该Tag。
  //logcat配置项
  .setLogcatTag("TestLogcatTag")                          //设置logcatTag，如果设置了该项，则不使用setTag配置的tag。
  .setLogcatShowBorder(false)                             //设置logcat是否显示边框和分割线。默认显示
  .setDiskDebugPriority(Log.WARN)                         //设置logcat日志打印最低级别，配置为ELogConfigs.DEBUG_STOP则不打印日志。默认VERBOSE级别
  .setLogcatMethodCount(7)                                //设置logcat显示方法数，设置为0可取消方法打印。默认2
  .setLogcatMethodOffset(2)                               //设置logcat方法偏移量。默认0
  .setLogcatShowThreadInfo(false)                         //设置logcat是否显示进程信息。默认显示
  .setLogcatLogStrategy(CustomLogStrategy)                //自定义logcat日志打印器。
  //disk日志配置项
  .setDiskTag("TestDiskTag")                              //设置diskTag，如果设置了该项，则不使用setTag配置的tag。
  .setDiskShowTimeMs(true)                                //设置是否显示毫秒时间。默认不显示
  .setDiskShowThreadInfo(false)                           //设置是否显示线程和包名。默认显示
  .setDiskDebugPriority(ELogConfigs.DEBUG_STOP)           //设置磁盘日志打印最低级别，配置为ELogConfigs.DEBUG_STOP则不打印日志。默认VERBOSE级别
  .setDiskDate(Date(2018, 1, 1, 24, 58))                  //设置磁盘日志时间。默认系统时间
  .setDiskDateFormat(SimpleDateFormat("MM.dd HH:mm"))     //设置磁盘日志格式。默认“yyyy.MM.dd HH:mm:ss.SSS”
  .setDiskLogStrategy(CustomLogStrategy)                  //自定义磁盘日志打印器。
  .setDiskPath(CustomDiskPath)                            //自定义磁盘日志保存路径，文件保存路径为“CustomDiskPath/ELog/*”。默认日志路径为：“Sdcard/ELog/*”
  .setDiskFileSizeKB(1024)                            	  //设置磁盘单个文件大小，单位KB。默认500KB
  .build()

  //设置自定义配置项
  ELog.init(configs)
```

### ELog打印日志
```
//常用方式
ELog.v("verbose")
ELog.d("debug")
ELog.i("info")
ELog.w("warn")
ELog.e("error")
ELog.wtf("assert")

//特殊方式
ELog.t("TEST_TAG").hex(0x08)	//追加tag
ELog.hex("bytes is", byteArrayOf(0x14, 0x33, 0x02, 0x15, 0xf2.toByte(), 0x0e))	//16进制打印byte数组
ELog.v("%d, %s, %6f : %3f", 1, "test", 5F, 10F)	//格式化打印日志
ELog.json(JSON_DATA)	//格式化打印json数据
ELog.xml(XML_DATA)	//格式化打印xml数据
ELog.d(MAP)	//打印Map
ELog.d(SET)	//打印Set
ELog.d(LIST)	//打印List
ELog.d(ARRAY)	//打印Array
```

## 感谢
&emsp;&emsp;ELog是在[logger](https://github.com/orhanobut/logger)的基础上再封装，并完善部分功能。
