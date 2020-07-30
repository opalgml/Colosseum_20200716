package kr.co.tjoeun.colosseum_20200716

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.tjoeun.colosseum_20200716.adapters.TopicAdatper
import kr.co.tjoeun.colosseum_20200716.datas.Topic
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val mTopicList = ArrayList<Topic>()

    lateinit var mTopicAdapter : TopicAdatper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        토론 주제를 누르면 => 상세 화면으로 이동한다.
        topicListView.setOnItemClickListener { parent, view, position, id ->

//            눌린 위치에 맞는 주제를 가져오자
            val clickedTopic = mTopicList[position]

//            상세화면으로 진입 => 클릭된 주제의 id값만 화면에 전달
            val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
            myIntent.putExtra("topicId", clickedTopic.id)
            startActivity(myIntent)

        }

    }

    override fun setValues() {

//        알림 버튼을 누르면 => 알림 목록 화면으로 이동
        notificationBtn.setOnClickListener {
//            이미지뷰도 => setOnClickListener 적용 가능
            val myIntent = Intent(mContext, NotificationListActivity::class.java)
            startActivity(myIntent)
        }

        getTopicListFromServer()

        mTopicAdapter = TopicAdatper(mContext, R.layout.topic_list_item, mTopicList)
        topicListView.adapter = mTopicAdapter

//        BaseActivity가 물려주는 알림 버튼을 화면에 보이도록 함
        notificationBtn.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
//        메인화면으로 돌아올때마다 서버에 읽지않은 알림이 몇개인지 요청
        getNotiCountFromServer()
    }

//    안읽은 알림의 갯수만 가져오는 API 호출
    fun getNotiCountFromServer(){
        ServerUtil.getRequestCount(mContext, object:ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {
                val data = json.getJSONObject("data")
                val unreadNotiCount = data.getInt("unread_noty_count")

//                안읽은 알림이 몇개인지에 따라 다른 UI 처리
                runOnUiThread {
                    if(unreadNotiCount == 0)
                    {
//                        빨간 동그라미 숨겨주기
                        notiCountTxt.visibility = View.GONE
                    }
                    else
                    {
//                        빨간 동그라미 보여주기 + 알림 수 보여주기
                        notiCountTxt.visibility = View.VISIBLE
                        notiCountTxt.text = unreadNotiCount.toString()
                    }
                }
            }

        })
    }


    fun getTopicListFromServer()
    {

        ServerUtil.getRequestMainInfo(mContext, object: ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")

//                topics 는 [] 임. => JSonArray로 추출해야함
                val topics = data.getJSONArray("topics")

//                topics 내부에는 JSONObject가 여러개 반복해서 존재
//                JSON을 들고 있는 배열 => JSONArray

//                for문 이용, topics 내부의 데이터를 하나씩 추출
                for(i in 0 until topics.length()) //for(i in 0..topics.length-1) 동일함.
                {
//                    topics 내부의 데이터를 JSONObject로 추출
                    val topicObj = topics.getJSONObject(i)

//                    topicObj => Topic 형태의 객체로 변환
                    val topic = Topic.getTopicFromJson(topicObj)

//                    변환된 객체를 목록에 추가
                    mTopicList.add(topic)
                }

//                for 문으로 주제 목록을 모두 추가하고 나면
//                리스트뷰의 내용이 바꼇다고 새로고침
                runOnUiThread {
                    mTopicAdapter.notifyDataSetChanged()
                }

            }

        })

    }
}