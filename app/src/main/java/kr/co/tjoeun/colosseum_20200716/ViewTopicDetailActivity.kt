package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import kr.co.tjoeun.colosseum_20200716.datas.Topic
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

//    메인화면에서 넘겨준 주제id 저장
    var mTopicId = 0 //일단 Int 형으로 처리

//    서버에서 받아오는 토론 정보를 저장할 멤버변수
    lateinit var mTopic : Topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_topic_detail)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

//        메인에서 넘겨준 id값을 멤버변수에 저장
//        만약 0값을 받아온 경우 => error 로 처리
        mTopicId = intent.getIntExtra("topicId", 0)

        if(mTopicId == 0)
        {
            Toast.makeText(mContext, "주제 상세id에 문제가 있습니다", Toast.LENGTH_SHORT).show()
        }

//        서버에서 토론 주제에 대한 상세 진행 상황 가져오기
        getTopicDetailFromServer()

    }

    fun getTopicDetailFromServer()
    {
        ServerUtil.getRequestTopicDetail(mContext, mTopicId, object :ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")

                val topicObj = data.getJSONObject("topic")

//                서버에서 내려주는 주제 정보를
//                Topic 객체로 변환해서 멤버변수에 저장
                mTopic = Topic.getTopicFromJson(topicObj)

//                화면에 토론 관련 정보 표시
                runOnUiThread {

                    topicTitleTxt.text = mTopic.title
                    Glide.with(mContext).load(mTopic.imageUrl).into(topicImg)

                    
//                    진영 정보도 같이 표시
                    firstSideTitleTxt.text = mTopic.sideList[0].title
                    SecondSideTitleTxt.text = mTopic.sideList[1].title

                    firstSideTitleTxt.text = "${mTopic.sideList[0].voteCount}표"
                    SecondSideTitleTxt.text = "${mTopic.sideList[1].voteCount}표"
                }

            }

        })
    }
}