package com.candra.submissiononeintermediate.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.adapter.ListStoryAdapter
import com.candra.submissiononeintermediate.databinding.ListStoryActivityBinding
import com.candra.submissiononeintermediate.helper.Help
import com.candra.submissiononeintermediate.model.LoginUpUser
import com.candra.submissiononeintermediate.viewmodel.PostStoryViewModel
import com.candra.submissiononeintermediate.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListStroy: AppCompatActivity()
{

    private lateinit var binding: ListStoryActivityBinding
    private val userViewModel: UserViewModel by viewModels()
    private val adapterMain by lazy { ListStoryAdapter()}
    private val storyViewModel: PostStoryViewModel by viewModels()
    private lateinit var layoutManager1: LinearLayoutManager
    private lateinit var dataUser: LoginUpUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListStoryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Help.showToolbar(this@ListStroy,supportActionBar,resources.getString(R.string.daftar_story),1)


        setOnClickListener()

        readDataAll()

    }


    private fun setOnClickListener(){
        addDataToRecyclerView()
        binding.apply {
           nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY < oldScrollY) binding.fabAdd.extend() else binding.fabAdd.shrink()
            })
            binding.fabAdd.setOnClickListener {
                startActivity(Intent(this@ListStroy,AddStory::class.java))
            }
        }
    }

    private fun addDataToRecyclerView(){
       layoutManager1 =  LinearLayoutManager(this@ListStroy,LinearLayoutManager.VERTICAL,false)
        adapterMain.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvListStory.smoothScrollToPosition(0)
            }
        })

        binding.rvListStory.apply {
            layoutManager = layoutManager1
            adapter = adapterMain
        }
    }

    private fun readDataAll(){
        storyViewModel.storyMutableliveData.observe(this){
            layoutManager1.scrollToPositionWithOffset(0,0)
            adapterMain.temptDataAll(it)
        }

        storyViewModel.mutableListEmpty.observe(this){
            binding.mtvNoData.visibility = if (it) View.VISIBLE else View.GONE
            binding.rvListStory.visibility = if (it) View.GONE else View.VISIBLE
        }

        storyViewModel.loading.observe(this){
            if (it) showShimmerEffect() else hideShimmerEffect()
        }

        storyViewModel.errorMessage.observe(this){
            Help.showDialog(this@ListStroy,it)
        }

        storyViewModel.getDataUser(this@ListStroy).observe(this){ data ->
            if(data.isLogginIn == false){
                startActivity(Intent(this@ListStroy,LoginActivity::class.java))
                finish()
            }

            this.dataUser = data

            lifecycleScope.launch {
                data.token?.let { storyViewModel.getAllStories(it) }
            }
        }
    }

    private fun showShimmerEffect(){
        binding.apply {
            shimmerEffect.startShimmer()
            shimmerEffect.visibility = View.VISIBLE
            rvListStory.visibility = View.GONE
        }
    }

    private fun hideShimmerEffect(){
        binding.apply {
            shimmerEffect.hideShimmer()
            shimmerEffect.visibility = View.GONE
            rvListStory.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.bahasa -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.logout -> {
                lifecycleScope.launch {
                    userViewModel.logoutUser(this@ListStroy)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}