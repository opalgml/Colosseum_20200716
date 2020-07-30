package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_notification_list.*
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import kr.co.tjoeun.colosseum_20200716.adapters.NotificationAdapter
import kr.co.tjoeun.colosseum_20200716.adapters.ReReplyAdapter
import kr.co.tjoeun.colosseum_20200716.datas.Notification
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import org.json.JSONObject

class NotificationListActivity : BaseActivity() {

    val mNotifiList = ArrayList<Notification>()
    lateinit var mNotificationAdapter : NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        getRequestNotification()
    }

    override fun setValues() {

        mNotificationAdapter = NotificationAdapter(mContext, R.layout.notification_list_item, mNotifiList)
        notifyListView.adapter = mNotificationAdapter

    }

    fun getRequestNotification(){
        ServerUtil.getRequestNotification(mContext, object: ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val dataObj = json.getJSONObject("data")
                val notifications = dataObj.getJSONArray("notifications")
                for (i in 0 until notifications.length())
                {
//                    JsonArray내부 object 추출 => 가공 => mNotiList에 담음
                    mNotifiList.add(Notification.getNotificationFromJson(notifications.getJSONObject(i)))
                }

//                알림이 하나라도 있으면 알림을 어디까지 읽었는지 서버에 전송 => 알림확인 기능
//                handler에 null을 넣어, 할일이 없다고 명시
                ServerUtil.postRequestNotificationCheck(mContext, mNotifiList[0].id, null)

                runOnUiThread{
                    mNotificationAdapter.notifyDataSetChanged()
                }

            }

        })
    }
}