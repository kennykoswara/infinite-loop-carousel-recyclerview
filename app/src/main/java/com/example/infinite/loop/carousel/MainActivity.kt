package com.example.infinite.loop.carousel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnFlingListener
import com.example.infinite.loop.carousel.databinding.ActivityMainBinding
import kotlin.math.abs
import kotlin.math.sign

class MainActivity : ComponentActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val adapter by lazy { MainAdapter() }

    private val itemDecoration = CarouselItemDecoration()

    private val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (adapter.itemCount == 0) return

            val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            if (firstVisiblePosition == RecyclerView.NO_POSITION) return
            val firstVisibleView = recyclerView.findViewHolderForAdapterPosition(firstVisiblePosition)?.itemView ?: return

            val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
            if (lastVisiblePosition == RecyclerView.NO_POSITION) return
            val lastVisibleView = recyclerView.findViewHolderForAdapterPosition(lastVisiblePosition)?.itemView ?: return

            val realItemSize = adapter.itemCount - (2 * FAKE_COUNT_PER_SIDE)
            if (firstVisiblePosition < FAKE_COUNT_PER_SIDE) {
                val stepToOriginalStart = FAKE_COUNT_PER_SIDE - firstVisiblePosition
                val modulus = stepToOriginalStart % realItemSize
                val substractBy = if (modulus == 0) realItemSize else modulus
                val stepToCorrectIndex = stepToOriginalStart + realItemSize - substractBy

                recyclerView.scrollBy(stepToCorrectIndex * (firstVisibleView.width + itemDecoration.getHorizontalOffset()), 0)
            } else if (lastVisiblePosition >= FAKE_COUNT_PER_SIDE + realItemSize) {
                val stepToOriginalEnd = lastVisiblePosition - FAKE_COUNT_PER_SIDE - realItemSize + 1 // e.g pos 14
                val modulus = stepToOriginalEnd % realItemSize
                val addBy = if (modulus == 0) realItemSize else modulus
                val stepToCorrectIndex = (-1 * stepToOriginalEnd) - realItemSize + addBy

                recyclerView.scrollBy(stepToCorrectIndex * (lastVisibleView.width + itemDecoration.getHorizontalOffset()), 0)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvCarousel.layoutManager = layoutManager
        binding.rvCarousel.adapter = adapter
        binding.rvCarousel.addItemDecoration(itemDecoration)
        binding.rvCarousel.addOnScrollListener(scrollListener)
        binding.rvCarousel.onFlingListener = object : OnFlingListener() {
            private val MAX_VELOCITY_X = 7500
            override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                if (abs(velocityX) > MAX_VELOCITY_X) {
                    val newVelocityX = MAX_VELOCITY_X * sign(velocityX.toDouble()).toInt()
                    binding.rvCarousel.fling(newVelocityX, velocityY)
                    return true
                }

                return false
            }
        }

        val modelList = List(5) {
            CarouselModel(it.toString())
        }

        val finalModifiedList = addFakes(modelList)

        adapter.submitList(finalModifiedList) {
            binding.rvCarousel.scrollToPosition(FAKE_COUNT_PER_SIDE)
            binding.rvCarousel.post {
                val firstItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val view = binding.rvCarousel.findViewHolderForAdapterPosition(firstItem)?.itemView ?: return@post

                val scrollBy = ((2 * view.x + view.width) / 2 - binding.rvCarousel.width / 2).toInt()

                binding.rvCarousel.scrollBy(scrollBy, 0)
            }
        }
    }

    private fun addFakes(currentList: List<CarouselModel>): List<CarouselModel> {
        return buildList {
            var leftFakeCount = FAKE_COUNT_PER_SIDE
            do {
                val takeCount = kotlin.math.min(leftFakeCount, currentList.size)
                addAll(
                    0,
                    currentList.takeLast(takeCount).map {
                        it.copy(id = "${it.id} (Cloned)")
                    }
                )
                leftFakeCount -= takeCount
            } while (leftFakeCount > 0)

            addAll(currentList)

            var rightFakeCount = FAKE_COUNT_PER_SIDE
            do {
                val takeCount = kotlin.math.min(rightFakeCount, currentList.size)
                addAll(
                    currentList.take(takeCount).map {
                        it.copy(id = "${it.id} (Cloned)")
                    }
                )
                rightFakeCount -= takeCount
            } while (rightFakeCount > 0)
        }
    }

    companion object {
        private const val FAKE_COUNT_PER_SIDE = 5
    }
}