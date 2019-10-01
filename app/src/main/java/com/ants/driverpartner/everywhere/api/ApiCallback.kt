import android.util.Log

import retrofit2.adapter.rxjava.HttpException
import rx.Subscriber

abstract class ApiCallback<M> : Subscriber<M>() {

    abstract fun onSuccess(model: M)

    abstract fun onFailure(msg: String?)

    abstract fun onFinish()


    override fun onError(e: Throwable) {
        e.printStackTrace()
        if (e is HttpException) {
//httpException.response().errorBody().string()
            val code = e.code()
            var msg: String? = e.message
            Log.d("code=", "" + code)
            if (code == 504) {
                msg = "Network error with code 504"
            }
            if (code == 502 || code == 404) {
                msg = "The server is abnormal. Please try again later"
            }
            onFailure(msg)
        } else {
            onFailure(e.message)
        }
        onFinish()
    }

    override fun onNext(model: M) {
        //        try{
        //            Log.e("response","- "+model.toString());
        //        }catch (Exception e){
        //            e.printStackTrace();
        //        }
        onSuccess(model)
    }

    override fun onCompleted() {
        onFinish()
    }
}
