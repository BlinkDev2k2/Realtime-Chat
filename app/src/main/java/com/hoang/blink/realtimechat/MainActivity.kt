package com.hoang.blink.realtimechat

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var socket: Socket
    private lateinit var listViewMessage: ListView
    private lateinit var listViewUser: ListView
    private lateinit var edtTypeMessage: EditText
    private lateinit var btnAddUser: ImageButton
    private lateinit var btnSendMessage: ImageButton
    private lateinit var listUser: ArrayList<User>
    private lateinit var listMessage: ArrayList<Message>
    private lateinit var userAdapter: UserAdapter
    private lateinit var messageAdapter: MessageAdapter

    companion object {
        internal var id: Short = 0
        internal var idSender: Short = 0
        internal var myMessage: Short = 0
        internal val avt = intArrayOf(R.drawable.lisa, R.drawable.rose, R.drawable.jennie, R.drawable.jisoo)
        internal lateinit var saveAvtById: ArrayList<Int>
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewMessage = findViewById(R.id.listMessage)
        edtTypeMessage = findViewById(R.id.edtTypeMessage)
        btnAddUser = findViewById(R.id.btnAddUser)
        btnSendMessage = findViewById(R.id.btnSendMessage)
        listViewUser = findViewById(R.id.listUser)
        userAdapter = UserAdapter(this, ArrayList(0))

        try {
            listUser = ArrayList()
            listMessage = ArrayList()
            saveAvtById = ArrayList()
            socket = IO.socket("http://192.168.1.6:3000/")
            socket.connect()
            listViewUser.adapter = userAdapter
            messageAdapter = MessageAdapter(this, myMessage, listMessage)
            listViewMessage.adapter = messageAdapter
        } catch (e: URISyntaxException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

        socket.on("send_to_all_user") {
            id = 0
            listUser.clear()
            val objJson: JSONObject = it[0] as JSONObject
            val arrJson: JSONArray = objJson.getJSONArray("listUser")
            val len = arrJson.length() - 1
            saveAvtById.add(avt[Random.nextInt(4)])
            for (i in 0..len)
                listUser.add(User(++id, arrJson.getString(i), saveAvtById[i]))
            runOnUiThread { userAdapter.setData(listUser) }
        }

        socket.on("my_message") {
            val json = it[0] as JSONObject
            myMessage = json.getInt("id").toShort()
        }

        socket.on("send_id_sender") {
            val json = it[0] as JSONObject
            idSender = json.getInt("id").toShort()
        }

        socket.on("send_mess_to_all") {idSend ->
            val json = idSend[0] as JSONObject
            val mess = json.getString("mess")
            listMessage.add(Message(idSender, listUser[idSender.toInt() - 1].name + ": " + mess))
            runOnUiThread {
                messageAdapter.setData(myMessage)
                messageAdapter.notifyDataSetChanged()
            }
        }

        btnAddUser.setOnClickListener {
            val name = edtTypeMessage.text.toString().trim()
            if (name.isNotBlank()) {
                socket.on("send_result") {
                    val json = it[0] as JSONObject
                    val success = json.getBoolean("result")
                    runOnUiThread {
                        if (success) {
                            Toast.makeText(this, "Add new user success", Toast.LENGTH_SHORT).show()
                            btnAddUser.isEnabled = false
                        }
                        else {
                            Toast.makeText(this, "Cannot add new user", Toast.LENGTH_SHORT).show()
                            btnAddUser.isEnabled = true
                        }
                    }
                }
                socket.emit("register_new_user", name)
                edtTypeMessage.setText("")
            } else Toast.makeText(this, "Please type name user before add", Toast.LENGTH_SHORT).show()
        }

        btnSendMessage.setOnClickListener {
            val content =  edtTypeMessage.text.toString().trim()
            if (content.isNotBlank()) {
                socket.emit("client_send_mess", content)
                edtTypeMessage.setText("")
            }
        }
    }
}