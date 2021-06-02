package juhyang.practice.flo_programmers.music

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import juhyang.practice.flo_programmers.R
import juhyang.practice.flo_programmers.databinding.ActivityMusicBinding
import juhyang.practice.flo_programmers.lyric.LyricAdapter

class MusicActivity : AppCompatActivity() {

    val musicViewModel : MusicViewModel by viewModels()
    lateinit var binding : ActivityMusicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_music)
        binding.viewModel = musicViewModel
        binding.lifecycleOwner = this

        musicViewModel.getMusicInform()

        setObserver()
        initRecycler()
        aboutView()
    }

    fun initRecycler() {
        val lyricAdapter = LyricAdapter { lyric ->
            musicViewModel.setCurrentTime(lyric.time.toInt())
        }

        binding.lyricRecyclerView.adapter = lyricAdapter
        binding.lyricRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.lyricRecyclerView.setHasFixedSize(true)

        musicViewModel.lyricList.observe(this, Observer { lyricList ->
            Log.d("hyang@LyricList", "LyricListChanged !")
            lyricAdapter.setNewLyricList(lyricList)
            for (i in 0 until lyricList.size) {
                val lyric = lyricList[i]
                if (lyric.isCurrent) {
                    binding.lyricRecyclerView.smoothScrollToPosition(i)
                }
            }
        })
    }

    fun setObserver() {
        musicViewModel.currentTime.observe(this, Observer {
            musicViewModel.setCurrentLyric(it)
        })
    }

    fun aboutView() {
        binding.songSeekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicViewModel.setCurrentTime(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}
