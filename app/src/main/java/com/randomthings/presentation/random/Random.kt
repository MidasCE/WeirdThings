package com.randomthings.presentation.random

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.randomthings.domain.entity.ImageContent
import com.randomthings.presentation.common.list.EndlessLazyColumn
import com.randomthings.presentation.loader.LoadingView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomScreen(modifier: Modifier = Modifier, viewModel: RandomThingViewModel) {
    val pullRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.refreshData();
            isRefreshing = false
        }
    }

    if (viewModel.randomImages.isEmpty())
    {
        LoadingView(
            modifier = modifier.fillMaxSize(),
            loading = true)
    } else
    {
        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            EndlessLazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                loadMore = { viewModel.fetchRandomContent() }
            ) {
                items(viewModel.randomImages.size) {
                    RandomThingItem(
                        item = viewModel.randomImages[it] as ImageContent.RandomImageContent,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
private fun RandomScreenPreview() {
    val pullRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    val onRefresh: () -> Unit = {
        isRefreshing = true
    }

    val content = ImageContent.RandomImageContent(
        id = "1",
        author = "Test Long Long Long Author",
        width = 512,
        height = 256,
        downloadUrl = "https://fastly.picsum.photos/id/176/"
    )

    PullToRefreshBox(
        state = pullRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(1) {
                RandomThingItem(
                    item = content,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
