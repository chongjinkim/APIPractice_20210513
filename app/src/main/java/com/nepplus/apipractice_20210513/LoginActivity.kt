package com.nepplus.apipractice_20210513

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.nepplus.apipractice_20210513.utils.ContextUtil
import com.nepplus.apipractice_20210513.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        //체크박스 여부가 저장이 되면은
        //자동으로 어떻게 바뀌는지 볼것

        autoLoginCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

            ContextUtil.setAutoLogin(mContext, isChecked)


        }

        singUpBtn.setOnClickListener {

            val myIntent = Intent(mContext,SignUpActivity::class.java)

            startActivity(myIntent)

        }

        loginBtn.setOnClickListener {

            val inputEmail = emailEdt.text.toString()
            val inputPassword = passwordEdt.text.toString()

            ServerUtil.postRequestLogin(inputEmail, inputPassword, object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObj: JSONObject) {

                        val codeNum = jsonObj.getInt("code")

                        if (codeNum == 200) {

//로그인에 성공한 경우를 입력을 한다
                            val dataObj = jsonObj.getJSONObject("data")
                            val userObj = dataObj.getJSONObject("user")

                            val nickname = userObj.getString("nick_name")


                            val token = dataObj.getString("token")

                            ContextUtil.setLoginToken(mContext, token)

                            runOnUiThread {

                                Toast.makeText(mContext, "${nickname}님 환영합니다.", Toast.LENGTH_SHORT).show()

                                val myIntent = Intent(mContext, MainActivity::class.java)

                                startActivity(myIntent)

                                finish()
                            }


                        } else {

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

        //        Contextutl로 저장해둔 자동 로그인여부를 꺼내서 => 체크박스에 반영.
        autoLoginCheckBox.isChecked = ContextUtil.getAutoLogin(mContext)

    }




}