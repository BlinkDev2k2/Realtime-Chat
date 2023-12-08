package com.hoang.blink.realtimechat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class MessageAdapter(private val context: Context, id1: Short, private val data: ArrayList<Message>) : BaseAdapter() {
    private var id = id1
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
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            viewHolder = ViewHolder()
        Log.d("AA", data[position].id.toString() + "<>" + this.id.toString())
            if (data[position].id == this.id) {
                view = LayoutInflater.from(context).inflate(R.layout.item_my_message, parent, false)
                viewHolder.avt = view.findViewById(R.id.imgAvtUserMyMess)
                viewHolder.message = view.findViewById(R.id.tvNameUserMyMess)
                view.tag = viewHolder
            }
            else {
                view = LayoutInflater.from(context).inflate(R.layout.item_other_message, parent, false)
                viewHolder.avt = view.findViewById(R.id.imgAvtUserOtherMess)
                viewHolder.message = view.findViewById(R.id.tvNameUserOtherMess)
                view.tag = viewHolder
            }
        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.avt.setImageResource(MainActivity.saveAvtById[data[position].id.toInt() - 1])
        viewHolder.message.text = data[position].message
        return view!!
    }

    fun setData(id1: Short) {
        this.id = id1
    }

    companion object {
        internal class ViewHolder{
            internal lateinit var avt: ImageView
            internal lateinit var message: TextView
        }
    }
}