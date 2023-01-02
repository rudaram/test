package com.kr.simi_mvvm_master.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * A SingleLiveEvent used for Snackbar messages. Like a [SingleLiveEvent] but also prevents
 * null messages and uses a custom observer.
 *
 *
 * Note that only one observer is going to be notified of changes.
 */
class SnackbarMessage : SingleLiveEvent<Int>() {

    fun observe(owner: LifecycleOwner, observer: (Int) -> Unit) {
        super.observe(owner, Observer {
            it?.run{
                observer(it)
            }
        })
    }

}

class SnackbarMessageString:SingleLiveEvent<String>(){
    fun observe(owner: LifecycleOwner, observer: (String) -> Unit) {
        super.observe(owner, Observer {
            it?.run{
                observer(it)
            }
        })
    }
}