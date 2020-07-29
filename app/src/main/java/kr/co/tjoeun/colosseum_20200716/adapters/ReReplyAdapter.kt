package kr.co.tjoeun.colosseum_20200716.adapters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import kr.co.tjoeun.colosseum_20200716.R
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil
import org.json.JSONObject

class ReReplyAdapter(val mContext: Context, resId : Int, val mList:List<Reply>): ArrayAdapter<Reply>(mContext, resId, mList) {
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
        val likeBtn = row.findViewById<Button>(R.id.likeBtn)
        val disLikeBtn = row.findViewById<Button>(R.id.dislikeBtn)

        writerNickNameTxt.text = data.writer.nickName
        selectedSideTitleTxt.text = data.selectedSide.title
        contentTxt.text = data.content
        replyWriteTime.text = TimeUtil.getTimeAgoFromCalendar(data.writtenDateTime)
        likeBtn.text = "좋아요 ${data.likeCount}"
        disLikeBtn.text = "싫어요 ${data.dislikeCount}"

//        좋아요 / 싫어요 여부에 따른 색 설정
        if(data.myLike)
        {
            likeBtn.setBackgroundResource(R.drawable.red_border_box)
            likeBtn.setTextColor(mContext.resources.getColor(R.color.naverRed))
        }
        else
        {
            likeBtn.setBackgroundResource(R.drawable.gray_border_box)
            likeBtn.setTextColor(mContext.resources.getColor(R.color.textGray))
        }

        if(data.myDisLike)
        {
            disLikeBtn.setBackgroundResource(R.drawable.blue_border_box)
            disLikeBtn.setTextColor(mContext.resources.getColor(R.color.naverBlue))
        }
        else
        {
            disLikeBtn.setBackgroundResource(R.drawable.gray_border_box)
            disLikeBtn.setTextColor(mContext.resources.getColor(R.color.textGray))
        }

//        좋아요 / 싫어요 모두 실행하는 코드는 동일함
//        서버에 true/false 어떤 값을 보내는지만 다름
//        두개의 버튼이 눌리면 할 일(object : ??)을 변수에 담아두고 버튼에게 붙여만 주자
        val sendLikeOrDislikeCode = View.OnClickListener{
//            서버에 좋아요 / 싫어요 중 하나를 보냄
//            it 에 달린 태그값을 Boolean 으로 변환해서 좋아요/싫어요 구별
            ServerUtil.postRequestReplyLikeOrDislike(mContext, data.id, it.tag.toString().toBoolean(), object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
                    val dataObj = json.getJSONObject("data")
                    val reply = Reply.getReplyFromJson(dataObj.getJSONObject("reply"))

                    data.likeCount = reply.likeCount
                    data.dislikeCount = reply.dislikeCount
                    data.myLike = reply.myLike
                    data.myDisLike = reply.myDisLike

                    val uiHandler = Handler(Looper.getMainLooper())

                    uiHandler.post {
                        notifyDataSetChanged()
                    }

                }

            })
        }

//        좋아요/싫어요 버튼이 클릭되면 sendLikeOrDislike 내부 코드를 실행하게 하자
        likeBtn.setOnClickListener(sendLikeOrDislikeCode)
        disLikeBtn.setOnClickListener(sendLikeOrDislikeCode)

        return row
    }


}