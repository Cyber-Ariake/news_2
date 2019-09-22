package com.example.news_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.eclipsesource.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.example.news_2.HttpUtil


class MainActivity : AppCompatActivity() {

//    val URL = "http://weather.livedoor.com/forecast/webservice/json/v1?city=400040" //サンプルとしてライブドアのお天気Webサービスを利用します
    val URL = "http://52.68.215.201/weather.json"
    var result = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getButton = findViewById(R.id.button) as Button
        getButton.setOnClickListener(object : View.OnClickListener {
            override
            fun onClick(view: View) {
                onParallelGetButtonClick()
            }
        })
    }

    fun onParallelGetButtonClick() = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()
        //Mainスレッドでネットワーク関連処理を実行するとエラーになるためBackgroundで実行
        async(Dispatchers.Default) { http.httpGET1(URL) }.await().let {
            //minimal-jsonを使って　jsonをパース
            val result = Json.parse(it).asObject()
            val textView = findViewById(R.id.text) as TextView
//            textView.setText(result.get("description").asObject().get("text").asString())
            textView.setText(result.get("pinpointLocations").asArray().get(0).asObject().get("name").asString())
        }
    }
}
