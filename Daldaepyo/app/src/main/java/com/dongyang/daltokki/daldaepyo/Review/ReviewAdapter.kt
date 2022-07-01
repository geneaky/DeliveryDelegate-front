package com.dongyang.daltokki.daldaepyo

import android.accounts.AccountManager.get
import android.content.Context
import android.graphics.ColorSpace.get
import android.media.CamcorderProfile.get
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration.get
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewTreeLifecycleOwner.get
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewDto
import com.google.gson.reflect.TypeToken.get
import okio.Utf8.size
import java.lang.reflect.Array.get
import java.nio.file.Files.size
import kotlin.coroutines.EmptyCoroutineContext.get


class ReviewAdapter(
        private val reviewList: List<ReviewDto>?,
        val context: Context?
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var storeName: TextView = itemView.findViewById(R.id.tv_reiview_store)
        var writer: TextView = itemView.findViewById(R.id.tv_review_writer)
        var content: TextView = itemView.findViewById(R.id.tv_review_content)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ReviewDto = reviewList!!.get(position)
        holder.storeName.setText(reviewList?.get(position)?.review_id)
        holder.writer.setText(reviewList?.get(position)?.user_id)
        holder.content.setText(reviewList?.get(position)?.content)

    }

    override fun getItemCount(): Int {
        var size: Int = 0
        if (reviewList != null) {
            size = reviewList.size
        }
        return size
    }

}