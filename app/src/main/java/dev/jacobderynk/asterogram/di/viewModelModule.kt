package dev.jacobderynk.asterogram.di

import dev.jacobderynk.asterogram.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { HomeViewModel(get()) }
}