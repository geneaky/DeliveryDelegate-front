package com.dongyang.daltokki.daldaepyo

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.SharedPreferencesCompat.EditorCompat.getInstance
import androidx.core.os.persistableBundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.dongyang.daltokki.daldaepyo.Board.BoardAdapter
import com.dongyang.daltokki.daldaepyo.Review.ReviewDetailActivity
import com.dongyang.daltokki.daldaepyo.Review.ReviewItem
import com.dongyang.daltokki.daldaepyo.databinding.ItemReviewBinding
import com.dongyang.daltokki.daldaepyo.retrofit.*
import com.google.android.material.circularreveal.CircularRevealWidget
import com.naver.maps.map.style.light.Position
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_review.view.*
import org.w3c.dom.Text
import retrofit2.Callback
import retrofit2.Response


class ReviewAdapter (
        private val ReviewList: ArrayList<ReviewItem>,
        val context: Context
) : RecyclerView.Adapter<ReviewAdapter.ItemViewHolder>() {

    var rList = mutableListOf<ReviewItem>()



    val preferences = this.context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    val tok = preferences.getString("token", "").toString()

    val reviewPref = this.context.getSharedPreferences("review", Context.MODE_PRIVATE)
    val review = reviewPref.getInt("review",0)

    val thumbPref = this.context.getSharedPreferences("thumbUp",Context.MODE_PRIVATE)
    val thumbUp = thumbPref.getInt("thumbUp",0)

//    val data = ThumbUpDto(review)




    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var store: TextView = itemView.findViewById(R.id.tv_review_store)
        private var writer: TextView = itemView.findViewById(R.id.tv_review_writer)
        private var content: TextView = itemView.findViewById(R.id.tv_review_content)
        var image_path: ImageView = itemView.findViewById(R.id.img_review)
        var thumb_up: TextView = itemView.findViewById(R.id.tv_thumbup_count)
        var btn_thumbUp : ImageView = itemView.findViewById(R.id.btn_thumbup)



        fun bind(reviewItem: ReviewItem, context: Context) {
            store.text = reviewItem.store_name
            writer.text = reviewItem.user_name
            content.text = reviewItem.content
            thumb_up.text = reviewItem.thumb_up.toString()

            itemView.setOnClickListener {
                Intent(context, ReviewDetailActivity::class.java).apply{

                }.run {
                    context.startActivity(this)
                }
            }

            Glide.with(itemView).load("http://146.56.132.245:8080/" + reviewItem.image_path).into(image_path)


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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun like(position:Int){
        if(position+1 == review) {
            val data = ThumbUpDto((position + 1))
            UserAPI.create().postThumbUp(tok, data).enqueue(object : Callback<ThumbUpDto> {
                override fun onResponse(call: retrofit2.Call<ThumbUpDto>, response: Response<ThumbUpDto>) {
                    val result = response.body().toString()
                    Log.d("position", position.toString())
                    Log.d("dto", data.toString())
                    Log.d("Log", result)
                    Log.d("review", review.toString())

                }

                override fun onFailure(call: retrofit2.Call<ThumbUpDto>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
            })
        }



    }




}

