package kr.co.tjoeun.colosseum_20200716.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kr.co.tjoeun.colosseum_20200716.R
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil

class ReReplyAdapter(mContext: Context, resId : Int, val mList:List<Reply>): ArrayAdapter<Reply>(mContext, resId, mList) {
    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView //convertView 는 값을 변경할 수 없기 때문에 tempRow를 이용하여 값을 넣음
        if(tempRow == null)
        {
            tempRow = inf.inflate(R.layout.re_reply_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]
        val writerNickNameTxt = row.findViewById<TextView>(R.id.writerNickNameTxt)
        val selectedSideTitleTxt = row.findViewById<TextView>(R.id.selectedSideTitleTxt)
        val contentTxt = row.findViewById<TextView>(R.id.contentTxt)
        val replyWriteTime = row.findViewById<TextView>(R.id.replyWriteTime)

        writerNickNameTxt.text = data.writer.nickName
        selectedSideTitleTxt.text = data.selectedSide.title
        contentTxt.text = data.content
        replyWriteTime.text = TimeUtil.getTimeAgoFromCalendar(data.writtenDateTime)

        return row
    }
}