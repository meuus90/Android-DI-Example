package com.dependency_injection.sample.ui.main.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dependency_injection.base.view.BaseViewHolder
import com.dependency_injection.sample.datasource.model.item.Item
import com.example.demo.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_play.*

class ItemListAdapter(val doOnClick: (item: Item) -> Unit) : PagedListAdapter<Item, BaseViewHolder<Item>>(DIFF_CALLBACK) {
    companion object {
        private val PAYLOAD_TITLE = Any()

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                    oldItem == newItem

            override fun getChangePayload(oldItem: Item, newItem: Item): Any? {
                return if (sameExceptTitle(oldItem, newItem)) {
                    PAYLOAD_TITLE
                } else {
                    null
                }
            }
        }

        private fun sameExceptTitle(oldItem: Item, newItem: Item): Boolean {
            return oldItem.copy(id = newItem.id) == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Item> {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val view = inflater.inflate(R.layout.item_play, parent, false)
        return PlayItemHolder(view, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Item>, position: Int) {
        val item = getItem(position)

        item?.let {
            if (holder is PlayItemHolder) {
                holder.bindItemHolder(holder, it, position)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id.hashCode().toLong()
    }

    private var searchedText = ""
    fun searchText(text: String) {
        searchedText = text
        notifyDataSetChanged()
    }

    class PlayItemHolder(
            override val containerView: View,
            private val adapter: ItemListAdapter
    ) : BaseViewHolder<Item>(containerView), LayoutContainer {
        @SuppressLint("SetTextI18n")
        override fun bindItemHolder(holder: BaseViewHolder<Item>, item: Item, position: Int) {
            tv_name.text = item.snippet.title
        }

        override fun onItemSelected() {
            containerView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            containerView.setBackgroundColor(0)
        }
    }
}