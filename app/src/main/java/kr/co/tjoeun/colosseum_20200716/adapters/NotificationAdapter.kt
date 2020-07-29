package kr.co.tjoeun.colosseum_20200716.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kr.co.tjoeun.colosseum_20200716.R
import kr.co.tjoeun.colosseum_20200716.datas.Notification
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil

class NotificationAdapter(val mContext: Context, resId : Int, val mList:List<Notification>) : ArrayAdapter<Notification>(mContext, resId, mList) {
    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView //convertView 는 값을 변경할 수 없기 때문에 tempRow를 이용하여 값을 넣음
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.reply_list_item, null)
        }

        val row = tempRow!!

        val mainNotiTxt = row.findViewById<TextView>(R.id.mainNotiTxt)
        val timeNotiTxt = row.findViewById<TextView>(R.id.timeNotiTxt)

        val data = mList[position]
        mainNotiTxt.text = data.title
        timeNotiTxt.text = TimeUtil.getTimeAgoFromCalendar(data.createdAtCal)

        return row

    }
}