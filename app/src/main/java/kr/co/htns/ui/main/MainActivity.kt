package kr.co.htns.ui.main
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.htns.R
import kr.co.htns.base.BaseActivity
import kr.co.htns.databinding.ActivityMainBinding
import kr.co.htns.di.ViewModelModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class  MainActivity: BaseActivity<ActivityMainBinding, MainViewModel>(){
    override val resId: Int = kr.co.htns.R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    override fun initView() {

    }

    override fun initBinding() {

    }


}