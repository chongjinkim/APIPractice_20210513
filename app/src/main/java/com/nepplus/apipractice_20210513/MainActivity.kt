package com.nepplus.apipractice_20210513

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.nepplus.apipractice_20210513.adapters.ProjectAdapter
import com.nepplus.apipractice_20210513.datas.Project
import com.nepplus.apipractice_20210513.utils.ContextUtil
import com.nepplus.apipractice_20210513.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : BaseActivity() {

    val mProjects = ArrayList<Project>()

    lateinit var mProjectAdapter : ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        projectListView.setOnItemClickListener { parent, view, position, id ->

            val clickedProject = mProjects[position]

            val myIntent = Intent(mContext, ViewProjectDetailActivity::class.java)

            myIntent.putExtra("project", clickedProject)

            startActivity(myIntent)
        }

        logOutBtn.setOnClickListener {

//            정말 로그아웃 -> alertDialog
//            로그아웃 할 때는 보통 -> alertDialog를 이용을 하면 된다.

            val alert = AlertDialog.Builder(mContext)
                alert.setTitle("로그아웃")
                alert.setMessage("로그아웃 하시겠습니까")
                alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

//로그인 -> 1. 아이디와 비밀번호를 전달을 한다. 2. 회원이 맞는 지 검사를 한다 3. 성공 시에 토큰 값을 전달을 한다 -> 어플리케이션에서 토큰 값을 저장을 한다.
//로그아웃 -> 기기에 저장된 토큰값을 삭제를 한다.

                    ContextUtil.setLoginToken(mContext, "")

                    val myIntent = Intent(mContext, LoginActivity::class.java)

                    startActivity(myIntent)

                    finish()

                })

                alert.setNegativeButton("확인", null)
                alert.show()
        }

    }

    override fun setValues() {

 //서버에서 요청 > 보여줄만한 프로젝트 목록이 어떤것이 있는지 > 받아서  > Project형태로 변환해서 mProject로 변환
      getProjectListFromServer()

      mProjectAdapter = ProjectAdapter(mContext, R.layout.project_list_item, mProjects)
      projectListView.adapter = mProjectAdapter

    }

    fun getProjectListFromServer(){

//        실제로 서버에서 받아오는 기능만 수행을 한다.
        //서버에서 => 보여줄 프로젝트 목록이 어떤것들이 있는지 받아서 => project()형태로 변환해서 => mProjects에 하나하나 추가

        ServerUtil.getRequestProjectList(mContext, object : ServerUtil.JsonResponseHandler{

            override fun onResponse(jsonObj: JSONObject) {

                val dataObj = jsonObj.getJSONObject("data")

                val projectsArr = dataObj.getJSONArray("projects")

                for(i in 0 until projectsArr.length()){

                val projectObj = projectsArr.getJSONObject(i)

//                    project클래스에 - 보조생성자를 추가.
//                    project클래스에 함수(기능) 추가. 적당한 jsonObj를 넣으면 project형태로 변환을 시킨다.
                val project = Project.getProjectFromJson(projectObj)

                mProjects.add(project)

                }

                runOnUiThread {

                    mProjectAdapter.notifyDataSetChanged()
                }

            }
        })


    }
}