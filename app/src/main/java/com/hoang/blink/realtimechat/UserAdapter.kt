package com.hoang.blink.realtimechat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class UserAdapter(private val context: Context, arrUser: ArrayList<User>) : BaseAdapter() {
    private var data: ArrayList<User> = arrUser

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v: View? = convertView
        val viewHolder: ViewHolder
        if (v != null) {
           viewHolder = v.tag as ViewHolder
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
            viewHolder = ViewHolder()
            viewHolder.avt = v.findViewById(R.id.imgAvtUserListUser)
            viewHolder.name = v.findViewById(R.id.tvNameUserListUser)
            v.tag = viewHolder
        }
        viewHolder.avt.setImageResource(data[position].avt)
        viewHolder.name.text = data[position].name
        return v!!
    }

    fun setData(data2: ArrayList<User>) {
        data = data2
        notifyDataSetChanged()
    }

    companion object {
        class ViewHolder {
            internal lateinit var avt: ImageView
            internal lateinit var name: TextView
        }
    }
}