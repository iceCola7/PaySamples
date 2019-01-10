package com.cxz.pay.samples.pay

/**
 * @author chenxz
 * @date 2019/1/10
 * @desc 支付工厂类
 */
object PayFactory {

    fun createPay(payType: PayType): BasePay? {
        return when (payType) {
            PayType.ALI -> {
                AliPay.getInstance()
            }
            PayType.WX -> {
                WxPay.getInstance()
            }
        }
    }

}

