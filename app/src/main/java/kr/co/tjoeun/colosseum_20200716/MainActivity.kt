package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun setupEvents() {

        loginBtn.setOnClickListener {
//            1.입력한 아이디, 비밀번호를 받아옴
            val inputEmail = emailEdt.text.toString()
            val inputPw = pwEdt.text.toString()

//            2. 서버에 전달해주고, 응답처리
            ServerUtil.postRequestLogin(mContext, inputEmail, inputPw, null) //전달만 처리

        }

    }

    override fun setValues() {

    }
}