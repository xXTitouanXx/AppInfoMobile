package com.polytech.app.data.repository

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.polytech.app.data.model.FormData

class ProductRepository {
    private val _products = mutableStateListOf<FormData>()
    val products: List<FormData> get() = _products


    fun addProduct(product: FormData) {
        _products.add(product)
        Log.d("ProductRepository", "Produit ajouté: $product")
    }

    fun removeProduct(product: FormData) {
        _products.remove(product)
    }

    fun updateProduct(updatedProduct: FormData) {
        val index = _products.indexOfFirst { it.id == updatedProduct.id }
        if (index != -1) {
            _products[index] = updatedProduct
        }
    }
}
