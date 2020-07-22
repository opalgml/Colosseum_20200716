package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import kr.co.tjoeun.colosseum_20200716.datas.Side
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

//        버튼이 눌리면 할 일을 변수에 담아서 저장
//        TedPermission에서 권한별 할 일을 변수에 담아서 저장한것과 같은 논리
        val voteCode = View.OnClickListener{

//            it => View 형태 => 눌린 버튼을 담고있는 변수
            val clickedSideTag = it.tag.toString()

            Log.d("tag값 ", clickedSideTag)

//            눌린 버튼의 태그를 Int로 바꿔서
//            토론 주제의 진영 중 어떤 진영을 눌렀는지 가져오는 index로 활용
            val clickedSide = mTopic.sideList[clickedSideTag.toInt()]

            Log.d("투표하려는 진영 제목", clickedSide.title)

//            실제로 해당 진영에 투표
            ServerUtil.postRequestVote(mContext, clickedSide.id , object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

//                    서버는 변경된 결과가 어떻게 되는지 다시 내려줌
//                    이 응답에서 토론 진행현황을 다시 파싱
//                    => 화면에 반영
                    val data = json.getJSONObject("data")
                    val topic = json.getJSONObject("topic")

                    mTopic = Topic.getTopicFromJson(topic)

//                    화면에 mTopic의 데이터를 이용해서 반영
                    runOnUiThread {
                        setTopicDataToUi()
                    }

                }
            })


        }

//        두개의 투표하기 버튼이 눌리면 할 일을 모두 voteCode에 적힌 내용으로 처리
        voteToFirstSideBtn.setOnClickListener(voteCode)
        voteToSecondSideBtn.setOnClickListener(voteCode)

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
                    setTopicDataToUi()
                }

            }

        })
    }

//    화면에 mTopic 기반으로 데이터를 반영해주는 기능
    fun setTopicDataToUi()
    {
        topicTitleTxt.text = mTopic.title
        Glide.with(mContext).load(mTopic.imageUrl).into(topicImg)


//                    진영 정보도 같이 표시
        firstSideTitleTxt.text = mTopic.sideList[0].title
        SecondSideTitleTxt.text = mTopic.sideList[1].title

        firstSideTitleTxt.text = "${mTopic.sideList[0].voteCount}표"
        SecondSideTitleTxt.text = "${mTopic.sideList[1].voteCount}표"
    }
}