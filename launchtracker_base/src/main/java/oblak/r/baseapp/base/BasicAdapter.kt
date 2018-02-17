package oblak.r.baseapp.base

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList

/**
 * Created by rokoblak on 10/21/16.
 * To support multiple types of viewholders, set the viewTypeFn to return the appropriate type
 * To support footers, assign any non-zero value to footerResourceId
 */

abstract class BaseViewHolder<in TData>(view: View, val viewType: Int = 0) : RecyclerView.ViewHolder(view) {
    abstract fun populateView(data: TData, position: Int)
}

open class BasicAdapter<out TViewHolder : BaseViewHolder<TData>, TData>(
        creatingFn: (ViewGroup, Int) -> TViewHolder) : AbstractBasicAdapter<TViewHolder, TData>(creatingFn, false)

class BasicStableAdapter<out TViewHolder : BaseViewHolder<TData>, TData>(
        creatingFn: (ViewGroup, Int) -> TViewHolder) : AbstractBasicAdapter<TViewHolder, TData>(creatingFn, true)

abstract class AbstractBasicAdapter<out TViewHolder : BaseViewHolder<TData>, TData>(
        // required function that creates the ViewHolder item on the onCreateViewHolder
        private val creatingFn: (ViewGroup, Int) -> TViewHolder, private val stableIds: Boolean = false) : RecyclerView.Adapter<BaseViewHolder<TData>>() {

    var onItemClicked: ((data: TData, view: View?, position: Int) -> Unit)? = null // optional, called when the item is clicked

    var viewTypeFn: ((data: TData, position: Int) -> Int)? = null // optional function to return the item view type

    var resolveItemId: ((data: TData, position: Int) -> Long)? = null

    var items: MutableList<TData> = ArrayList()
        private set

    init {
        if (stableIds) setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return if (stableIds) {
            resolveItemId?.invoke(items[position], position) ?: items[position].toString().hashCode().toLong()
        } else {
            super.getItemId(position)
        }
    }

    /* assign the items by reference */
    fun setItemsByRef(items: MutableList<TData>) {
        this.items = items
        notifyDataSetChanged()
    }

    /* copy the items */
    fun setNewItems(newItems: List<TData>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<TData>) {
        val startIdx = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startIdx, items.size)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun updateItems(newItems: List<TData>) {
        items.clear()
        items.addAll(newItems)
        notifyItemRangeChanged(0, items.count())
    }

    override fun onBindViewHolder(holder: BaseViewHolder<TData>, position: Int) {
        if (position >= items.count()) {
            return
        }

        if (onItemClicked != null) {
            holder.itemView?.setOnClickListener {
                onItemClicked?.invoke(items[position], holder.itemView, position)
            }
        }

        holder.populateView(items[position], position)
    }

    override fun getItemCount(): Int = items.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TData> {
        return creatingFn(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypeFn?.invoke(items[position], position) ?: 0
    }
}