package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_reply.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import kotlinx.android.synthetic.main.re_reply_list_item.*
import kr.co.tjoeun.colosseum_20200716.adapters.ReReplyAdapter
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

//    보려는 의견의 id는 여러 함수에서 공유 => 멤버변수로 생성하여 저장
    var mReplyId = 0

//    이 화면에서 보여줘야할 의견의 정보를 가진 변수 => 멤버변수
    lateinit var mReply : Reply

//    의견에 달린 답글들을 저장할 목록
    val mReReplyList = ArrayList<Reply>()
    lateinit var mReReplyAdapter : ReReplyAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reply_detail)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        //대댓글 달기 버튼 클릭
        reReplyBtn.setOnClickListener {

            val inputContent = reReplyEdt.text.toString()
            if(inputContent.length < 5) {
                Toast.makeText(mContext, "최소 5글자 이상은 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ServerUtil.postRequestReReply(mContext, mReplyId, inputContent, object: ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

                    runOnUiThread {
                        val message = json.getString("message")
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

//                        입력한 내용을 다시 빈칸으로 돌려주자.
                        contentEdt.setText("")
                    }
                }

            })
        }
    }

    override fun setValues() {

//        의견 리스트뷰에서 보내준 id 값을 멤버변수에 담아주자
        mReplyId = intent.getIntExtra("replyId", 0)

//        받아온 replyId에 맞는 의견 정보를 다시 불러온다.
        getReplyFromServer()

        mReReplyAdapter = ReReplyAdapter(mContext, R.layout.re_reply_list_item, mReReplyList)
        reReplyListView.adapter = mReReplyAdapter
    }

    //서버에서 의견 정보 불러오기
    fun getReplyFromServer(){

        ServerUtil.getRequestReplyDetail(mContext, mReplyId, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")
                val replyObject = data.getJSONObject("reply")

                mReply = Reply.getReplyFromJson(replyObject)

//                답글 목록은 누적되면 안됨 => 다 비워주고 다시 파싱

//                대댓글 정보
                val replies = replyObject.getJSONArray("replies")
                for(i in 0 until replies.length())
                {
//                    reReply 내부 정보를 추출
                    val reReplyObj = replies.getJSONObject(i)
                    val reReply = Reply.getReplyFromJson(reReplyObj)
                    mReReplyList.add(reReply)
                }

                runOnUiThread {
                    setReplyData()
//                    답글 목록이 불러지면 새로 반영
                    mReReplyAdapter.notifyDataSetChanged()

                    reReplyListView.smoothScrollToPosition(mReReplyList.size-1)
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