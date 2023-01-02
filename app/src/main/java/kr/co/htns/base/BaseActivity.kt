package kr.co.htns.base

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import kr.co.htns.R
import com.google.android.material.snackbar.Snackbar
import kr.co.htns.util.LoadingDialog

/*
* @author Eunseong Kim
* @email eskim@slsolution.co.kr
* @create 2022-11-29
* @desc
*/

abstract class BaseActivity<B : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(), BaseCallBack{
    /**
     * data binding 로 쓰일 변수.
     */
    lateinit var binding: B

    /**
     * setContentView로 호출할 Layout의 리소스 Id.
     * ex) R.layout.activity_sbs_main
     */
    abstract val resId: Int

    /**
     * viewModel 로 쓰일 변수.
     */
    abstract val viewModel: V

    /**
     * Dialog
     */
    private var alertDialog:AlertDialog? = null


    abstract fun initView()
    abstract fun initBinding()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //테스트 커밋
        Log.d("보리","뇸뇸")
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    fun showSnackbar(stringResourceId: Int) {
        val view: Snackbar =   Snackbar.make(findViewById(android.R.id.content), stringResourceId, Snackbar.LENGTH_LONG)
        view.setTextColor(ContextCompat.getColor(getContext(), R.color.white))
        view.show()
        Log.d("보리","뇸뇸")
    }

    fun showSnackbar(str: String) {
        val view: Snackbar =   Snackbar.make(findViewById(android.R.id.content), str, Snackbar.LENGTH_LONG)
        view.setTextColor(ContextCompat.getColor(getContext(), R.color.white))
        view.show()
    }

    /**
     * dialog
     */
    override fun openDialog(msg: String, pOnclickListener: DialogInterface.OnClickListener?, nOnclickListener: DialogInterface.OnClickListener?, isCancelable: Boolean){
        if(alertDialog != null && alertDialog!!.isShowing){
            cancelDialog()
        }

        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)

        builder.setPositiveButton("확인", pOnclickListener)

        nOnclickListener?.let{
            builder.setNegativeButton("취소", nOnclickListener)
        }

        builder.setCancelable(isCancelable)

        alertDialog = builder.create()
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    override fun openDialog(
        stringResourceId: Int,
        pOnclickListener: DialogInterface.OnClickListener?,
        nOnclickListener: DialogInterface.OnClickListener?, isCancelable: Boolean
    ) {
        val msg = getContext().resources.getString(stringResourceId)
        openDialog(msg, pOnclickListener, nOnclickListener,isCancelable)
    }

    override fun hideDialog(){
        alertDialog?.let {
            it.hide()
        }
    }

    fun cancelDialog(){
        alertDialog?.let {
            it.cancel()
            alertDialog = null
        }
    }

    /**
     * loding bar
     */
    override fun showLoading(cancelable: Boolean) {
        hideLoading()
        LoadingDialog.displayLoadingWithText(this, resources.getString(R.string.network_wait),cancelable)
    }
    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun getContext(): Context {
        return binding.root.context
    }

    override fun finishActivity() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun finishActivity(resultCode: Int) {
        setResult(resultCode)
        finishActivity()
    }

    override fun finishActivity(resultCode: Int, data: Intent) {
        setResult(resultCode, data)
        finishActivity()
    }

    override fun openActivity(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun openFinishActivity(intent: Intent) {
        openActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}