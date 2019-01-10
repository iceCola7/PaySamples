package com.cxz.pay.samples.pay

import android.app.Activity

/**
 * @author chenxz
 * @date 2019/1/10
 * @desc 支付抽象类
 */
abstract class BasePay {

    /**
     * 抽象支付方法
     * @param activity  Activity
     * @param orderStr  调起支付时需要传递的参数
     * @param callback  支付回调
     */
    abstract fun pay(activity: Activity, orderStr: String, callback: OnPayCallback? = null)

}