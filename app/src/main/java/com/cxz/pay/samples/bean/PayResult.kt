package com.cxz.pay.samples.bean

/**
 * @author chenxz
 * @date 2019/1/10
 * @desc 支付宝支付结果的实体类
 */
class PayResult {

    var resultStatus = ""
    var result = ""
    var memo = ""

    constructor(rawResult: Map<String, String>?) {
        if (rawResult == null) return
        rawResult.forEach {
            when {
                it.key == "resultStatus" -> resultStatus = it.value
                it.key == "result" -> result = it.value
                it.key == "memo" -> memo = it.value
            }
        }
    }

    override fun toString(): String {
        return "PayResult(resultStatus='$resultStatus', result='$result', memo='$memo')"
    }

}