package com.dongyang.daltokki.daldaepyo.User

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.retrofit.*
import retrofit2.Callback
import retrofit2.Response

class UserReviewAdapter(private val context: Context) : RecyclerView.Adapter<UserReviewAdapter.ViewHolder>() {

    var datas = mutableListOf<UserReviewDto>()

    val preferences = this.context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    val tok = preferences.getString("token", "").toString()

    fun submitList(rList: List<UserReviewDto>) {
        this.datas.clear()
        this.datas.addAll(rList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserReviewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_review, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val store : TextView = itemView.findViewById(R.id.tv_review_user_store)
        private val content : TextView = itemView.findViewById(R.id.tv_review_user_content)
        private val image : ImageView = itemView.findViewById(R.id.img_review_user)
        private val writer : TextView = itemView.findViewById(R.id.tv_review_user_writer)
        private val btn_delete : ImageView = itemView.findViewById(R.id.img_review_delete)

        fun bind(item: UserReviewDto, position: Int) {
            store.text = item.store_name
            writer.text = item.user_name
            content.text = item.content

            val review_id = item.review_id


            Glide.with(itemView)
                .load("http://146.56.132.245:8080/${item.image_path}").into(image)

            btn_delete.setOnClickListener {
                datas.removeAt(position)
                delete(review_id)
                notifyDataSetChanged()
            }
        }
    }


    override fun onBindViewHolder(holder: UserReviewAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position], position)

    }


    override fun getItemCount(): Int = datas.size

    fun delete(review_id : Int){
        Log.d("position::", review_id.toString())
        UserAPI.create().postDeleteReview(tok, review_id).enqueue(object : Callback<ReviewDeleteDto> {
            override fun onResponse(call: retrofit2.Call<ReviewDeleteDto>, response: Response<ReviewDeleteDto>) {
                Log.d("UserReviewAdapter ::", "Success")
                Log.d("UserReviewAdapter", response.code().toString())
            }
            override fun onFailure(call: retrofit2.Call<ReviewDeleteDto>, t: Throwable) {
                Log.d("UserReviewAdapter ::", t.message.toString())
            }
        })

    }
}