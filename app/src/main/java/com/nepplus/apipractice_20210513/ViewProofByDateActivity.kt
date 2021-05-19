package com.nepplus.apipractice_20210513

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import com.nepplus.apipractice_20210513.adapters.ProofAdapter
import com.nepplus.apipractice_20210513.datas.Project
import com.nepplus.apipractice_20210513.datas.Proof
import com.nepplus.apipractice_20210513.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_proof_by_date.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ViewProofByDateActivity : BaseActivity() {

    lateinit var mProject : Project

//    현재 식나이 그때그때 기록이 된다.
    val mSelectedDate = Calendar.getInstance()

    val mProofList = ArrayList<Proof>()

    lateinit var mProofAdapter : ProofAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_proof_by_date)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        selectDateBtn.setOnClickListener {

//            날짜가 선택되면 어떻게 할지를 변수에 저장한다

            val dateSetListener = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

//                    선택된 년/월/일이 변수에 담겨 있다. => 텍스트뷰에 반영이 된다.

                    Log.d("선택된날짜", "${year}년 ${month}월 ${dayOfMonth}일")

//                    선택일자를 멤버변수에 저장
                    mSelectedDate.set(year, month, dayOfMonth)

//                    SimpleDateFormat 이용, 날짜 -> String양식 가공
//                   2020년 5월 3일의 양식으로 출력
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                    dateTxt.text = simpleDateFormat.format(mSelectedDate.time)

//                    서버에서 선택된 날짜의 인증글을 받아야 한다.
                    getProofListByDate()



                }


            }
//            실제로 달력 띄우기(alertdialog와 유사)
        val datePickerDialog = DatePickerDialog(mContext, dateSetListener,
                mSelectedDate.get(Calendar.YEAR),
                mSelectedDate.get(Calendar.MONTH),
                mSelectedDate.get(Calendar.DAY_OF_MONTH))
                datePickerDialog.show()

        }
    }

    override fun setValues() {

        mProject = intent.getSerializableExtra("project") as Project
        mProofAdapter = ProofAdapter(mContext, R.layout.proof_list_item, mProofList)
        proofListView.adapter = mProofAdapter




    }

    //서버에서 선택된 날짜의 글을 받아와 주는 함수이다.
    fun getProofListByDate(){

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val dateStr = sdf.format(mSelectedDate.time)

        ServerUtil.getRequestProjectProofList(mContext, mProject.id, dateStr, object : ServerUtil.JsonResponseHandler{

            override fun onResponse(jsonObj: JSONObject) {

                val dataObj = jsonObj.getJSONObject("data")
                val projectObj = dataObj.getJSONObject("project")

                val proofsArr = projectObj.getJSONArray("proofs")


                for(i in 0 until proofsArr.length()){

//                    인증글 정보를 json을 가지고 proof형태로 객체로 변환 -> mProofList에 추가를 시켜준다.

                    mProofList.add(Proof.getProofFromJson(proofsArr.getJSONObject(i)))

                }
//나중에 게시글 목록을 불러왔따 -> 리스트 뷰가 변경-> 어댑터 새로고침
//                데이터 - 새로고침 notifyDataSetChanged()
                runOnUiThread {
                    mProofAdapter.notifyDataSetChanged()
                }

            }


        })
    }
}