package juhyang.practice.flo_programmers

import android.util.Log
import juhyang.practice.flo_programmers.retrofit.MusicHelper
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val musicHelper = MusicHelper()
        musicHelper.getMusic {
            Log.d("hyang@it", "${it}")
        }
    }
}
