# PaySamples

> 基于工厂模式对微信、支付宝支付进行二次封装。


##### 1、首先封装一个抽象类 BasePay ，封装一个支付的抽象方法 pay

```
abstract class BasePay {
    /**
     * 抽象支付方法
     * @param activity  Activity
     * @param orderStr  调起支付时需要传递的参数
     * @param callback  支付回调
     */
    abstract fun pay(activity: Activity, orderStr: String, callback: OnPayCallback? = null)
}
```

##### 2、定义一个 OnPayCallback 接口，用来回调支付成功和支付失败，该接口可扩展

```
interface OnPayCallback {

    fun onPaySuccess()

    fun onPayFailed()

}
```

##### 3、封装支付宝支付，定义一个类 AliPay ，继承 BasePay 重写 pay 方法，pay 方法里调用支付宝支付

```
class AliPay : BasePay() {
...
    override fun pay(activity: Activity, orderStr: String, callback: OnPayCallback?) {
        doAsync {
            val aliPay = PayTask(activity)
            val result = aliPay.payV2(orderStr, true)
            uiThread {
                val payResult = PayResult(result)
                val resultStatus = payResult.resultStatus
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    callback?.onPaySuccess() // 支付成功回调
                } else {
                    callback?.onPayFailed() // 支付失败回调
                }
            }
        }
    }
}
```

##### 4、封装微信支付，定义一个类 WxPay ，继承 BasePay 重写 pay 方法，pay 方法里调用微信支付

```
class WxPay : BasePay() {
···
    private var mWxApi: IWXAPI? = null
    private val gson = Gson()

    override fun pay(activity: Activity, orderStr: String, callback: OnPayCallback?) {
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
    }
}
```

> 由于微信支付需要类 `WXPayEntryActivity` 的支持，支付成功与否要在类 `WXPayEntryActivity` 中处理，这里采用的是 `EventBus` 处理结果

```
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_KEY);
        api.handleIntent(getIntent(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq req) {
    }
    @Override
    public void onResp(BaseResp resp) {
        Log.d("WxPayEntryActivity", "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            WxPayEvent event = new WxPayEvent(resp.errCode);
            EventBus.getDefault().post(event);
            finish();
        }
    }
}
```

> 处理结果要在 `Activity` 或者 `Fragment` 里接收 `EventBus` 发送的消息

```
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
```

##### 5、创建一个支付工厂类 PayFactory 来封装两种支付方式

```
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
```

> 创建支付方式的枚举类

```
enum class PayType {
    ALI, WX
}
```

##### 6、使用两种支付方式

- 支付宝支付

```
PayFactory.createPay(PayType.ALI)?.pay(this@MainActivity, "", object : OnPayCallback{
                override fun onPaySuccess() {
                }

                override fun onPayFailed() {
                }
            })
```

- 微信支付

```
PayFactory.createPay(PayType.WX)?.pay(this@MainActivity, "")
```

```
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
```