package juhyang.practice.flo_programmers.music

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.JsonObject
import juhyang.practice.flo_programmers.R
import juhyang.practice.flo_programmers.lyric.Lyric
import juhyang.practice.flo_programmers.retrofit.MusicHelper
import kotlinx.coroutines.*


/**

 * Created by juhyang on 2021/06/01.

 */
class MusicViewModel : ViewModel() {

    companion object {
        @JvmStatic
        @BindingAdapter("imageFromUrl")
        fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(view)
            }
        }

        @JvmStatic
        @BindingAdapter("playButtonImage")
        fun setPlayButtonImage(view : ImageView, isPlaying : Boolean) {
            if (isPlaying) {
                view.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                view.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }
    }

    private var musicInform: JsonObject = JsonObject()

    private val _songTitle = MutableLiveData<String>()
    val songTitle: LiveData<String>
        get() {
            return _songTitle
        }

    private val _albumTitle = MutableLiveData<String>()
    val albumTitle: LiveData<String>
        get() {
            return _albumTitle
        }

    private val _artistName = MutableLiveData<String>()
    val artistName: LiveData<String>
        get() {
            return _artistName
        }

    private val _currentLyric = MutableLiveData<String>()
    val currentLyric: LiveData<String>
        get() {
            return _currentLyric
        }

    private val _nextLyric = MutableLiveData<String>()
    val nextLyric: LiveData<String>
        get() {
            return _nextLyric
        }

    private val _currentTime = MutableLiveData<Int>()
    val currentTime: LiveData<Int>
        get() {
            return _currentTime
        }

    private val _albumImageUrl = MutableLiveData<String>()
    val albumImageUrl: LiveData<String>
        get() {
            return _albumImageUrl
        }

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int>
        get() {
            return _duration
        }

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying : LiveData<Boolean>
        get() {
            return _isPlaying
        }

    private val _lyricList : MutableLiveData<ArrayList<Lyric>> = MutableLiveData<ArrayList<Lyric>>()
    val lyricList : LiveData<ArrayList<Lyric>>
        get() {
            return _lyricList
        }

    private val _visibleMainLayout = MutableLiveData<Boolean>()
    val visibleMainLayout : LiveData<Boolean>
        get() {
            return _visibleMainLayout
        }

    init {
        _currentTime.value = 1
        _visibleMainLayout.value = true
    }

    var mediaPlayer = MediaPlayer()
    var musicFile = ""

    fun getMusicInform() {
        val musicHelper = MusicHelper()
        musicHelper.getMusic {
            musicInform = it

            _albumTitle.value = it.get("album").asString
            _artistName.value = it.get("singer").asString
            _songTitle.value = it.get("title").asString
            _albumImageUrl.value = it.get("image").asString

            _duration.value = it.get("duration").asInt * 1000
            musicFile = it.get("file").asString
            val lyrics = it.get("lyrics").asString
            makeLyricList(lyrics)
        }
    }

    private fun makeLyricList(lyrics : String) {
        val lyricTempList = lyrics.split("\n")
        val resultLyricList = ArrayList<Lyric>()
        for (lyric in lyricTempList) {
            resultLyricList.add(Lyric(lyric))
        }

        _lyricList.value = resultLyricList
    }

    private fun setMusic() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.apply {
            setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            setDataSource(musicFile)
            prepare()
        }
    }

    private var job: Job? = null
    fun playMusic() {
        if (mediaPlayer.isPlaying) {
            _isPlaying.value = false
            mediaPlayer.stop()
            job?.cancel()
        } else {
            setMusic()
            setCurrentTime(currentTime.value!!)
            mediaPlayer.start()
            _isPlaying.value = true

            mediaPlayer.setOnCompletionListener {
                _isPlaying.value = false
            }

            job = GlobalScope.launch(Dispatchers.Main) {
                while (mediaPlayer.isPlaying) {
                    delay(1000)
                    _currentTime.value = mediaPlayer.currentPosition
                }
            }
        }
    }

    fun setCurrentTime(currentTime: Int) {
        _currentTime.value = currentTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaPlayer.seekTo((currentTime).toLong(), MediaPlayer.SEEK_CLOSEST)
        } else mediaPlayer.seekTo(
            currentTime as Int
        )
    }

    var beforeCurrent = -1
    fun setCurrentLyric(currentTime: Int) {
        val lyricList = lyricList.value ?: return
        if (lyricList.size == 0) return
        if (lyricList[0].time > currentTime) {
            _currentLyric.value = ""
            _nextLyric.value = lyricList[0].text
        } else {
            var status = true
            for (i in 1 until lyricList.size) {
                if (lyricList[i - 1].time <= currentTime && currentTime < lyricList[i].time) {
                    if (beforeCurrent != -1) {
                        lyricList[beforeCurrent].isCurrent = false
                    }
                    lyricList[i - 1].isCurrent = true
                    beforeCurrent = i - 1
                    _currentLyric.value = lyricList[i - 1].text
                    _nextLyric.value = lyricList[i].text
                    status = false
                    break
                }
            }
            if (status) {
                _currentLyric.value = lyricList.last().text
                _nextLyric.value = ""
            }
        }

        _lyricList.value = lyricList
    }

    fun changeMainLayout() {
        _visibleMainLayout.value = visibleMainLayout.value != true
    }
}
