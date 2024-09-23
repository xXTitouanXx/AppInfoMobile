package com.polytech.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.polytech.app.data.model.Post
import com.polytech.app.data.model.Accommodation
import com.polytech.app.data.repository.RetrofitInstance

class MainViewModel : ViewModel() {
    private val apiService = RetrofitInstance.api
    private val _posts = mutableStateOf<List<Post>>(emptyList())
    val posts: State<List<Post>> = _posts
    private val _accommodations = mutableStateOf<List<Accommodation>>(emptyList())
    val accommodations: State<List<Accommodation>> = _accommodations

    fun getAccommodations() {
        viewModelScope.launch {
            try {
                val response = apiService.getAccommodations()
                _accommodations.value = response.results
            } catch (e: Exception) {
                // Gérer les erreurs ici
            }
        }
    }

    fun getPosts() {
        viewModelScope.launch {
            try {
                val response = apiService.getPosts()
                _posts.value = response
            } catch (e: Exception) {
                // Gérer les erreurs ici
            }
        }
    }
}
