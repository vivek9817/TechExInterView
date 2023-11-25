package com.example.techexac.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import app.bandhan.bhp.presenter.viewholder.GenericViewHolder
import com.example.techexac.Model.AppList

abstract class CommonAlertAdapter<T>(
    private var itemLayoutId: Int,
    var items: ArrayList<T>
) : RecyclerView.Adapter<GenericViewHolder<T>>() {

    abstract fun bindData(holder: GenericViewHolder<T>, item: T)
    abstract fun clickHanlder(pos: Int, item: T, aView: View): Unit

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        return GenericViewHolder(parent, itemLayoutId) { pos, aView ->
            clickHanlder(pos, items[pos], aView)
        }
    }

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        bindData(holder, items[position])
    }

    fun getFilter(tag: String, list: (ArrayList<T>) -> Unit): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val queryString = p0.toString()
                val filterResults = FilterResults()
                when (tag) {
                    "1" -> {
                        filterResults.values =
                            if (queryString.isEmpty()) {
                                items
                            } else {
                                (items as ArrayList<AppList>).filter {
                                    it.app_name!!.contains(
                                        queryString,
                                        ignoreCase = true
                                    ) || it.app_name!!.contains(p0.toString())
                                }
                            }
                    }
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                list(p1?.values as ArrayList<T>)
                /*items = p1?.values as ArrayList<T>
                notifyDataSetChanged()*/
            }
        }
    }

}