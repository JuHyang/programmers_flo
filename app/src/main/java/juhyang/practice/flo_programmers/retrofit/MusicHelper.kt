package juhyang.practice.flo_programmers.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**

 * Created by juhyang on 2021/06/01.

 */
class MusicHelper {
    companion object {
        const val MUSIC_URL = "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com"
    }
    private fun getServerInterface(url : String) : MusicInterface {
        val client = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        val gsonBuilder = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()

        return retrofit.create(MusicInterface::class.java)
    }

    fun getMusic(onSuccess : (response : JsonObject) -> Unit) {
        val serverInterface = getServerInterface(MUSIC_URL)
        val serverCall = serverInterface.getMusicInform()
        serverCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.code() == 200) {
                    Log.d("hyang@respone", "${response.body()}")
                    val musicObject = response.body() ?: return
                    onSuccess(musicObject)
                } else {
                    Log.e("hyang@err", "${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("hyang@err", "${t.message}")
            }
        })
    }
}
