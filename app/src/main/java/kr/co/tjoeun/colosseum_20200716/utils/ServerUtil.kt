package kr.co.tjoeun.colosseum_20200716.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class ServerUtil {
    
//  서버에 응답을 처리해주는 용도로 쓰는 인터페이스
    interface JsonResponseHandler {
        fun onResponse(json: JSONObject)
    }

//    JAVA의 static에 대응되는 개념 -> companion
    companion object {

//        호스트 주소를 저장해두는 변수
        private val BASE_URL = "http://15.165.177.142"

//    메인화면에서 쓸 토론주제 목록을 가져오는 기능
    fun getRequestMainInfo(context: Context, handler: JsonResponseHandler?) {
        val client = OkHttpClient()

//        GET, DELETE : query 에 파라미터 첨부
//        query => 주소 (url)에 직접 어떤 데이터가 담기는지 기록
//        주소를 적을 때 => 파라미터 첨부도 같이 진행
        val urlBuilder = "${BASE_URL}/v2/main_info".toHttpUrlOrNull()!!.newBuilder()

//        urlBuilder에 필요한 파라미터 첨부
        urlBuilder.addEncodedQueryParameter("device_token", "TEST기기토큰")
        urlBuilder.addEncodedQueryParameter("os", "Android")

//        모든 데이터가 담겼으면 주소를 완성해서 String 으로 저장
        val urlString = urlBuilder.build().toString()

//        실제 요청 정보를 request변수에 종합
        val request = Request.Builder()
            .url(urlString)
            .get()
            .header("X-Http-Token", ContextUtil.getLoginUserToken(context))
            .build()

//        완성된 요청 정보를 실제로 호출 => 응답 처리
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                        서버 연결 자체에 실패한경우
                }

                override fun onResponse(call: Call, response: Response) {
//                        연결은 성공해서, 서버가 응답을 내려줬을때 실행
//                        실제 서버가 내려준 응답 내용을 변수로 저장
                    val bodyStr = response.body?.string()

//                        응답 내용으로 Json 객체 생성
                    val json = JSONObject(bodyStr)

//                        최종적으로 가져온 내용을 로그로 출력
                    Log.d("서버 응답 내용", json.toString())

//                        handler 변수에 응답 처리 코드가 있으면 실행해주자
                    handler?.onResponse(json)
                }

            })
    }

//    토론 상세 정보 API를 호출하는 기능
    fun getRequestTopicDetail(context: Context, topicId : Int,  handler: JsonResponseHandler?) {
        val client = OkHttpClient()

    //        GET, DELETE : query 에 파라미터 첨부
    //        query => 주소 (url)에 직접 어떤 데이터가 담기는지 기록
    //        주소를 적을 때 => 파라미터 첨부도 같이 진행
        val urlBuilder = "${BASE_URL}/topic/${topicId}".toHttpUrlOrNull()!!.newBuilder()

    //        urlBuilder에 필요한 파라미터 첨부
        urlBuilder.addEncodedQueryParameter("order_type", "NEW")
        urlBuilder.addEncodedQueryParameter("page_num", "1")

    //        모든 데이터가 담겼으면 주소를 완성해서 String 으로 저장
        val urlString = urlBuilder.build().toString()

    //        실제 요청 정보를 request변수에 종합
        val request = Request.Builder()
            .url(urlString)
            .get()
            .header("X-Http-Token", ContextUtil.getLoginUserToken(context))
            .build()

    //        완성된 요청 정보를 실제로 호출 => 응답 처리
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
    //                        서버 연결 자체에 실패한경우
                }

                override fun onResponse(call: Call, response: Response) {
    //                        연결은 성공해서, 서버가 응답을 내려줬을때 실행
    //                        실제 서버가 내려준 응답 내용을 변수로 저장
                    val bodyStr = response.body?.string()

    //                        응답 내용으로 Json 객체 생성
                    val json = JSONObject(bodyStr)

    //                        최종적으로 가져온 내용을 로그로 출력
                    Log.d("서버 응답 내용", json.toString())

    //                        handler 변수에 응답 처리 코드가 있으면 실행해주자
                    handler?.onResponse(json)
                }

            })
    }

