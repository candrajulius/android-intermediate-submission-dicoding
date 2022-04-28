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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.adapter.ListStoryAdapter
import com.candra.submissiononeintermediate.databinding.ListStoryActivityBinding
import com.candra.submissiononeintermediate.helper.Help
import com.candra.submissiononeintermediate.model.LoginUpUser
import com.candra.submissiononeintermediate.paging.LoadingStateAdapter
import com.candra.submissiononeintermediate.viewmodel.PostStoryViewModel
import com.candra.submissiononeintermediate.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
                if (scrollY < oldScrollY){
                    fabAdd.extend()
                    fapMaps.extend()
                } else {
                    fabAdd.shrink()
                    fapMaps.shrink()
                }
            })
            fabAdd.setOnClickListener {
                startActivity(Intent(this@ListStroy,AddStory::class.java))
            }
            fapMaps.setOnClickListener {
                startActivity(Intent(this@ListStroy,MapsActivity::class.java))
                finish()
            }
        }
    }

    private fun addDataToRecyclerView(){

        adapterMain.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    binding.rvListStory.smoothScrollToPosition(0)
                }
            })

            lifecycleScope.launch {
                adapterMain.loadStateFlow.collect {
                    if (it.refresh is LoadState.Loading){
                        showShimmerEffect()
                    }else if (it.refresh is LoadState.Error){
                        Help.showToast(this@ListStroy,getString(R.string.kesalahan))
                    }else{
                        hideShimmerEffect()
                    }
                }
            }
        }

        val footerAdapterLoading = adapterMain.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapterMain.retry()
            }
        )

        layoutManager1 =  LinearLayoutManager(this@ListStroy,LinearLayoutManager.VERTICAL,false)

        binding.rvListStory.apply {
            layoutManager = layoutManager1
            adapter = footerAdapterLoading
        }
    }

    private fun readDataAll(){


        storyViewModel.errorMessage.observe(this){
            Help.showDialog(this@ListStroy,it)
        }

        storyViewModel.getDataUser(this@ListStroy).observe(this){ data ->
            if(data.isLogginIn == false){
                startActivity(Intent(this@ListStroy,LoginActivity::class.java))
                finish()
            }

            this.dataUser = data
        }


        storyViewModel.dataStory(this@ListStroy).observe(this){data ->
            adapterMain.submitData(lifecycle,data)
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