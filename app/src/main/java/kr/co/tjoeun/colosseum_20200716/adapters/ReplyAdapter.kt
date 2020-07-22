package kr.co.tjoeun.colosseum_20200716.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kr.co.tjoeun.colosseum_20200716.R
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.datas.Topic

class ReplyAdapter(val mContext: Context, val resId:Int, val mList:List<Reply>) : ArrayAdapter<Reply>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if(tempRow == null)
        {
            tempRow = inf.inflate(R.layout.activity_reply_list, null)
        }

        val row = tempRow!!

//        val topicImg = row.findViewById<ImageView>(R.id.topicImg)
//        val topicTitleTxt = row.findViewById<TextView>(R.id.topicTitleTxt)
//
//        val data = mList[position]
//
//        topicTitleTxt.text = data.title
//        Glide.with(mContext).load(data.imageUrl).into(topicImg)

        return row
    }

}