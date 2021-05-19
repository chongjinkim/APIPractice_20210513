package com.nepplus.apipractice_20210513.datas

import org.json.JSONObject
import java.io.Serializable

class Project(
        var id : Int,
        var title : String,
        var imageUrl : String,
        var description : String,
        var onGoingUsersConunt : Int,
        var proofMethod : String,
        var myLastStatus : String?) : Serializable {
//myLastStatus null일 수도 있으니까 ?를 달수도 있따.

    val tags = ArrayList<String>()

    //         보조생성자 추가 - Project()닫는거 만으로도 만들 수 있게
    constructor() : this(0, "", "", "", 0, "", null)

    //  기능을 추가하겠다 - json을 넣으면(input) 알아서 프로젝트로 변환을 해주는 기능(return) => 단순 기능, companion Ojbect를 이용하는 게 편하다
    companion object{

        fun getProjectFromJson(jsonObj : JSONObject) : Project {

            val project = Project()

//jsonObj에서 항목을 정보추출 -> Project의 하위 항목반영
            project.id = jsonObj.getInt("id")
            project.title = jsonObj.getString("title")
            project.imageUrl = jsonObj.getString("img_url")
            project.description = jsonObj.getString("description")

            project.onGoingUsersConunt = jsonObj.getInt("ongoing_users_count")
            project.proofMethod = jsonObj.getString("proof_method")

//            내 최종 도전 상태 -> null일 가능성도 있다.
//            null인 데이터를파싱하려고 하면 -> 에러처리 + 파싱중단
//            null인지 아닌지 확인? -> null이 아닐때만 파싱하자

            if(!jsonObj.isNull("my_last_status")){

                project.myLastStatus = jsonObj.getString("my_last_status")
            }


            val tagsArr = jsonObj.getJSONArray("tags")

            for(i in 0 until tagsArr.length()){

                val tagsObj = tagsArr.getJSONObject(i)

                val title = tagsObj.getString("title")

                project.tags.add(title)

            }

//최종적으로 완성된 프로젝트가 결과로 나간다.
            return project
        }

    }

}