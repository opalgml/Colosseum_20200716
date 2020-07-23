package kr.co.tjoeun.colosseum_20200716.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import kr.co.tjoeun.colosseum_20200716.R
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil
import java.text.SimpleDateFormat

class ReplyAdapter(val mContext: Context, resId : Int, val mList:List<Reply>) : ArrayAdapter<Reply>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView //convertView 는 값을 변경할 수 없기 때문에 tempRow를 이용하여 값을 넣음
        if(tempRow == null)
        {
            tempRow = inf.inflate(R.layout.reply_list_item, null)
        }

        val row = tempRow!!

        val writerNickNameTxt = row.findViewById<TextView>(R.id.writerNickNameTxt)
        val selectedSideTitleTxt = row.findViewById<TextView>(R.id.selectedSideTitleTxt)
        val contentTxt = row.findViewById<TextView>(R.id.contentTxt)

//        시간 정보 텍스트뷰
        val replyWriteTime = row.findViewById<TextView>(R.id.replyWriteTime)
        val likeBtn = row.findViewById<Button>(R.id.likeBtn)
        val dislikeBtn = row.findViewById<Button>(R.id.dislikeBtn)
        val replyBtn = row.findViewById<Button>(R.id.replyBtn)

        val data = mList[position]

        writerNickNameTxt.text = data.writer.nickName
        selectedSideTitleTxt.text = "(${data.selectedSide.title})"
        contentTxt.text = data.content

//        시간정보 텍스트뷰 내용 설정
        replyWriteTime.text = TimeUtil.getTimeAgoFromCalendar(data.writtenDateTime)

//        날짜 출력 양식용 변수
//        val sdf = SimpleDateFormat("yy-MM-dd a h시 m분")
//        replyWriteTime.text = sdf.format(data.writtenDateTime.time)

        likeBtn.text = "좋아요 ${data.likeCount.toString()}"
        dislikeBtn.text = "싫어요 ${data.dislikeCount.toString()}"
        replyBtn.text = "답글 ${data.replyCount.toString()}"

        return row
    }

}