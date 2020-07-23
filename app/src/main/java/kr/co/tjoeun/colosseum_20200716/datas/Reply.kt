package kr.co.tjoeun.colosseum_20200716.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Reply {

    var id = 0
    var content = ""
    lateinit var writer : User
    lateinit var selectedSide : Side

//    의견이 작성된 시간을 담는 변수
    val writtenDateTime = Calendar.getInstance()

    companion object {
        
//        jsonObject 하나를 넣으면 => 의견 내용을 파싱해서 Reply로 리턴하는 기능
        fun getReplyFromJson(json: JSONObject) : Reply{
    
            val r = Reply()
    
            r.id = json.getInt("id")
            r.content = json.getString("content")
            
//            작성자 / 선택진영 => JSONObject를 받아서 곧바로 대입
            r.writer = User.getUserFromJson(json.getJSONObject("user"))
            r.selectedSide = Side.getSideFromJson(json.getJSONObject("selected_side"))

//            작성일시를 서버가 주는 내용을 분석해서 대입
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

//            서버가 주는 내용을 변수로 저장
            val createdAtString = json.getString("created_at")

//            멤버변수인 Calendar 변수에게 데이터를 적용
            r.writtenDateTime.time = sdf.parse(createdAtString)

//            핸드폰의 시간대와 서버 시간대 "시차"를 구해서, 작성 일시의 시간값을 조정

//          1. 휴대폰의 시간대가 어디 시간대인지 변수로 저장
            val myPhoneTimeZone = r.writtenDateTime.timeZone // 한국시간대로 설정

//          2. 서버와 몇시간 차이가 나는지 변수로 저장 => 밀리초까지 계산된 시차 => 시간단위로 변경해줘야함.
            val timeOffset = myPhoneTimeZone.rawOffset / 1000 / 60 / 60

//          3. 게시글 작성시간을 timeOffset만큼 시간값을 더해준다.
            r.writtenDateTime.add(Calendar.HOUR, timeOffset)
    
            return r
    
        }
        
    }

}