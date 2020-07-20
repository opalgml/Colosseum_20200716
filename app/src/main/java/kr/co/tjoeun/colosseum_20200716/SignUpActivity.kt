package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    override fun setupEvents() {

//        비밀번호 입력 내용 변경 이벤트 처리
        passwordEdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

//                내용 변경 완료된 시점에 실행
                Log.d("비밀번호 입력 > ", s.toString())

//                입력 글자 길이 확인
                val tempPw = passwordEdt.text.toString()
                if(tempPw.isEmpty())
                {
                    passwordCheckResultTxt.text = "비밀번호를 입력해 주세요."
                }
                else if(tempPw.length < 8)
                {
                    passwordCheckResultTxt.text = "비밀번호가 너무 짧습니다."
                }
                else
                {
                    passwordCheckResultTxt.text = "사용해도 좋은 비밀번호 입니다."
                }
            }

        })
    }

    override fun setValues() {
    }


}