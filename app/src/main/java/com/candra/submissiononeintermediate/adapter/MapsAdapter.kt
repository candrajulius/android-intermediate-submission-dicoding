package com.candra.submissiononeintermediate.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.activity.DetailActivity
import com.candra.submissiononeintermediate.databinding.ItemListMapsBinding
import com.candra.submissiononeintermediate.helper.`object`.Contant
import com.candra.submissiononeintermediate.helper.`object`.Contant.POSITION
import com.candra.submissiononeintermediate.helper.genereteDate
import com.candra.submissiononeintermediate.model.local.MapStory

class MapsAdapter(
    private val context: Context,
    private val onItemClicked: (position: Int) -> Unit,
    private val mapsStories: List<MapStory>
): RecyclerView.Adapter<MapsAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemListMapsBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(context: Context, mapStory: MapStory, onItemClicked: (position: Int) -> Unit, position: Int) {
            with(binding){
                ivPhotos.load(mapStory.photoUrl){
                    crossfade(true)
                    crossfade(600)
                    error(R.drawable.ic_baseline_broken_image_24)
                    placeholder(R.drawable.ic_baseline_image_24)
                    transformations(RoundedCornersTransformation(16f))
                }
                tvUserName.text = mapStory.name
                tvDesc.text = mapStory.createdAt.genereteDate
                btnToDetail.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            androidx.core.util.Pair(ivPhotos, "gambar"),
                            androidx.core.util.Pair(tvUserName, "name"),
                            androidx.core.util.Pair(tvDesc, "date"),
                        )
                    itemView.context.startActivity(
                        Intent(
                            context,
                            DetailActivity::class.java
                        ).apply { putExtra(Contant.EXTRA_MAP, mapStory)
                                putExtra(POSITION,2)
                                },
                        optionsCompat.toBundle()
                    )
                }

                btnToRoute.setOnClickListener {
                    Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?daddr=${mapStory.lat},${mapStory.lon}")).also {
                        context.startActivity(it)
                    }
                }

                root.setOnClickListener { onItemClicked(position) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapsAdapter.ViewHolder {
        return ViewHolder(
            ItemListMapsBinding.inflate(LayoutInflater.from(context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: MapsAdapter.ViewHolder, position: Int) {
        holder.bind(context,mapsStories[position],onItemClicked,position)
    }

    override fun getItemCount(): Int = mapsStories.size
}