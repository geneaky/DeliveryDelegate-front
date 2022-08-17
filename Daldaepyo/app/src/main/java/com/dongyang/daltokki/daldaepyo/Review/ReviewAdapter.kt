package com.dongyang.daltokki.daldaepyo

import android.accounts.AccountManager.get
import android.content.Context
import android.graphics.ColorSpace.get
import android.media.CamcorderProfile.get
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration.get
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewTreeLifecycleOwner.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dongyang.daltokki.daldaepyo.Board.BoardAdapter
import com.dongyang.daltokki.daldaepyo.Review.ReviewItem
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewDto
import com.google.gson.reflect.TypeToken.get
import okio.Utf8.size
import java.lang.reflect.Array.get
import java.nio.file.Files.size
import kotlin.coroutines.EmptyCoroutineContext.get


class ReviewAdapter(
        private val ReviewList: ArrayList<ReviewItem>,
        val context: Context
) : RecyclerView.Adapter<ReviewAdapter.ItemViewHolder>()
{
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var store: TextView = itemView.findViewById(R.id.tv_review_store)
        private var writer: TextView = itemView.findViewById(R.id.tv_review_writer)
        private var content: TextView = itemView.findViewById(R.id.tv_review_content)
        private var image_path : ImageView = itemView.findViewById(R.id.img_review)

        fun bind(reviewItem: ReviewItem, context: Context){
            store.text = reviewItem.store_name
            writer.text = reviewItem.user_name
            content.text = reviewItem.content
            Glide.with(itemView).load("http://146.56.132.245:8080/"+reviewItem.image_path).into(image_path)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_review, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ItemViewHolder, position: Int) {
        holder.bind(ReviewList[position], context)
        Log.d("리뷰_리사이클러뷰", "fff")

    }

    override fun getItemCount(): Int {
        return ReviewList.size
    }

}