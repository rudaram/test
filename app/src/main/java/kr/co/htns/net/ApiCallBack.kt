package kr.co.htns.net


interface ApiCallBack<T> {
    fun onSuccess(res: T?)
    fun onError(err: Throwable)
}