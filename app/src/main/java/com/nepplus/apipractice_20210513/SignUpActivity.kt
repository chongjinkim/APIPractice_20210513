package com.nepplus.apipractice_20210513

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.nepplus.apipractice_20210513.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        이메일 입력칸의 내용 변경 -> 무조건 다시 검사하도록 문구 변경

       emailEdt.addTextChangedListener(object : TextWatcher{
           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

           }

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
              emailCheckResultTxt.text = "다른 이메일을 선택을 해 주세요"
           }

           override fun afterTextChanged(s: Editable?) {

           }

       })

        emailCheckBtn.setOnClickListener {

            val inputEmail = emailEdt.text.toString()

            ServerUtil.getRequestEmailCheck(inputEmail, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    val codeNum = jsonObj.getInt("code")

                    if (codeNum == 200) {

                        runOnUiThread {
                            emailCheckResultTxt.text = "사용해도 좋은 이메일입니다."
                        }

                    } else {

                        val message = jsonObj.getString("message")

                        runOnUiThread {
                            emailCheckResultTxt.text = "이미 사용중인 이메일입니다."
                        }

                    }


                }

            })


        }

        signUpBtn.setOnClickListener {

            val inputEmail = emailEdt.text.toString()
            val inputPassword = passwordEdt.text.toString()
            val inputNickname = nicknameEdt.text.toString()

            ServerUtil.putReqeustSignUp(inputEmail, inputPassword, inputNickname, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    val codeNum = jsonObj.getInt("code")

                    if (codeNum == 200) {
                        runOnUiThread {
                            Toast.makeText(mContext, "회원가입을 환영합니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    }
                    else {
                        val message = jsonObj.getString("message")
                        runOnUiThread {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }


                    }
                }

            })


        }

    }

    override fun setValues() {

    }
}