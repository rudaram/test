package kr.co.htns.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.kr.simi_mvvm_master.util.SnackbarMessage
import com.kr.simi_mvvm_master.util.SnackbarMessageString
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/*
* @author Eunseong Kim
* @email eskim@slsolution.co.kr
* @create 2022-11-29
* @desc
*/

open class BaseViewModel : ViewModel()  {
    private var mCallBack: BaseCallBack? = null

    // 일회성 이벤트를 만들어 내는 라이브 이벤트
    // 뷰는 이러한 이벤트를 바인딩하고 있다가, 적절한 상황이 되면 액티비티를 호출하거나 스낵바를 만듬
    private val snackbarMessage = SnackbarMessage()
    private val snackbarMessageString = SnackbarMessageString()

    /**
     * RxJava 의 observing을 위한 부분.
     * addDisposable을 이용하여 추가하기만 하면 된다
     */
    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    /**
     * 스낵바를 보여주고 싶으면 viewModel 에서 이 함수를 호출
     */
    fun showSnackbar(stringResourceId: Int) {
        snackbarMessage.value = stringResourceId
    }

    fun showSnackbar(str: String) {
        snackbarMessageString.value = str
    }

    /**
     * BaseKotlinActivity 에서 쓰는 함수
     */
    fun observeSnackbarMessage(lifeCycleOwner: LifecycleOwner, ob: (Int) -> Unit) {
        snackbarMessage.observe(lifeCycleOwner, ob)
    }

    fun observeSnackbarMessageStr(lifeCycleOwner: LifecycleOwner, ob: (String) -> Unit) {
        snackbarMessageString.observe(lifeCycleOwner, ob)
    }



    /**
     * callback
     */
    open fun getCallBack(): BaseCallBack? {
        return mCallBack
    }

    open fun setCallBack(callback: BaseCallBack) {
        this.mCallBack = callback
    }
}
