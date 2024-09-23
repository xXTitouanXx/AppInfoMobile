package com.polytech.app.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.polytech.app.viewmodel.MainViewModel

@Composable
fun PostList(viewModel: MainViewModel) {
    val posts = viewModel.posts.value
    LazyColumn {
        items(posts) { post ->
            BasicText(text = post.title)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getPosts()
    }
}
