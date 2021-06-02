package juhyang.practice.flo_programmers.retrofit

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET

/**

 * Created by juhyang on 2021/06/01.

 */
interface MusicInterface {
    @GET("/2020-flo/song.json")
    fun getMusicInform() : Call<JsonObject>
}