//        로그인API를 호출해주는 기능
//        1. 화면에서 어떤 데이터를 받아와야 하는지? -> email, name
        fun postRequestLogin(context: Context, email: String, pw: String, handler: JsonResponseHandler?) {

//            서버 통신 담당 변수 (클라이언트 역할 수행 변수)
             val client = OkHttpClient()

//            어느 주소로 가야하는지 저장
            val urlString = "${BASE_URL}/user"

//            서버가 가지고 갈 데이터를 FormBody를 이용해 담음
//            POST/PUT/PATCH가 같은 방식을 사용
            val formData = FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .build()

//            요청 정보를 종합하는 변수 Request 사용
//            Intent 만드는것과 비슷한 개념
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .build()

//            종합된 request를 이용해서 실제 API 호출 (-> Client가 처리)
//            받아올 응답도 같이 처리
            client.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
//                        서버 연결 자체에 실패한경우
                    }

                    override fun onResponse(call: Call, response: Response) {
//                        연결은 성공해서, 서버가 응답을 내려줬을때 실행
//                        실제 서버가 내려준 응답 내용을 변수로 저장
                        val bodyStr = response.body?.string()

//                        응답 내용으로 Json 객체 생성
                        val json = JSONObject(bodyStr)

//                        최종적으로 가져온 내용을 로그로 출력
                        Log.d("서버 응답 내용", json.toString())

//                        handler 변수에 응답 처리 코드가 있으면 실행해주자
                    handler?.onResponse(json)
                }

                })

        }

//    주제 진영 선택 투표하는 기능
        fun postRequestVote(context: Context, sideId: Int, handler: JsonResponseHandler?) {

//            서버 통신 담당 변수 (클라이언트 역할 수행 변수)
        val client = OkHttpClient()

//            어느 주소로 가야하는지 저장
        val urlString = "${BASE_URL}/topic_vote"

//            서버가 가지고 갈 데이터를 FormBody를 이용해 담음
//            POST/PUT/PATCH가 같은 방식을 사용
        val formData = FormBody.Builder()
            .add("side_id", sideId.toString()) // add시 String 값만 넣을 수 있음
            .build()

//            요청 정보를 종합하는 변수 Request 사용
//            Intent 만드는것과 비슷한 개념
        val request = Request.Builder()
            .url(urlString)
            .post(formData)
            .header("X-http-Token", ContextUtil.getLoginUserToken(context))
            .build()

//            종합된 request를 이용해서 실제 API 호출 (-> Client가 처리)
//            받아올 응답도 같이 처리
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                        서버 연결 자체에 실패한경우
                }

                override fun onResponse(call: Call, response: Response) {
//                        연결은 성공해서, 서버가 응답을 내려줬을때 실행
//                        실제 서버가 내려준 응답 내용을 변수로 저장
                    val bodyStr = response.body?.string()

//                        응답 내용으로 Json 객체 생성
                    val json = JSONObject(bodyStr)

//                        최종적으로 가져온 내용을 로그로 출력
                    Log.d("서버 응답 내용", json.toString())

//                        handler 변수에 응답 처리 코드가 있으면 실행해주자
                    handler?.onResponse(json)
                }

            })

    }
//     회원가입 호출 기능
        fun putRequestSignUp(context: Context, email: String, password:String, nickName: String, handler: JsonResponseHandler?) {

//            서버 통신 담당 변수 (클라이언트 역할 수행 변수)
        val client = OkHttpClient()

//            어느 주소로 가야하는지 저장
        val urlString = "${BASE_URL}/user"

//            서버가 가지고 갈 데이터를 FormBody를 이용해 담음
//            POST/PUT/PATCH가 같은 방식을 사용
        val formData = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .add("nick_name", nickName)
            .build()

//            요청 정보를 종합하는 변수 Request 사용
//            Intent 만드는것과 비슷한 개념
        val request = Request.Builder()
            .url(urlString)
            .put(formData)
            .build()

//            종합된 request를 이용해서 실제 API 호출 (-> Client가 처리)
//            받아올 응답도 같이 처리
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                        서버 연결 자체에 실패한경우
                }

                override fun onResponse(call: Call, response: Response) {
//                        연결은 성공해서, 서버가 응답을 내려줬을때 실행
//                        실제 서버가 내려준 응답 내용을 변수로 저장
                    val bodyStr = response.body?.string()

//                        응답 내용으로 Json 객체 생성
                    val json = JSONObject(bodyStr)

//                        최종적으로 가져온 내용을 로그로 출력
                    Log.d("서버 응답 내용", json.toString())

//                        handler 변수에 응답 처리 코드가 있으면 실행해주자
                    handler?.onResponse(json)
                }

            })

    }
    }
}