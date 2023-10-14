package com.example.infinite.loop.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infinite.loop.carousel.databinding.ItemCarouselBinding

class MainAdapter : ListAdapter<CarouselModel, MainViewHolder>(
	object : DiffUtil.ItemCallback<CarouselModel>() {
		override fun areItemsTheSame(oldItem: CarouselModel, newItem: CarouselModel): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: CarouselModel, newItem: CarouselModel): Boolean {
			return oldItem == newItem
		}
	}
) {

	override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
		return MainViewHolder.create(parent)
	}
}

class MainViewHolder(
	private val binding: ItemCarouselBinding,
): RecyclerView.ViewHolder(binding.root) {

	fun bind(model: CarouselModel) {
		binding.text.text = "Item ${model.id}"
	}

	companion object {
		fun create(parent: ViewGroup): MainViewHolder {
			return MainViewHolder(
				ItemCarouselBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false,
				)
			)
		}
	}
}