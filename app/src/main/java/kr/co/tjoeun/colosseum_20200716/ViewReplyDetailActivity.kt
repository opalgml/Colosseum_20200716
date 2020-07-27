package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import kotlinx.android.synthetic.main.reply_list_item.*
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

//    보려는 의견의 id는 여러 함수에서 공유 => 멤버변수로 생성하여 저장
    var mReplyId = 0

//    이 화면에서 보여줘야할 의견의 정보를 가진 변수 => 멤버변수
    lateinit var mReply : Reply
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reply_detail)

        setupEvents()
        setValues()
        getReplyFromServer()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

//        의견 리스트뷰에서 보내준 id 값을 멤버변수에 담아주자
        mReplyId = intent.getIntExtra("replyId", 0)

//        받아온 replyId에 맞는 의견 정보를 다시 불러온다.
        getReplyFromServer()

    }

    //서버에서 의견 정보 불러오기
    fun getReplyFromServer(){

        ServerUtil.getRequestReplyDetail(mContext, mReplyId, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")
                val replyObject = data.getJSONObject("reply")

                Log.d("flag1", "activity")
                mReply = Reply.getReplyFromJson(replyObject)

                runOnUiThread {
                    setReplyData()
                }
            }

        })

    }

    fun setReplyData(){

        textWriterTxt.text = mReply.writer.nickName
        selectedSideTxt.text = "(${mReply.selectedSide.title})"
        replyContentTxt.text = mReply.content
        writtenDateTimeTxt.text = TimeUtil.getTimeAgoFromCalendar(mReply.writtenDateTime)

    }
}