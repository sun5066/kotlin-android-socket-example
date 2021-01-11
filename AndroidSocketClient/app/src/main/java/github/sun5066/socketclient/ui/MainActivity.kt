package github.sun5066.socketclient.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import github.sun5066.socketclient.R

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mTxtIP: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTxtIP = findViewById(R.id.txt_ip)
        findViewById<Button>(R.id.btn_server_conn).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, ClientActivity::class.java)
        intent.putExtra("ip", mTxtIP.text.toString())
        startActivity(intent)
    }
}
