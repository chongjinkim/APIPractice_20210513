package com.nepplus.apipractice_20210513.datas

import org.json.JSONObject

class Proof {

//    멤버변수들만 추가, 생성자 커스터마이징 x

    var id = 0
    var content = ""



    companion object{

        fun getProofFromJson(jsonObj : JSONObject) : Proof {

            val proof = Proof()

//            json항목들
            proof.id = jsonObj.getInt("id")
            proof.content = jsonObj.getString("content")

            return proof

        }
    }

}