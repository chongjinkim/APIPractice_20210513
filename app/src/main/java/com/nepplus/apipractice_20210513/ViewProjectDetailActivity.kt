package com.nepplus.apipractice_20210513

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.nepplus.apipractice_20210513.datas.Project
import com.nepplus.apipractice_20210513.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_project_detail.*
import org.json.JSONObject

class ViewProjectDetailActivity : BaseActivity() {

    lateinit var mProject : Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_project_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        viewProofBtn.setOnClickListener {

            val myIntent = Intent(mContext, ViewProofByDateActivity::class.java)

            myIntent.putExtra("project", mProject)

            startActivity(myIntent)

        }

        giveUpBtn.setOnClickListener {

//            서버에 포기 의사를 전달을 한다.

            ServerUtil.deletRequestGiveUpProject(mContext, mProject.id, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    val code = jsonObj.getInt("code")

                    if (code == 200) {

//서버에서 다시 정보를 불러온다.
//                        조회하러 다시 이동을 한다.
//                        다시 원래 서버를 보여준다.
                        ServerUtil.getRequestProjectDetail(mContext, mProject.id, object : ServerUtil.JsonResponseHandler{
                            override fun onResponse(jsonObj: JSONObject) {

                                val dataObj = jsonObj.getJSONObject("data")
                                val projectObj = dataObj.getJSONObject("project")

                                mProject = Project.getProjectFromJson(projectObj)

                                runOnUiThread {
                                    refreshDataToUi()
                                }

                            }


                        })

                    } else {

                        Toast.makeText(mContext, "포기 신청에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }

                }


            })
        }

        applyBtn.setOnClickListener {

            ServerUtil.postReuqestApplyProject(mContext, mProject.id, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    //성공시 응답으로 -> 새로 값이 반영된 프로젝트 정보를 JSONObject를 다시 내려준다
//                    새로 파싱해서 mProject를 갱신을 해야 한다.

                    val code = jsonObj.getInt("code")

                    if (code == 200) {

                        val dataObj = jsonObj.getJSONObject("data")
                        val projectObj = dataObj.getJSONObject("project")

                        mProject = Project.getProjectFromJson(projectObj)

                        runOnUiThread {
                            refreshDataToUi()
                        }

                    }
                    else {
                        runOnUiThread {
                            Toast.makeText(mContext, "참여 신청에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            })
        }



    }

    override fun setValues() {

        mProject = intent.getSerializableExtra("project") as Project
        refreshDataToUi()


    }

    fun refreshDataToUi(){

        Glide.with(mContext).load(mProject.imageUrl).into(projectImg)
        titleTxt.text = mProject.title
        descriptionTxt.text = mProject.description

        userCountText.text = "${mProject.onGoingUsersConunt}명"
        proofMethodTxt.text = mProject.proofMethod


//태그 목록은 몇개 일 지가 매번 다르다
//        빈 layout을 불러내서 -> 기존의 텍스트뷰 모두 삭제 -> 태그 갯수만큼 텍스트뷰 (코틀린에서) 추가

       tagListLayout.removeAllViews()

        for(tag in mProject.tags){

            Log.d("프로젝트태그", tag)

            val tagTextView = TextView(mContext)

            tagTextView.text = "#${tag}"

            tagTextView.setTextColor(Color.BLUE)

            tagListLayout.addView(tagTextView)

        }

//        내 마지막 도전상태가 ongoing이면 -> 포기하기 버튼만
//        그 외의 경우 -> 재 신청 가능

        if(mProject.myLastStatus == "ONGOING"){

            giveUpBtn.visibility = View.VISIBLE
            applyBtn.visibility = View.GONE
        }

        else{
            giveUpBtn.visibility = View.GONE
            applyBtn.visibility = View.VISIBLE
        }


    }
}