package com.fanwe.lib.xianliao.sgapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fanwe.lib.xianliao.SGManager;

import org.xianliao.im.sdk.api.ISGAPI;
import org.xianliao.im.sdk.api.ISGAPIEventHandler;
import org.xianliao.im.sdk.constants.SGConstants;
import org.xianliao.im.sdk.modelbase.BaseReq;
import org.xianliao.im.sdk.modelbase.BaseResp;
import org.xianliao.im.sdk.modelmsg.InvitationResp;
import org.xianliao.im.sdk.modelmsg.SendAuth;


/**
 * Created by nickyang on 2017/1/18.
 * <p>
 * 此类用于接收从闲聊返回到应用的返回值
 * <p>
 * 注意： "sgapi" 目录名和 "SGEntryActivity" 类名都不能改动
 */
public class SGEntryActivity extends Activity implements ISGAPIEventHandler
{
    private final ISGAPI mApi = SGManager.getInstance().getSGAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req)
    {

    }

    @Override
    public void onResp(BaseResp resp)
    {
        switch (resp.getType())
        {
            case SGConstants.COMMAND_AUTH:  //授权登陆
                SendAuth.Resp respAuth = (SendAuth.Resp) resp;
                if (resp.errCode == SGConstants.ERR_OK)
                {
                    Toast.makeText(this, "授权登录成功！" + resp.errCode + "\ncode: " + respAuth.code, Toast.LENGTH_SHORT).show();
                } else if (resp.errCode == SGConstants.ERR_CANCEL)
                {
                    Toast.makeText(this, "授权登录取消！" + resp.errCode, Toast.LENGTH_SHORT).show();
                } else if (resp.errCode == SGConstants.ERR_FAIL)
                {
                    Toast.makeText(this, "授权登录失败！" + resp.errCode, Toast.LENGTH_SHORT).show();
                }
                break;

            case SGConstants.COMMAND_SHARE:  //分享文本，图片，邀请
                if (resp.errCode == SGConstants.ERR_OK)
                {
                    Toast.makeText(this, "分享成功！" + resp.errCode, Toast.LENGTH_SHORT).show();
                } else if (resp.errCode == SGConstants.ERR_CANCEL)
                {
                    Toast.makeText(this, "分享取消！" + resp.errCode, Toast.LENGTH_SHORT).show();
                } else if (resp.errCode == SGConstants.ERR_FAIL)
                {
                    Toast.makeText(this, "分享失败！" + resp.errCode, Toast.LENGTH_SHORT).show();
                }
                break;

            case SGConstants.COMMAND_INVITE:  //从闲聊点击邀请进入应用,
                /**
                 * 需要Manifest里面配置特殊 intent-filter 才有用,详情参见AndroidManifest
                 */
                InvitationResp invitationResp = (InvitationResp) resp;
                Toast.makeText(this, "邀请进入: roomId: " + invitationResp.roomId + " roomToken: "
                        + invitationResp.roomToken, Toast.LENGTH_LONG).show();

                //传递roomId roomToken到其他页面
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("roomId", invitationResp.roomId);
                intent.putExtra("roomToken", invitationResp.roomToken);
                intent.putExtra("openId", invitationResp.openId);
//                startActivity(intent);
                break;
        }
        finish();
    }
}
