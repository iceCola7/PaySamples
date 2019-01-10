package com.cxz.pay.samples.bean

import com.google.gson.annotations.SerializedName

/**
 * @author chenxz
 * @date 2019/1/10
 * @desc 创建支付时的实体类
 */
data class CreatePayBean(
        val appid: String?,
        val partnerid: String?,
        val device_info: String?,
        @SerializedName("package")
        val packageValue: String?,
        val noncestr: String?,
        val sign: String?,
        val trade_type: String?,
        val prepayid: String?,
        val aliKey: String?,
        val timestamp: String?,
        val payorderno: String?
)