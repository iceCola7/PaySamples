package com.cxz.pay.samples.pay

/**
 * @author chenxz
 * @date 2019/1/10
 * @desc 支付回调接口
 */
interface OnPayCallback {

    fun onPaySuccess()

    fun onPayFailed()

}