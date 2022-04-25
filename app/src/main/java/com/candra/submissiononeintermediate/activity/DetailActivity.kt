package com.candra.submissiononeintermediate.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.RoundedCornersTransformation
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.databinding.DetailActivityBinding
import com.candra.submissiononeintermediate.helper.Contant.EXTRA_DATA
import com.candra.submissiononeintermediate.helper.Help
import com.candra.submissiononeintermediate.helper.genereteDate
import com.candra.submissiononeintermediate.model.Story
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity: AppCompatActivity()
{

    private lateinit var binding: DetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataFromAdapter()

        Help.showToolbar(this@DetailActivity,supportActionBar,getString(R.string.detail_cerita),2)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getDataFromAdapter(){
        intent.extras?.getParcelable<Story>(EXTRA_DATA)?.let {
            with(binding){
                gambarDetail.load(it.photoUrl){
                    crossfade(true)
                    crossfade(600)
                    transformations(RoundedCornersTransformation())
                }
                textUsername.text = it.name
                textDate.text = it.createdAt.genereteDate
                textDeskripsi.text = it.description
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}