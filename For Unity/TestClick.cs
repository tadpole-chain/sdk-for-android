using UnityEngine;


public class TestClick : MonoBehaviour {

    public static string str = "----";



    private void Start()
    {
        TCTForUnity.online("Canvas", "BeCallFunc");
    }

    public void Click () {
        Debug.Log("点击了登录按钮");
        int status = TCTForUnity.getStatus();

        str = "status : " + status;
        if (status == TCTForUnity.Status_Initial)
        {

            TCTForUnity.login("Canvas", "BeCallFunc");
        }
        else {


            str = "UserId : " + TCTForUnity.getUserInfo("Id") +
                "\nNickname : " + TCTForUnity.getUserInfo("Nickname") +
                "\nAvatar : " + TCTForUnity.getUserInfo("Avatar");
        }
    }

    public void ClickAd()
    {
        Debug.Log("点击了广告按钮");
        str = "点击了广告按钮";
        TCTForUnity.showAd();
    }

    public void ClickMsg()
    {
        Debug.Log("点击了发公告按钮");
        str = "点击了发公告按钮";
        TCTForUnity.showMsg("这是从Unity发送的公告");
    }

    public void ClickSend()
    {
        Debug.Log("点击了上传分数按钮");
        str = "点击了上传分数按钮";
        TCTForUnity.sendScore(6.5F, "Canvas", "ScoreCallFunc");
    }

    public void ClickData()
    {
        Debug.Log("点击了上传数据按钮");
        str = "点击了上传数据按钮";
        TCTForUnity.sendData("coins", 99, "Canvas", "DataCallFunc");
    }

    private void OnGUI()
    {
        GUI.skin.label.fontSize = 30;
        GUI.Label(new Rect(50, Screen.height / 2 - 100, Screen.width / 2 - 100, 300), str);
    }

    private void BeCallFunc(string content)
    {
		Debug.Log("BeCallFunc");

        if (content.Equals("success"))
        {

            str = "UserId : " + TCTForUnity.getUserInfo("Id") +
                "\nNickname : " + TCTForUnity.getUserInfo("Nickname") +
                "\nAvatar : " + TCTForUnity.getUserInfo("Avatar");
        }
        else
        {
            str = content;// 错误信息
        }
    }

    // 支付的回调方法
    private void PayCallFunc(string content)
    {
		Debug.Log("BeCallFunc");

        if (content.Equals("success"))
        {

            str = "PayType : " + TCTForUnity.getUserInfo("PayType");
        }
        else
        {
            str = content;// 错误信息
        }
    }

    private void ScoreCallFunc(string content)
    {
        if (content.Equals("success"))
        {
            str = "上传分数成功 : 6.5";
        }
        else
        {
            str = content;
        }
    }

    private void DataCallFunc(string content)
    {
        if (content.Equals("success"))
        {
            str = "上传数据成功 : coins,99";
        }
        else
        {
            str = content;
        }
    }
}
