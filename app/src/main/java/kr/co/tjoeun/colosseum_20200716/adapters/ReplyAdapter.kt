package kr.co.tjoeun.colosseum_20200716.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kr.co.tjoeun.colosseum_20200716.R
import kr.co.tjoeun.colosseum_20200716.ViewReplyDetailActivity
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil
import org.json.JSONObject
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

//        좋아요/싫어요 눌렀을 때 화면 버튼 backGround 변경
        if(data.myLike)
        {
            likeBtn.setTextColor(mContext.resources.getColor(R.color.naverRed))
            likeBtn.setBackgroundResource(R.drawable.red_border_box)
        }
        else
        {
            likeBtn.setTextColor(mContext.resources.getColor(R.color.textGray)) // 리스트뷰의 재사용성때문에 -> else 에도 조건을 지정해서 초기화해준다.
            likeBtn.setBackgroundResource(R.drawable.gray_border_box)
        }

        if(data.myDisLike)
        {
            dislikeBtn.setTextColor(mContext.resources.getColor(R.color.naverBlue))
            dislikeBtn.setBackgroundResource(R.drawable.blue_border_box)
        }
        else
        {
            dislikeBtn.setTextColor(mContext.resources.getColor(R.color.textGray))
            dislikeBtn.setBackgroundResource(R.drawable.gray_border_box)
        }
        
//        답글 버튼이 눌리면 => 의견 상세 화면으로 진입
        replyBtn.setOnClickListener {

            val myIntent = Intent(mContext, ViewReplyDetailActivity::class.java)

//            몇번 의견에 대한 상세를 보고 싶은지 id만 넘김
//            해당 화면에서 다시 서버를 통해 데이터를 받아옴.
            myIntent.putExtra("replyId", data.id)

//            startActiviy 함수는 AppComatActivity 가 내려주는 기능.
//            Adapter는 액티비티가 아니므로, startActivity 기능을 내려주지 않는다.
//            mContext 변수가, 어떤 화면이 리스트뷰를 뿌리는지 들고 있음. => mContext.startActivity 를 이용하자!
            mContext.startActivity(myIntent)

        }

//        의견에 대한 좋아요 / 싫어요 버튼 클릭 이벤트
        likeBtn.setOnClickListener {
            ServerUtil.postRequestReplyLikeOrDislike(mContext, data.id, true, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

//                    변경된 좋아요 / 싫어요 갯수를 파악해서 버튼 문구 반영
//                    => 목록에 뿌려지는 data의 좋아요 / 싫어요 갯수를 변경

                    val dataObj = json.getJSONObject("data")
                    val replyObj = dataObj.getJSONObject("reply")

                    val reply = Reply.getReplyFromJson(replyObj)
                    data.likeCount = reply.likeCount
                    data.dislikeCount = reply.dislikeCount

//                    버튼 backGround 반영
                    data.myLike = reply.myLike
                    data.myDisLike = reply.myDisLike

//                    data 값 변경 => 리스트뷰 구성하는 목록 변경 => 어탭터.notifyDataSetChanged 실행
//                    이 코드는 어댑터 내부 => notifyDataSetChanged 내장되어있음 => 호출해서 처리

//                    새로고침도 UI 변경됨 => runOnUiThread 등으로 UI 쓰레드가 처리하도록 해아함
//                    어댑터는 runOnUiThread 기능 내장 X
//                    => Haneler 이용 -> UI 쓰레드에 접근
                    val uiHandler = Handler(Looper.getMainLooper())
                    uiHandler.post{ //지연시키는경우 postDelayed 사용
                        notifyDataSetChanged()
//                    서버가 알려주는 메시지를 Toast로 출력
                        val message = json.getString("message")
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }


                }

            })
        }

        dislikeBtn.setOnClickListener {
            ServerUtil.postRequestReplyLikeOrDislike(mContext, data.id, false, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

//                    변경된 좋아요 / 싫어요 갯수를 파악해서 버튼 문구 반영
//                    => 목록에 뿌려지는 data의 좋아요 / 싫어요 갯수를 변경

                    val dataObj = json.getJSONObject("data")
                    val replyObj = dataObj.getJSONObject("reply")

                    val reply = Reply.getReplyFromJson(replyObj)
                    data.likeCount = reply.likeCount
                    data.dislikeCount = reply.dislikeCount

//                    버튼 backGround 반영
                    data.myLike = reply.myLike
                    data.myDisLike = reply.myDisLike

//                    data 값 변경 => 리스트뷰 구성하는 목록 변경 => 어탭터.notifyDataSetChanged 실행
//                    이 코드는 어댑터 내부 => notifyDataSetChanged 내장되어있음 => 호출해서 처리

//                    새로고침도 UI 변경됨 => runOnUiThread 등으로 UI 쓰레드가 처리하도록 해아함
//                    어댑터는 runOnUiThread 기능 내장 X
//                    => Haneler 이용 -> UI 쓰레드에 접근
                    val uiHandler = Handler(Looper.getMainLooper())
                    uiHandler.post{ //지연시키는경우 postDelayed 사용
                        notifyDataSetChanged()

//                    서버가 알려주는 메시지를 Toast로 출력
                        val message = json.getString("message")
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }


                }

            })
        }

        return row
    }

}