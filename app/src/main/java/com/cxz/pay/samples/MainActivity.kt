package com.cxz.pay.samples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cxz.pay.samples.event.WxPayEvent
import com.cxz.pay.samples.pay.OnPayCallback
import com.cxz.pay.samples.pay.PayFactory
import com.cxz.pay.samples.pay.PayType
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), OnPayCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_ali.setOnClickListener {
            PayFactory.createPay(PayType.ALI)?.pay(this@MainActivity, "", this@MainActivity)
        }

        btn_wx.setOnClickListener {
            PayFactory.createPay(PayType.WX)?.pay(this@MainActivity, "")
        }

    }

    /**
     * 微信支付的回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    fun wxPayResult(event: WxPayEvent) {
        if (event.errorCode == 0) {
            onPaySuccess()
        } else {
            onPayFailed()
        }
    }

    override fun onPaySuccess() {

    }

    override fun onPayFailed() {

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}
