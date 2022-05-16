package com.dongyang.daltokki.daldaepyo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.daltokki.daldaepyo.databinding.ItemLoadingBinding
import com.dongyang.daltokki.daldaepyo.databinding.ItemBoardBinding

class BoardAdapter() {}

/*
        RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    private var boardList = emptyList<Post>()

    class BoardViewHolder(val binding: ItemLoadingBinding) : RecyclerView.ViweHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.binding.tv_title.text = boardList[position].title.toString()
        holder.binding.tv_date.text = boardList[position].title
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    fun setData(newList: List<Post>){
        boardList = newList
        //새로고침
        notifyDataSetChanged()
    }
}


*/