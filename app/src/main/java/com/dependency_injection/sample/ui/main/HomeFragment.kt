package com.dependency_injection.sample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dependency_injection.base.network.NetworkBoundResource.Companion.BOUND_FROM_BACKEND
import com.dependency_injection.base.network.Status
import com.dependency_injection.base.utility.Parameters
import com.dependency_injection.base.utility.Query.Companion.doQuery
import com.dependency_injection.base.view.AutoClearedValue
import com.dependency_injection.sample.datasource.model.item.Item
import com.dependency_injection.sample.ui.BaseFragment
import com.dependency_injection.sample.ui.main.adapter.ItemListAdapter
import com.dependency_injection.sample.viewmodel.item.ItemListViewModel
import com.example.demo.R
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {
    @Inject
    internal lateinit var itemListViewModel: ItemListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val acvView =
                AutoClearedValue(
                        this,
                        inflater.inflate(R.layout.fragment_home, container, false)
                )
        return acvView.get()?.rootView
    }

    private lateinit var adapter: ItemListAdapter
    private lateinit var acvAdapter: AutoClearedValue<ItemListAdapter>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(10)
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.isVerticalScrollBarEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapter = ItemListAdapter() { item ->

        }
        adapter.setHasStableIds(true)
        acvAdapter = AutoClearedValue(this, adapter)
        acvAdapter.get()?.setHasStableIds(true)
        recyclerView.adapter = acvAdapter.get()
        getList()
    }

    private fun getList() {
        val query = doQuery(listOf("football"))
        query.boundType = BOUND_FROM_BACKEND

        itemListViewModel

        itemListViewModel.pullTrigger(Parameters(query))
        itemListViewModel.assets.observe(this.viewLifecycleOwner, Observer { resource ->
            when (resource.getStatus()) {
                Status.SUCCESS -> {
                    resource.getData()?.let { list ->
                        @Suppress("UNCHECKED_CAST")
                        list as PagedList<Item>
                        acvAdapter.get()?.submitList(list)
                    }
                }

                Status.LOADING -> {
                }

                Status.ERROR -> {
                }

                else -> {
                }
            }

            if (resource.getStatus() != Status.LOADING) {
            }
        })
    }
}