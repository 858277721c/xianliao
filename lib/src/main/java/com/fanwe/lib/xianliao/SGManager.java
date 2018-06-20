package com.fanwe.lib.xianliao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import org.xianliao.im.sdk.api.ISGAPI;
import org.xianliao.im.sdk.api.SGAPIFactory;
import org.xianliao.im.sdk.constants.SGConstants;
import org.xianliao.im.sdk.modelmsg.SGImageObject;
import org.xianliao.im.sdk.modelmsg.SGMediaMessage;
import org.xianliao.im.sdk.modelmsg.SendMessageToSG;

public class SGManager
{
    private static SGManager sInstance;

    private Context mContext;
    private String mAppId;

    private ISGAPI mSGAPI;

    private SGManager()
    {
    }

    public static SGManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (SGManager.class)
            {
                if (sInstance == null)
                    sInstance = new SGManager();
            }
        }
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context)
    {
        if (mSGAPI != null)
            return;

        if (context == null)
            throw new NullPointerException("context is null");

        mContext = context.getApplicationContext();
        mAppId = context.getResources().getString(R.string.sg_appid);
        if (TextUtils.isEmpty(mAppId))
            throw new NullPointerException("sg_appid is not specified in string.xml");

        mSGAPI = SGAPIFactory.createSGAPI(context, mAppId);
    }

    public ISGAPI getSGAPI()
    {
        checkInit();
        return mSGAPI;
    }

    private void checkInit()
    {
        if (mContext == null)
            throw new NullPointerException("you must invoke SGManager.getInstance().init(Activity) before this");
    }

    /**
     * 分享图片
     *
     * @param bytes 二进制数据
     * @return
     */
    public boolean shareImage(byte[] bytes)
    {
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return shareImage(bitmap);
    }

    /**
     * 分享图片
     *
     * @param localPath 本地图片路径
     * @return
     */
    public boolean shareImage(String localPath)
    {
        final Bitmap bitmap = BitmapFactory.decodeFile(localPath);
        return shareImage(bitmap);
    }

    /**
     * 分享图片
     *
     * @param bitmap
     * @return
     */
    public boolean shareImage(Bitmap bitmap)
    {
        if (bitmap == null)
            return false;

        //初始化一个SGImageObject对象，设置所分享的图片内容
        SGImageObject imageObject = new SGImageObject(bitmap);

        //用SGImageObject对象初始化一个SGMediaMessage对象
        SGMediaMessage msg = new SGMediaMessage();
        msg.mediaObject = imageObject;

        //构造一个Req
        SendMessageToSG.Req req = new SendMessageToSG.Req();
        req.transaction = SGConstants.T_IMAGE;
        req.mediaMessage = msg;
        req.scene = SendMessageToSG.Req.SGSceneSession; //代表分享到会话列表

        //调用api接口发送数据到闲聊
        return getSGAPI().sendReq(req);
    }
}