package com.nepplus.apipractice_20210513

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.nepplus.apipractice_20210513.utils.ContextUtil

class SplashActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

//        자동 로그인을 하는 상황인지 검사(질문)
//        사용자가 자동 로그인 체크를 했는지
//        저장된 토큰 값이 있는지(" ") 아닌지
//        둘다 만족을 하면 메인으로 이동

//        != 아니니? 무엇인가 있니?
        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed({

            val myIntent : Intent// -> 나중에 intent를 넣어준다

            if(ContextUtil.getAutoLogin(mContext) && ContextUtil.getLoginToken(mContext) != "")  {

                //자동 로그인을 해도 된다 MainActivity로 이동을 한다

                myIntent = Intent(mContext, MainActivity::class.java)
            }

            else{
//                로그인에서 로그인 화면으로 이동 -> LoginActiviy로 이동한다.
                myIntent = Intent(mContext, LoginActivity::class.java)
            }

            startActivity(myIntent)
            finish()

        },2500)

    }

}