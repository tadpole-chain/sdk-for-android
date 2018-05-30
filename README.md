sdk-for-android
====

����˵��
----

# 1.������������Ŀ
��Unity����Android��Ŀ��A����������һ�ݣ�B������ΪAndroid Studio�༭����Ŀ��֮��ÿ�ε�������A��Ȼ��A��Ŀ��src\main\assets\bin�е��ļ���������ĿB����Ӧ��λ�ã���Ϊÿ�ε�������ֻ����һ�������ݻᷢ���仯����
# 2.�ϲ���Դ�ļ�
��res�е��ļ�ȫ�����Ƶ���Ŀ��src\main\res�У�����г�ͻ����ֱ�Ӹ��ǡ���Դ�ļ�������������Ӱ����Ϸ���С�
# 3.����SDK
���ļ�tct_v1.0.2.jar��������Ŀlibs�ļ����¡�<br>
����Ŀ�е�AndroidManifest.xml�ļ��м������Ȩ�ޣ�<br>
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

��app�е�AndroidManifest.xml�ļ��м���SDK�ĵ�¼����������<br>
```xml
<activity android:name="com.tadpolechain.LoginActivity"
    android:theme="@style/LoginDialog" />
```

# 4.�ϲ�activity
��UnityPlayerActivity�ļ�����Ŀ�е�UnityPlayerActivity�ļ��ϲ����滻���˰���������һ�е�package��������������ݣ�����onCreate�������app key��secret key�滻Ϊ�Լ��ġ�
# 5.�޸İ汾�ź���Ϸ����
�����Ҫ�޸İ汾�ţ����AndroidManifest.xml�ļ����޸��������ݣ�<br>
![](http://img.suncity.ink/github/2018/05/git_0001.png) 
����޸���Ϸ���ƣ�����ļ�src\main\res\values\strings.xml���޸�app_name:<br>
![](http://img.suncity.ink/github/2018/05/git_0002.png) 
# 6.����Unity��װ�ļ�
��TCTForUnity.cs��������Ŀ��ӦĿ¼��TCTForUnity�еķ����Ѿ��Խӵ�SDK������ֱ�ӵ��á����÷�����TestClick.cs�ļ���ʾ��
# 7.�ص�����
����SDK�����ĵ�����Ҫ�ص��������¼���ܣ�TestClick.cs�еĵ��÷���������ʾ��<br>
![](http://img.suncity.ink/github/2018/05/git_0003.png)
��ĿĿ¼���£�<br>
![](http://img.suncity.ink/github/2018/05/git_0004.png)
��һ������Canvas������ǽű������صĳ������ƣ��ڶ�������������ǻص��������ơ�
# 8. ��װ�������
## ��¼����(login)
���ü��ɵ�¼��������ӻص��������ص������Ĳ�����һ���ַ����������¼�ɹ��򷵻�success��Ȼ����Ի���û���Ϣ�������¼ʧ�ܣ��򷵻�ʧ�ܵ�ԭ��
## ���߷���(online)
���֮ǰ��¼����Ϸ���ٴδ���Ϸ���Զ����ߣ��˷��������ڴ��ݻص�������
## ���SDK״̬(getStatus)
��¼״̬������ֵ��<br>
```table
TCTForUnity.Status_Unavailable	0	
TCTForUnity.Status_Initial	1	
TCTForUnity.Status_Login	201	�Ѿ���¼
TCTForUnity.Status_Online	200	�Ѿ�����
TCTForUnity.Status_Offline	500	�Ѿ�����
```

## ����û���Ϣ(getUserInfo)
���Ի���ַ������͵��û���Ϣ����ʱ��֧�����²�����Id��Nickname��Avatar��
## ��ʾ���(showAd)
���ÿ�����ʾ�����Ϣ��
## ���͹���(showMsg)
���ô˷���������һ���ַ������������ʾ�����������ƹ�������
## ���͵÷�(sendScore)
���ô˷��������������������һ��float�͵ķ������û����а����򡣴˷������Դ��ص���
## ������������(sendData)
���ô˷��������������������һ����ֵ�ԡ�����key��һ��С��100�ֽڵ��ַ������������������࣬score��һ��float�͵ķ�����������������ͳ�ƺͷ������˷������Դ��ص���

