package com.candra.submissiononeintermediate.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.candra.submissiononeintermediate.databinding.StoryLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit): LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    override fun onBindViewHolder(
        holder: LoadingStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        return LoadingStateViewHolder(
            StoryLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false),retry
        )
    }

    class LoadingStateViewHolder(private val binding: StoryLoadingBinding, retry: () -> Unit):
        RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState){
            with(binding){
                if (loadState is LoadState.Error){
                    errorMessage.text = loadState.error.localizedMessage
                }
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                errorMessage.isVisible = loadState is LoadState.Error
            }
        }
    }
}