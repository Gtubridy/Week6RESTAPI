package edu.du.week6restapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private lateinit var service: SkiResortService
    private lateinit var gson: Gson
    private lateinit var requestText: TextView
    private lateinit var responseText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/Gtubridy/SkiResorts/")
            .build()

        service = retrofit.create(SkiResortService::class.java)
        gson = GsonBuilder().setPrettyPrinting().create()

        requestText = findViewById(R.id.requestView)
        responseText = findViewById(R.id.responseView)

        findViewById<Button>(R.id.getButton).setOnClickListener {
            makeCall {
                if (TextUtils.isEmpty(requestText.text)){
                    service.getResorts()
                } else {
                    service.getResort(requestText.text.toString())
                }
            }
        }

        findViewById<Button>(R.id.postButton).setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("id", "3")
            jsonObject.put("Name", "Brighton")
            jsonObject.put("Elevation", "9500")
            jsonObject.put("Verticle Feet", "1900")
            jsonObject.put("Average Annual Snowfall", "400in")
            jsonObject.put("Chairlift Count", "19")
            jsonObject.put("Average Daily Personnel", "8000")
            makeCall {
                service.createResort(jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            }
        }

        findViewById<Button>(R.id.putButton).setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("id", "1")
            jsonObject.put("Name", "Solitude")
            makeCall {
                service.updateResort(jsonObject.getString("id"), jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            }
        }

        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            makeCall {
                service.deleteResort(requestText.text.toString())
            }
        }

    }

    private fun makeCall(action: suspend () -> Response<ResponseBody>) {
        CoroutineScope(Dispatchers.IO).launch {
            var response: Response<ResponseBody> = action()
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    responseText.text = formatJson(response.body()?.string())
                } else {
                    responseText.text = response.code().toString()
                }
            }
        }
    }

    private fun formatJson(text: String?): String {
        return gson.toJson(JsonParser.parseString(text))
    }


}