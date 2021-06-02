package juhyang.practice.flo_programmers

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import juhyang.practice.flo_programmers.retrofit.MusicHelper

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("juhyang.practice.flo_programmers", appContext.packageName)

        val musicHelper = MusicHelper()
        musicHelper.getMusic {
            Log.d("hyang@it", "${it}")
        }
    }
}
