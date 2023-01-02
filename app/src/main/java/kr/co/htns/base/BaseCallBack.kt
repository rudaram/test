package kr.co.htns.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent

/*
* @author Eunseong Kim
* @email eskim@slsolution.co.kr
* @create 2022-11-29
* @desc
*/
interface BaseCallBack {
    fun openDialog(msg: String, pOnclickListener: DialogInterface.OnClickListener?, nOnclickListener: DialogInterface.OnClickListener?, isCancelable: Boolean)
    fun openDialog(stringResourceId: Int, pOnclickListener: DialogInterface.OnClickListener?, nOnclickListener: DialogInterface.OnClickListener?, isCancelable: Boolean)
    fun hideDialog()
    fun showLoading(cancelable: Boolean)
    fun hideLoading()
    fun finishActivity()
    fun finishActivity(resultCode: Int, data: Intent)
    fun openActivity(intent: Intent)
    fun openFinishActivity(intent: Intent)
    fun getContext(): Context
}