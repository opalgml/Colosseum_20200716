package kr.co.tjoeun.colosseum_20200716

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

abstract class BaseActivity :AppCompatActivity() {

    val mContext = this

    abstract fun setupEvents()
    abstract fun setValues()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        BaseActivity를 상속받는 모든 Activity 는, onCreate에서 액션바 설정을 한다.
//        액션바가 있는지 확인하고 실행 (Manifest 파일에서, 스플래쉬화면에서 NoActionBar 옵션으로 넣었기 때문에 전체 화면에 적용하면 널포인트 에러 발생함
        supportActionBar?.let {
            setCustomActionBar()   
        }
    }

//    액션바를 커스텀으로 바꿔주는 기능
    fun setCustomActionBar(){
//        액션바가 절대 널이 아니라고 별개의 변수에 담음
        val myActionBar = supportActionBar!!
//            액션바를 커스텀 액션바를 쓸 수 있도록 셋팅
        myActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        myActionBar.setCustomView(R.layout.custom_action_bar)

//      액션바 뒤의 기본색 제거 -> 액션바를 가진 ToolBar의 좌우 여백을 0으로 설정
        val parentToolBar = myActionBar.customView.parent as Toolbar
        parentToolBar.setContentInsetsAbsolute(0,0)

    }
}