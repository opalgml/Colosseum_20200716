package kr.co.tjoeun.colosseum_20200716.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {

    companion object {

//        표시할 시간을 재료로 주면 => 상황에 맞는 String으로 변환해주는 기능

//        댓글 게시시간이 10초가 X => "방금전"으로 출력
//        1분 이내 => "?초천"으로 출력
//        1시간 이내 => "?분전"으로 출력
//        12시간 이내 => "?시간전"으로 출력
//        그 이상 => "?년?월?일 오전/오후 ?시 ?분" 으로 출력
//        ==> 최종 값을 전달

        private val dateFormat = SimpleDateFormat("yyyy년 M월 d일 a h시 m분")

        fun getTimeAgoFromCalendar(dateTime : Calendar) : String
        {

//            현재 시간이 몇시인지 알아야함 => Calendar 새로 만들면 현재 시간
            val now = Calendar.getInstance()
            
//            현재 시간에서 변수 파라미터 dateTime을 빼면 몇 ms 차이가 나는지 체크
//            30분 차이 : 30 * 60 * 1000 => 1,800,000 만큼 차이가 난다고 계산

            val msDiff = now.timeInMillis - dateTime.timeInMillis //차이가 ms만큼 계산

//            msDiff 값에 따른 if 처리
            if(msDiff < (10*1000))
            {
                return "방금 전"
            }
            else if(msDiff < (1*60*1000))
            {
                val second = msDiff / 1000
                return "${second}초 전"
            }
            else if(msDiff < (1*60*60*1000))
            {
                val min = msDiff / 1000 / 60
                return "${min}분 전"
            }
            else if(msDiff < (1*12*60*60*1000))
            {
                val hour = msDiff / 1000 / 60 / 60
                return "${hour}시간 전"
            }
            else
            {
                return msDiff.toString()
            }

            return ""
        }

    }

}