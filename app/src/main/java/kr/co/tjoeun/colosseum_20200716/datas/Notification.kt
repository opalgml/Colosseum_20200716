package kr.co.tjoeun.colosseum_20200716.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Notification {
    var id = 0
    var title = ""
//    알림 발생 시간 변수
    val createdAtCal = Calendar.getInstance()

    companion object {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        fun getNotificationFromJson(json : JSONObject) : Notification{
            val n = Notification()

            n.id = json.getInt("id")
            n.title = json.getString("title")


            n.createdAtCal.time = sdf.parse(json.getString("created_at"))

            val myPhoneTimeZone = n.createdAtCal.timeZone
            val timeOffSet = myPhoneTimeZone.rawOffset / 1000 / 60 / 60
            n.createdAtCal.add(Calendar.HOUR, timeOffSet)

            return n
        }
    }
}