package com.polytech.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.polytech.app.data.model.FormData
import com.polytech.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow(repository.products)
    val products: StateFlow<List<FormData>> = _products

    fun addProduct(product: FormData) {
        repository.addProduct(product)
        _products.update { repository.products }
    }

    fun removeProduct(product: FormData) {
        repository.removeProduct(product)
        _products.value = repository.products
    }

    fun updateProduct(updatedProduct: FormData) {
        repository.updateProduct(updatedProduct)
        _products.value = repository.products
    }
}
