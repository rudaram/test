package kr.co.htns.di

import kr.co.htns.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    //activity
    viewModel { MainViewModel(get(),get()) }

}