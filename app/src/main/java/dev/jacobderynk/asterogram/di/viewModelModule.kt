package dev.jacobderynk.asterogram.di

import dev.jacobderynk.asterogram.ui.MainActivityViewModel
import dev.jacobderynk.asterogram.ui.home.HomeViewModel
import dev.jacobderynk.asterogram.ui.profile.ProfileScreen
import dev.jacobderynk.asterogram.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { MainActivityViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
}