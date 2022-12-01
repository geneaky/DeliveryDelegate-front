package com.dongyang.daltokki.daldaepyo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
        var btn_thumbUp: ImageButton = itemView.findViewById(R.id.btn_thumbup)


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
            
            var c_count = 0

                
            btn_thumbUp.setOnClickListener{
                like(review_id)
                c_count += 1
                if(is_Liked == true){ // 초기상태 : 눌린상태
                    if (c_count % 2 == 0 ){ // 복귀
                        btn_thumbUp.setImageResource(R.drawable.ic_dal_fill)
                        thumb_up.text = (item.thumb_up).toString()
                    } else { // 좋아요 취소
                        btn_thumbUp.setImageResource(R.drawable.ic_dal_line)
                        thumb_up.text = (item.thumb_up - 1 ).toString()
                    }
                } else { // 초기상태 : 안 눌린상태
                    if (c_count % 2 == 0 ){ // 복귀
                        btn_thumbUp.setImageResource(R.drawable.ic_dal_line)
                        thumb_up.text = (item.thumb_up).toString()
                    } else { // 좋아요 누름
                        btn_thumbUp.setImageResource(R.drawable.ic_dal_fill)
                        thumb_up.text = (item.thumb_up + 1 ).toString()
                    }
                }


            }






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
//        val seqCheckBox = holder.itemView.findViewById<CheckBox>(R.id.btn_thumbup)
//        seqCheckBox.setOnCheckedChangeListener(null)
//        seqCheckBox.isChecked = selectCheckBoxPosition.containsValue(position)
//        seqCheckBox.setOnClickListener{
//            val seq = seqCheckBox.tag.toString().toBoolean()
//            if(seqCheckBox.isChecked){
//                selectCheckBoxPosition[seq] = position
//            }
//            else{
//                selectCheckBoxPosition.remove(seq)
//            }
//        }





    }

    override fun getItemCount(): Int = rList.size

    override fun getItemId(position: Int): Long = position.toLong()

    fun like(review_id: Int) {
        val data = ThumbUpDto((review_id))
        UserAPI.create().postThumbUp(tok, data).enqueue(object : Callback<ThumbUpDto> {
            override fun onResponse(call: retrofit2.Call<ThumbUpDto>, response: Response<ThumbUpDto>) {
                Log.d("ReviewAdapter ::", "Success")
            }
            override fun onFailure(call: retrofit2.Call<ThumbUpDto>, t: Throwable) {
                Log.d("ReviewAdapter ::", t.message.toString())
            }
        })

    }

    companion object{
        lateinit var selectCheckBoxPosition: HashMap<Boolean, Int>
        private set
    }
}
