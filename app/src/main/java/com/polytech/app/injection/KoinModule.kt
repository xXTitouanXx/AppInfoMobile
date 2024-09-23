package com.polytech.app.injection

import com.polytech.app.data.repository.ProductRepository
import com.polytech.app.ui.viewmodel.ProductViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ProductRepository() }
    viewModel { ProductViewModel(get()) }
}
