package com.candra.submissiononeintermediate.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.activity.DetailActivity
import com.candra.submissiononeintermediate.databinding.ItemListBinding
import com.candra.submissiononeintermediate.helper.`object`.Contant
import com.candra.submissiononeintermediate.helper.`object`.Contant.POSITION
import com.candra.submissiononeintermediate.helper.convertLatLngToAddressForAdapter
import com.candra.submissiononeintermediate.helper.genereteDate
import com.candra.submissiononeintermediate.room.entity.Story

class ListStoryAdapter: PagingDataAdapter<Story,ListStoryAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryAdapter.ViewHolder {
        return ViewHolder(
            ItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.ViewHolder, position: Int) {
        val data = getItem(position)
        data?.let { holder.bind(it) }
    }


   inner class ViewHolder(private val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: Story) {
            with(binding){
                gambar.load(data.photoUrl){
                    crossfade(true)
                    crossfade(600)
                    error(R.drawable.ic_baseline_broken_image_24)
                    placeholder(R.drawable.ic_baseline_image_24)
                    transformations(CircleCropTransformation())
                }
                name.text = data.name
                date.text = data.createdAt.genereteDate
                location.text = convertLatLngToAddressForAdapter(data.lat,data.lon,itemView.context)
                cvList.setOnClickListener {
                   val optionSelected: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                       itemView.context as Activity,
                       Pair(gambar,"gambar"),
                       Pair(name,"name"),
                       Pair(date,"date")
                   )
                    itemView.context.startActivity(
                        Intent(itemView.context,DetailActivity::class.java).apply {
                            putExtra(Contant.EXTRA_DATA,data)
                            putExtra(POSITION,1)
                        },
                        optionSelected.toBundle()
                    )
                }
            }
        }

    }

    companion object{
        val DIFF_CALLBACK = object:
        DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean = oldItem.id == newItem.id

        }
    }
}