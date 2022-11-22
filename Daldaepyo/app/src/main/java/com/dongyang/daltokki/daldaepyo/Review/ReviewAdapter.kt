package com.dongyang.daltokki.daldaepyo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewDto
import com.dongyang.daltokki.daldaepyo.retrofit.ThumbUpDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import retrofit2.Callback
import retrofit2.Response


class ReviewAdapter(
        val context: Context
) : RecyclerView.Adapter<ReviewAdapter.ItemViewHolder>() {

    var rList = mutableListOf<ReviewDto>()

    val preferences = this.context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    val tok = preferences.getString("token", "").toString()

    var thumbUp = false

    fun submitList(rList: List<ReviewDto>) {
        this.rList.clear()
        this.rList.addAll(rList)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var store: TextView = itemView.findViewById(R.id.tv_review_store)
        private var writer: TextView = itemView.findViewById(R.id.tv_review_writer)
        private var content: TextView = itemView.findViewById(R.id.tv_review_content)
        var image_path: ImageView = itemView.findViewById(R.id.img_review)
        var thumb_up: TextView = itemView.findViewById(R.id.tv_thumbup_count)
        var btn_thumbUp: ImageView = itemView.findViewById(R.id.btn_thumbup)


        fun bind(item: ReviewDto, position: Int) {
            store.text = item.store_name
            writer.text = item.user_name
            content.text = item.content
            thumb_up.text = item.thumb_up.toString()

            val review_id = item.review_id
            val is_Liked = item.is_Liked

            if(is_Liked == true){
                btn_thumbUp.setImageResource(R.drawable.ic_dal_fill)
            }
            else{
                btn_thumbUp.setImageResource(R.drawable.ic_dal_line)
            }

            btn_thumbUp.setOnClickListener {
                Log.d("ReviewAdapter ::", "onClick")
                like(review_id)
//                var count = 0
//                count++
                thumb_up.text = (item.thumb_up + 1).toString()
                notifyDataSetChanged()
                thumbUp = true

                if(thumbUp != true){
                    btn_thumbUp.setImageResource(R.drawable.ic_dal_line)
                    notifyItemChanged(position)
                }
                else{
                    btn_thumbUp.setImageResource(R.drawable.ic_dal_fill)
                    btn_thumbUp.setOnClickListener{
                        thumbUp = false
                        notifyItemChanged(position)
                    }
                }

            }//btn setOnClickListener


//            image_path.setOnClickListener() {
//                val intent = Intent(context, ImageActivity::class.java)
//                intent.putExtra("img_path", image_path.toString())
//                intent.run{context.startActivity(intent)}
//            }


            Glide.with(itemView)
                    .load("http://146.56.132.245:8080/${item.image_path}").into(image_path)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_review, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ItemViewHolder, position: Int) {
        holder.bind(rList[position], position)


    }

    override fun getItemCount(): Int = rList.size

    override fun getItemId(position: Int): Long = position.toLong()

    fun like(position: Int) {
        val data = ThumbUpDto((position))
        UserAPI.create().postThumbUp(tok, data).enqueue(object : Callback<ThumbUpDto> {
            override fun onResponse(call: retrofit2.Call<ThumbUpDto>, response: Response<ThumbUpDto>) {
                Log.d("ReviewAdapter ::", "Success")
            }
            override fun onFailure(call: retrofit2.Call<ThumbUpDto>, t: Throwable) {
                Log.d("ReviewAdapter ::", t.message.toString())
            }
        })

    }
}
