using System;
using UnityEngine;
using System.Runtime.InteropServices;

public class TCTForUnity {

    public const int Status_Unavailable = 0;

    public const int Status_Initial = 1;

    public const int Status_Login = 200;

    public const int Status_Online = 201;

    public const int Status_Offline = 500;

#if UNITY_IOS

    [DllImport("__Internal")]
	private static extern void SDKShop();//广告
    [DllImport("__Internal")]
	private static extern void SDKLogin(string scene,string method);//登陆
    [DllImport("__Internal")]
	private static extern void SDKOnline( string scene,string method);//上线
    [DllImport("__Internal")]
    private static extern void SDKDiversions(string msg);//公告
    [DllImport("__Internal")]
    private static extern void SDKGameScore(float score);//公告
	[DllImport("__Internal")]
	private static extern int SDKGetDate();
	[DllImport("__Internal")]
	private static extern string SDKUserInfo(string key);
	[DllImport("__Internal")]
	private static extern string SDKGameDataKey(string key,float score);


#endif

    public static void online(string scene, string method)
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
                // Android的Java接口
                AndroidJavaClass jc = new AndroidJavaClass("com.tadpolechain.TCT");
                AndroidJavaObject jo = jc.CallStatic<AndroidJavaObject>("instance");

                // 调用方法
                jo.Call("online", scene, method);
            }
            catch (Exception e)
            {
                TestClick.str = e.Message;
            }
        }
        else if (Application.platform == RuntimePlatform.IPhonePlayer)
        {


			SDKOnline(scene, method);


        }
        else
        {
            // 写一些东西
        }
    }

    public static void login(string scene, string method)
    {

		// 写一些东西

        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
			//	Debug.Log("点击了安卓按钮");
                // Android的Java接口
                AndroidJavaClass jc = new AndroidJavaClass("com.tadpolechain.TCT");
                AndroidJavaObject jo = jc.CallStatic<AndroidJavaObject>("instance");

                // 调用方法
                jo.Call("login", scene, method);
            }
            catch (Exception e)
            {
                TestClick.str = e.Message;
            }
        }
		else if (Application.platform == RuntimePlatform.IPhonePlayer)
        {

			//Debug.Log("点击了苹果按钮");

            // 写一些东西
			SDKLogin(scene, method);

        }
        else
        {

        }
    }

	public static int getStatus()
    {
		//Debug.Log("--------%d" + Application.platform );
        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
                // Android的Java接口
                AndroidJavaClass jc = new AndroidJavaClass("com.tadpolechain.TCT");
                AndroidJavaObject jo = jc.CallStatic<AndroidJavaObject>("instance");

                // 调用方法
                int status = jo.Call<int>("getStatus");
                return status;
            }
            catch (Exception e)
            {
                TestClick.str = e.Message;
                return 0;
            }
        }
        else if (Application.platform == RuntimePlatform.IPhonePlayer)
        {


			int status = SDKGetDate();

			Debug.Log("点击了苹getStatus果按钮");

			return status;
        }
        else
        {
            return 0;
        }
    }

    public static string getUserInfo(string infoName)
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
                // Android的Java接口
                AndroidJavaClass jc = new AndroidJavaClass("com.tadpolechain.TCT");
                AndroidJavaObject jo = jc.CallStatic<AndroidJavaObject>("instance");

                // 调用方法
                string ret = jo.Call<string>("get" + infoName);
                return ret;
            }
            catch (Exception e)
            {
                TestClick.str = e.Message;
                return e.Message;
            }
        }
        else if (Application.platform == RuntimePlatform.IPhonePlayer)
        {
			string str = SDKUserInfo (infoName);

			return str;
        }
        else
        {
            return "WinPhone";
        }
    }

    public static void showAd()
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
                // Android的Java接口
                AndroidJavaClass jc = new AndroidJavaClass("com.tadpolechain.TCT");
                AndroidJavaObject jo = jc.CallStatic<AndroidJavaObject>("instance");

                // 调用方法
                jo.Call("showAd");
            }
            catch (Exception e)
            {
                TestClick.str = e.Message;
            }
        }
        else if (Application.platform == RuntimePlatform.IPhonePlayer)
        {
            // 写一些东西
			 SDKShop();
        }
        else
        {
            // 写一些东西
        }
    }

    public static void showMsg(string msg)
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
                // Android的Java接口
                AndroidJavaClass jc = new AndroidJavaClass("com.tadpolechain.TCT");
                AndroidJavaObject jo = jc.CallStatic<AndroidJavaObject>("instance");

                // 调用方法
                jo.Call("showMsg", msg);
            }
            catch (Exception e)
            {
                TestClick.str = e.Message;
            }
        }
        else if (Application.platform == RuntimePlatform.IPhonePlayer)
        {
            // 写一些东西
            SDKDiversions(msg);
        }
        else
        {
            // 写一些东西
        }
    }

    public static void sendScore(float score, string scene, string method)
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
                // Android的Java接口
                AndroidJavaClass jc = new AndroidJavaClass("com.tadpolechain.TCT");
                AndroidJavaObject jo = jc.CallStatic<AndroidJavaObject>("instance");

                // 调用方法
                jo.Call("sendScore", score, scene, method);
            }
            catch (Exception e)
            {
                TestClick.str = e.Message;
            }
        }
        else if (Application.platform == RuntimePlatform.IPhonePlayer)
        {
            // 写一些东西
            SDKGameScore(score);
        }
        else
        {
            // 写一些东西
        }
    }

    public static void sendData(string key, float score, string scene, string method)
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
                // Android的Java接口
                AndroidJavaClass jc = new AndroidJavaClass("com.tadpolechain.TCT");
                AndroidJavaObject jo = jc.CallStatic<AndroidJavaObject>("instance");

                // 调用方法
                jo.Call("sendData", key, score, scene, method);
            }
            catch (Exception e)
            {
                TestClick.str = e.Message;
            }
        }
        else if (Application.platform == RuntimePlatform.IPhonePlayer)
        {
            // 写一些东西
			SDKGameDataKey(key, score);
        }
        else
        {
            // 写一些东西
        }
    }

    public static void pay(float amount, string content, string scene, string method)
    {

		// 写一些东西

        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
			//	Debug.Log("点击了安卓按钮");
                // Android的Java接口
                AndroidJavaClass jc = new AndroidJavaClass("com.tadpolechain.TCT");
                AndroidJavaObject jo = jc.CallStatic<AndroidJavaObject>("instance");

                // 调用方法
                jo.Call("pay", amount, content, scene, method);
            }
            catch (Exception e)
            {
                TestClick.str = e.Message;
            }
        }
		else if (Application.platform == RuntimePlatform.IPhonePlayer)
        {

            // 写一些东西
        }
        else
        {

        }
    }
}
