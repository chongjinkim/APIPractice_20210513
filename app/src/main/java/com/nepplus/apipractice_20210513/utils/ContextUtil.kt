package com.nepplus.apipractice_20210513.utils

import android.content.Context

class ContextUtil {

    companion object{//끌어다가 쓰기 편하게 하기 위해서 companion object안에 작성을 한다.

        private val prefName = "Daily10Minutes"

        private val AUTO_LOGIN = "AUTO_LOGIN"

        private val LOGIN_TOKEN = "LOGIN_TOKEN"


        fun setAutoLogin(context : Context, autoLogin : Boolean){

//메모장을 열어서 -> 변수에 담아두자.
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

            pref.edit().putBoolean(AUTO_LOGIN, autoLogin).apply()

        }

        fun getAutoLogin(context: Context) : Boolean{

            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

            return pref.getBoolean(AUTO_LOGIN, false) //한번도 한적 없으면 기본값 -> false

        }

        fun setLoginToken(context : Context, token : String) {

            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

            pref.edit().putString(LOGIN_TOKEN, token).apply()

        }

        fun getLoginToken(context : Context) : String{

            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

            return pref.getString(LOGIN_TOKEN, "")!!

        }
    }
}