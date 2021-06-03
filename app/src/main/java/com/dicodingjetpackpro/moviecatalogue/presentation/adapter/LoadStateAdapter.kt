/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dicodingjetpackpro.moviecatalogue.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicodingjetpackpro.moviecatalogue.databinding.LoadStateItemViewBinding
import com.dicodingjetpackpro.moviecatalogue.presentation.adapter.LoadStateAdapter.ViewHolder

class LoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder.createFrom(parent, retry)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class ViewHolder(
        private val binding: LoadStateItemViewBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                val errorMessage =
                    if (loadState is LoadState.Error) loadState.error.localizedMessage else ""
                tvErrorMsg.text = errorMessage
                val stateIsError = loadState is LoadState.Error
                btnRetryPaging.isVisible = stateIsError
                tvErrorMsg.isVisible = stateIsError
                btnRetryPaging.setOnClickListener { retry.invoke() }

                executePendingBindings()
            }
        }

        companion object {

            fun createFrom(parent: ViewGroup, retry: () -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LoadStateItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, retry)
            }
        }
    }
}
