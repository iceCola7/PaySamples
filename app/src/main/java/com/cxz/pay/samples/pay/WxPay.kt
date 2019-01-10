package com.cxz.pay.samples.pay

import android.app.Activity
import com.cxz.pay.samples.Constants
import com.cxz.pay.samples.bean.CreatePayBean
import com.google.gson.Gson
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * @author chenxz
 * @date 2019/1/10
 * @desc 微信支付
 */
class WxPay : BasePay() {

    private var mActivity: Activity? = null
    private var mCallback: OnPayCallback? = null

    private var mWxApi: IWXAPI? = null
    private val gson = Gson()

    companion object {
        fun getInstance(): WxPay {
            return WxPay()
        }
    }

    override fun pay(activity: Activity, orderStr: String, callback: OnPayCallback?) {
        mActivity = activity
        mCallback = callback

        mWxApi = WXAPIFactory.createWXAPI(mActivity, Constants.WX_APP_KEY)

        val payInfo = gson.fromJson<CreatePayBean>(orderStr, CreatePayBean::class.java)
        val req = PayReq()
        req.appId = payInfo?.appid
        req.partnerId = payInfo?.partnerid
        req.prepayId = payInfo?.prepayid
        req.nonceStr = payInfo?.noncestr
        req.packageValue = payInfo?.packageValue
        req.timeStamp = payInfo?.timestamp
        req.sign = payInfo?.sign
        mWxApi?.sendReq(req)

        /**
         * 微信的支付回调要在 Activity 使用 EventBus 接收消息，这里不做处理，例如下面的这段代码
         */
        /**
        @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
        fun wxPayResult(event: WxPayEvent) {
        if (event.errorCode == 0) {
        paySuccess()
        } else {
        payFail()
        }
        }
         */
    }

}