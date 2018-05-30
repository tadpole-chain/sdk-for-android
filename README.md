sdk-for-android
====

接入说明
----

# 1.导出并复制项目
从Unity导出Android项目（A），并复制一份（B），作为Android Studio编辑的项目。之后每次导出都用A，然后将A项目里src\main\assets\bin中的文件拷贝到项目B中相应的位置（因为每次导出，都只有这一部分数据会发生变化）。
# 2.合并资源文件
将res中的文件全部复制到项目里src\main\res中，如果有冲突，则直接覆盖。资源文件已做处理，不会影响游戏运行。
# 3.引入SDK
将文件tct_v1.0.2.jar拷贝到项目libs文件夹下。<br>
在项目中的AndroidManifest.xml文件中加入相关权限：<br>
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

在app中的AndroidManifest.xml文件中加入SDK的登录界面声明：<br>
```xml
<activity android:name="com.tadpolechain.LoginActivity"
    android:theme="@style/LoginDialog" />
```

# 4.合并activity
将UnityPlayerActivity文件和项目中的UnityPlayerActivity文件合并，替换除了包名（即第一行的package）以外的所有内容，并将onCreate方法里的app key和secret key替换为自己的。
# 5.修改版本号和游戏名称
如果需要修改版本号，则打开AndroidManifest.xml文件，修改以下内容：<br>
![](http://img.suncity.ink/github/2018/05/git_0001.png) 
如果修改游戏名称，则打开文件src\main\res\values\strings.xml，修改app_name:<br>
![](http://img.suncity.ink/github/2018/05/git_0002.png) 
# 6.引入Unity封装文件
将TCTForUnity.cs拷贝到项目相应目录。TCTForUnity中的方法已经对接到SDK，可以直接调用。调用范例如TestClick.cs文件所示。
# 7.回调方法
部分SDK方法的调用需要回调，比如登录功能，TestClick.cs中的调用方法如下所示：<br>
![](http://img.suncity.ink/github/2018/05/git_0003.png)
项目目录如下：<br>
![](http://img.suncity.ink/github/2018/05/git_0004.png)
第一个参数Canvas代表的是脚本所挂载的场景名称，第二个参数代表的是回调方法名称。
# 8. 封装方法简介
## 登录方法(login)
调用即可登录，可以添加回调方法，回调方法的参数是一个字符串，如果登录成功则返回success，然后可以获得用户信息，如果登录失败，则返回失败的原因。
## 上线方法(online)
如果之前登录过游戏，再次打开游戏会自动上线，此方法仅用于传递回调方法。
## 获得SDK状态(getStatus)
登录状态有以下值：<br>
```table
TCTForUnity.Status_Unavailable	0	
TCTForUnity.Status_Initial	1	
TCTForUnity.Status_Login	201	已经登录
TCTForUnity.Status_Online	200	已经上线
TCTForUnity.Status_Offline	500	已经下线
```

## 获得用户信息(getUserInfo)
可以获得字符串类型的用户信息，暂时仅支持以下参数：Id、Nickname、Avatar。
## 显示广告(showAd)
调用可以显示广告信息。
## 发送公告(showMsg)
调用此方法，传入一个字符串，则可以显示在上面的跑马灯公告栏。
## 发送得分(sendScore)
调用此方法，可以向服务器发送一个float型的分数，用户排行榜排序。此方法可以带回调。
## 发送其他数据(sendData)
调用此方法，可以向服务器发送一个键值对。其中key是一个小于100字节的字符串，代表了数据种类，score是一个float型的分数，可以用于数据统计和分析。此方法可以带回调。

