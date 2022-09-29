package com.dongyang.daltokki.daldaepyo.Game

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.dongyang.daltokki.daldaepyo.GameActivity
import com.dongyang.daltokki.daldaepyo.R

class GameDelegatorListAdapter(val context: GameActivity, val List: MutableList<GameDelegatorListItem>) : BaseAdapter(){
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

        var nickname = convertView?.findViewById<TextView>(R.id.tv_search_review)
        val list = List[position]
        nickname!!.text = list.nickname

        return convertView!!
    }
}