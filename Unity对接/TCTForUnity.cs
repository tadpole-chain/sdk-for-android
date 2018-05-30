using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TCTForUnity {

    public const int Status_Unavailable = 0;

    public const int Status_Initial = 1;

    public const int Status_Login = 200;

    public const int Status_Online = 201;

    public const int Status_Offline = 500;

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
            // 写一些东西
        }
        else
        {
            // 写一些东西
        }
    }

    public static void login(string scene, string method)
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            try
            {
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
            // 写一些东西
        }
        else
        {
            // 写一些东西
        }
    }

    public static int getStatus()
    {
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
            return 0;
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
            return "IPhone";
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
        }
        else
        {
            // 写一些东西
        }
    }
}
