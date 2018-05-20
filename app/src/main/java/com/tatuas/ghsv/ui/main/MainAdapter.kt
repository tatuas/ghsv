package com.tatuas.ghsv.ui.main

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tatuas.ghsv.R
import com.tatuas.ghsv.data.db.User
import com.tatuas.ghsv.data.glide.GlideApp
import com.tatuas.ghsv.databinding.ItemMainContentBinding
import com.tatuas.ghsv.ext.loadDefault

class MainAdapter(private val context: Context)
    : PagedListAdapter<User, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                    oldItem.id == newItem.id

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                    oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val inflater = LayoutInflater.from(context)

    var onItemClick: ((String) -> Unit)? = null

    override fun getItemViewType(position: Int) = ViewType.CONTENT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.CONTENT -> ContentViewHolder(ItemMainContentBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContentViewHolder -> {
                val item = getItem(position) ?: return

                holder.binding.root.setOnClickListener { onItemClick?.invoke(item.name) }

                holder.binding.name.text = item.name

                GlideApp.with(context)
                        .loadDefault(item.imageUrl)
                        .centerCrop()
                        .into(holder.binding.image)
            }
            else -> throw IllegalArgumentException()
        }
    }

    class ContentViewHolder(val binding: ItemMainContentBinding) : RecyclerView.ViewHolder(binding.root)

    private object ViewType {
        const val CONTENT = R.layout.item_main_content
    }
}
