sdk-for-android

[中文文档](./README.md "中文文档")

====
Unity access instructions
----

# 1. Export and copy the project
Export the Android project (A) from Unity and copy one (B) as a project edited by Android Studio. Then use A for each export, and then copy the files in src\main\assets\bin in the A project to the corresponding position in project B (because only this part of the data changes every time you export).
# 2. Merge resource files
Copy all the files in res to src\main\res in the project. If there is a conflict, it will be overwritten directly. The resource file has been processed and will not affect the game running.
# 3. Introducing the SDK
Copy the file tct_v1.1.0.jar to the project libs folder. <br>
Add the relevant permissions in the AndroidManifest.xml file in the project:<br>

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

Add the SDK login interface declaration in the AndroidManifest.xml file in the app:<br>

```xml
<activity android:name="com.tadpolechain.LoginActivity"
    android:theme="@style/LoginDialog" />
```

# 4. Merge activity
Combine the UnityPlayerActivity file with the UnityPlayerActivity file in the project, replace everything except the package name (ie the first line of the package), and replace the app key and secret key in the onCreate method with your own. <br>
[Note] The last parameter of the initialization method means whether it is being tested. If it is tested, it is true, and the test server data is called.
# 5. Modify the version number and game name
If you need to change the version number, open the AndroidManifest.xml file and modify the following:<br>
![](http://img.suncity.ink/github/2018/05/git_0001.png) <br>
If you modify the game name, open the file src\main\res\values\strings.xml and modify app_name:<br>![](http://img.suncity.ink/github/2018/05/git_0002.png) 
# 6. Introducing Unity package files
Copy TCTForUnity.cs to the appropriate directory of the project. The method in TCTForUnity has been connected to the SDK and can be called directly. Call the van as shown in the TestClick.cs file.
# 7. Callback method
Calls to some SDK methods require callbacks, such as the login function. The calling methods in TestClick.cs are as follows:<br>
![](http://img.suncity.ink/github/2018/05/git_0003.png)<br>
The project directory is as follows:<br>
![](http://img.suncity.ink/github/2018/05/git_0004.png)<br>
The first parameter, Canvas, represents the name of the scene that the script is mounted on, and the second parameter represents the name of the callback method.
# 8. Introduction to the package method
## Login method (login)
Call to log in, you can add a callback method, the parameter of the callback method is a string, if the login is successful, return success, and then get the user information, if the login fails, return the reason for the failure.
## Online method (online)
If you have logged in to the game before, opening the game again will automatically go online. This method is only used to pass the callback method.
## Get SDK status (getStatus)
The login status has the following values:<br>
```table
TCTForUnity.Status_Unavailable	0	
TCTForUnity.Status_Initial	1	
TCTForUnity.Status_Login	201	Already logged in
TCTForUnity.Status_Online	200	Already online
TCTForUnity.Status_Offline	500	Already offline
```

## Get user information (getUserInfo)
You can obtain user information of the string type. Currently, only the following parameters are supported: Id, Nickname, and Avatar.
## Send announcement (showMsg)
Call this method, passing in a string, you can display the marquee bulletin board above.
## Send score (sendScore)
Call this method, you can send a float type score to the server, sorting the user leaderboard. This method can take a callback.
## Send other data (sendData)
Call this method to send a key-value pair to the server. The key is a string of less than 100 bytes, representing the type of data, and score is a type of float that can be used for data statistics and analysis. This method can take a callback.
## payments (pay)
Call the implementation of the payment function, parameters:
```
amount：Recharge amount of RMB
content：the goods for pay, can't be empty，for example "Recharge 500 gold coins"
```
After successful, you can get the PayType data as follows:
```
{
        "id":"79f3662c-a5f4-11e8-9a64-00163e006954", // order number
        "profit":0.857143, // The amount paid, when the type is 0, represents the TCT amount
        "amount":1.8, // The amount of RMB paid
        "type":0 // Payment method, 0 means TCT payment
 }
```
