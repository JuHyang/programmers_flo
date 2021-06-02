package juhyang.practice.flo_programmers.music

import android.util.Log

/**

 * Created by juhyang on 2021/06/02.

 */
class Lyric(lyric: String) {
    var time : Long = 0
    var text : String = ""

    init {
        val time = lyric.substring(1, 10)
        val timeList = time.split(":")
        this.time = timeList[2].toLong()
        this.time = this.time + timeList[1].toLong() * 1000
        this.time = this.time + timeList[0].toLong() * 1000 * 60
        text = lyric.substring(11)
        Log.d("hyang@time", "${this.time}")
        Log.d("hyang@text", "${text}")
    }
}
