package com.nepplus.apipractice_20210513.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.nepplus.apipractice_20210513.R
import com.nepplus.apipractice_20210513.datas.Project

class ProjectAdapter(
        val mContext : Context,
        val resId : Int,
        val mList : ArrayList<Project>) : ArrayAdapter<Project>(mContext, resId, mList) {

        val inflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView

        //재활용이 꼭 필요할 때만 활용을 한다!!
        if(tempRow == null){
            tempRow = inflater.inflate(R.layout.project_list_item, null)
        }

        val row = tempRow!!

        val projectData = mList[position]

        val projectBackgroundImg = row.findViewById<ImageView>(R.id.projectBackgroundImg)
        val profileTitleTxt = row.findViewById<TextView>(R.id.profileTitleTxt)

        profileTitleTxt.text = projectData.title
        Glide.with(mContext).load(projectData.imageUrl).into(projectBackgroundImg)


        return row
    }
}