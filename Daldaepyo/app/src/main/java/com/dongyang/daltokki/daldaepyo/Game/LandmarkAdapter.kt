package com.dongyang.daltokki.daldaepyo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.retofit.BoardItem
import com.dongyang.daltokki.daldaepyo.retrofit.SearchResponseDto
import retrofit2.Callback

class LandmarkAdapter(val context: Callback<SearchResponseDto>, val List: MutableList<BoardItem>): BaseAdapter() {

    override fun getCount(): Int {
        return List.size
    }

    override fun getItem(position: Int): Any {
        return List[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.item_search_review, parent, false)
        }

        var title = convertView?.findViewById<TextView>(R.id.tv_search_review)
        val list = List[position]
        title!!.text = list.title

        return convertView!!
    }
}