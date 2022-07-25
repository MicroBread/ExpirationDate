package com.meng.expirationdate.view.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.meng.expirationdate.R
import com.meng.expirationdate.databinding.ListItemInfoBinding
import com.meng.expirationdate.room.ItemInfo
import com.meng.expirationdate.utils.onClickNoAnim

/**
 * 物品详情条目适配器
 */
class ItemsAdapter(private val mContext: Context, private val datas: MutableList<ItemInfo>) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {
    private var listener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_info, parent, false), 0)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val bean = datas[position]
        val nearBinding = holder.bind as ListItemInfoBinding
        nearBinding.bean = bean
        nearBinding.clRoot.onClickNoAnim { listener?.itemClick(bean) }
        nearBinding.clRoot.setOnLongClickListener {
            listener?.itemLongClick(bean)
            true
        }
        holder.bind.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    class ItemViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var bind: ViewDataBinding = DataBindingUtil.bind(itemView)!!
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun itemClick(item: ItemInfo)
        fun itemLongClick(item: ItemInfo)
        fun itemChildClick(view: View, item: ItemInfo, anim: AnimationDrawable)
    }
}