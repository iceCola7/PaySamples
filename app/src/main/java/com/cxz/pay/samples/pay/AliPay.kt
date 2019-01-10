package com.cxz.pay.samples.pay

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.alipay.sdk.app.PayTask
import com.cxz.pay.samples.bean.PayResult
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * @author chenxz
 * @date 2019/1/10
 * @desc 支付宝支付
 */
class AliPay : BasePay() {

    private var mActivity: Activity? = null
    private var mCallback: OnPayCallback? = null

    companion object {
        fun getInstance(): AliPay {
            return AliPay()
        }
    }

    override fun pay(activity: Activity, orderStr: String, callback: OnPayCallback?) {
        mActivity = activity
        mCallback = callback

        doAsync {
            val aliPay = PayTask(activity)
            val result = aliPay.payV2(orderStr, true)
            uiThread {
                val payResult = PayResult(result)
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                val resultInfo = payResult.result// 同步返回需要验证的信息
                val resultStatus = payResult.resultStatus
                Log.d("AliPay", "PayResult::$payResult")
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    callback?.onPaySuccess()
                } else {
                    callback?.onPayFailed()
                }
            }
        }
    }

}