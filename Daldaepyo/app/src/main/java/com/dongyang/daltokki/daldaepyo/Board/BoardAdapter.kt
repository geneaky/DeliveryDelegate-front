package com.dongyang.daltokki.daldaepyo.Board

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.daltokki.daldaepyo.Game.AttendOrderActivity
import com.dongyang.daltokki.daldaepyo.R

class BoardAdapter(private val context: Context, private val GameList: ArrayList<BoardItem>) :
    RecyclerView.Adapter<BoardAdapter.ItemViewHolder>() {

    val preference = this.context.getSharedPreferences("Gamepref", 0)

    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tv_board_title)
        private val population = itemView.findViewById<TextView>(R.id.tv_board_size)
        private val landmark = itemView.findViewById<TextView>(R.id.tv_board_landmark)
        private val write = itemView.findViewById<TextView>(R.id.tv_board_writer)
        private val date = itemView.findViewById<TextView>(R.id.tv_board_date)
        
        fun bind(boardItem: BoardItem, context: Context) {
            title.text = boardItem.title
            population.text = boardItem.population.toString()
            landmark.text = boardItem.landmark
            write.text = boardItem.write
            date.text = boardItem.date

            itemView.setOnClickListener {
                Intent(context, AttendOrderActivity::class.java).apply {
                    val edit = preference.edit()
                    edit.apply()
                    edit.putString("room_name", boardItem.socket_room_name)
                    edit.putInt("Population", boardItem.population)
                    edit.putInt("game_id", boardItem.game_id)
                    edit.commit()
                }.run { context.startActivity(this) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_board, parent, false))
    }

    override fun onBindViewHolder(holder: BoardAdapter.ItemViewHolder, position: Int) {
        holder.bind(GameList[position], context)
        Log.d("리사이클러뷰가 불러짐", "ㅇㅇㅇ")
    }

    override fun getItemCount(): Int {
        return GameList.size
    }


}