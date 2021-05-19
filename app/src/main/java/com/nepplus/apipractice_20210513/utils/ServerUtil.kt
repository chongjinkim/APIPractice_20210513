package com.nepplus.apipractice_20210513.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class ServerUtil {

    //화면에 입장에서 서버에 다녀오면 행동을 적는 가이드북
    //화면에 입장에서 서버에 다녀왔을때의 행동을 적는 가이드북 -> 행동 지침을 기록하는 개념 interface
    interface JsonResponseHandler{
        fun onResponse(jsonObj : JSONObject)
    }


    companion object{

        val HOST_URL = "http://15.164.153.174/"

        fun postRequestLogin(email : String, pw : String, handler : JsonResponseHandler){

            val urlString = "${HOST_URL}/user"

            val formData = FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .build()

            val request = Request.Builder()
                .url(urlString)
                .post(formData)//포스트 방식 필요한 데이터를 가지고 갈 수 있도록 한다.
                .build()


            //클라이언트로써 코드를 쉽게 작성할 수 있도록 도와주는 라이브러리
            val client = OkHttpClient()

            //request 요청사항을 정리를 해준다.
            //즉 Request정보를 처리 및 정리를 한다. client.newCall() -> Request로써 정보처리를 쉽게 도와주는 코드
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                    //서버 연결 자체를 실패
                    //데이터 요금 소진,  서버가 터짐등의 이유로 연결 자체를 실패
                    //

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

//                    받아낸 서버 응답 내용은 여기서 반영을 하는 것이 아니라, 화면에 UI에 반영하기 위해 사용한다.
//                    우리가 완성해낸 jsonObj를 화면에 넘겨주자
//                      파싱등의 처리는 액티비티에서 작성을 한다.
                    handler?.onResponse(jsonObj)


                }

            })


        }

        fun putReqeustSignUp(email : String, pw : String, nickname : String, handler: JsonResponseHandler){

            val urlString = "${HOST_URL}/user"

            val FormData= FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .add("nick_name", nickname)
                .build()

            val request = Request.Builder()
                .url(urlString)
                .put(FormData)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)
                }


            })


        }

        fun getRequestEmailCheck(email : String, handler : JsonResponseHandler){

//            어디로 어떤데이터 => url을 만들 때 전부 한꺼번에 적어야 한다.
//          주소로 적는게 복잡할 예정 -> /value2/value3 이런식으로 전부 이어 붙이게 된다
            val urlBuilder = "${HOST_URL}/email_check".toHttpUrlOrNull()!!.newBuilder()

            urlBuilder.addEncodedQueryParameter("email", email)

            val urlString = urlBuilder.build().toString()

            Log.d("가공된 url", urlString)

            val request = Request.Builder()
                    .url(urlString)
                    .get()
                    .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

//서버의 응답을 받아내는 데 성공을 한 경우
// 데이터 내부에 본문만 활용을 하게 된다. -> 서버내에 있는 데이터에 본문만을 활용을 하는 코드이다.
//                    val bodyString = response.body!!.string()

                    val bodyString = response.body!!.string()

                    val jsonObj =JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)
                }

            })



        }

        fun getRequestProjectList(context : Context, handler : JsonResponseHandler){

            val urlBuilder = "${HOST_URL}/project".toHttpUrlOrNull()!!.newBuilder()

//            urlBuilder.addEncodedQueryParameter("email", email)

            val urlString = urlBuilder.build().toString()

            val request = Request.Builder()
                    .url(urlString)
                    .get()
                    .header("X-Http-Token", ContextUtil.getLoginToken(context))
                    .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)

                }

            })
        }

        fun postReuqestApplyProject(context: Context, projectId : Int, handler : JsonResponseHandler){

                val urlString = "${HOST_URL}/project"

                val formData = FormBody.Builder()
                    .add("project_id", projectId.toString())
                    .build()

                val request = Request.Builder()
                        .url(urlString)
                        .post(formData)
                        .header("X-Http-Token", ContextUtil.getLoginToken(context))
                        .build()

                val client = OkHttpClient()
                client.newCall(request).enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    override fun onResponse(call: Call, response: Response) {

//                        서버의 응답을 받아내는 데 성공을 한 경우
//                        응답(response) - 내부의 본문(body)만 활용 , String형태로 저장

                        val bodyString = response.body!!.string()

//                        bodyString은 인코딩이 되어 있는 상태라 -> 사람이 읽기가 매우 어렵다
//                        bodyString -< jsonObject로 변환 시키면 읽을 수가 있게 된다.
                        val jsonObj = JSONObject(bodyString)

                        Log.d("서버응답", jsonObj.toString())

                        handler!!.onResponse(jsonObj)
                    }


                })


        }

        fun deletRequestGiveUpProject(context: Context, projectId: Int, handler : JsonResponseHandler){

            val urlBuilder = "${HOST_URL}/project".toHttpUrlOrNull()!!.newBuilder()

            urlBuilder.addEncodedQueryParameter("project_id", projectId.toString())

            val urlString = urlBuilder.build().toString()

            Log.d("서버응답", urlString)

            val request = Request.Builder()
                    .url(urlString)
                    .delete()//이미 주소에 미리 들어가 있기 때문에 delete에는 별다른 것을 안 넣어도 된다.
                    .header("X-Http-Token", ContextUtil.getLoginToken(context))
                    .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)
                }


            })
        }

        fun getRequestProjectDetail(context : Context, projectId: Int, handler : JsonResponseHandler){

            val urlBuilder = "${HOST_URL}/project/".toHttpUrlOrNull()!!.newBuilder()

//            몇 번 프로젝트로 볼건지 /1번 등으로 추가하자

            urlBuilder.addEncodedPathSegment(projectId.toString())

//            urlBuilder.addEncodedQueryParameter("email", email)

            val urlString = urlBuilder.build().toString()

            val request = Request.Builder()
                    .url(urlString)
                    .get()
                    .header("X-Http-Token", ContextUtil.getLoginToken(context))
                    .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)

                }

            })
        }

//        특정날짜의 인증글을 날짜별로 받아오기
        fun getRequestProjectProofList(context : Context, projectId: Int, date : String, handler : JsonResponseHandler){

            val urlBuilder = "${HOST_URL}/project".toHttpUrlOrNull()!!.newBuilder()

//            몇 번 프로젝트로 볼건지 /1번 등으로 추가하자
            urlBuilder.addEncodedPathSegment(projectId.toString())

           urlBuilder.addEncodedQueryParameter("proof_date", date)

            val urlString = urlBuilder.build().toString()

            val request = Request.Builder()
                    .url(urlString)
                    .get()
                    .header("X-Http-Token", ContextUtil.getLoginToken(context))
                    .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)

                }

            })
        }




    }
}



