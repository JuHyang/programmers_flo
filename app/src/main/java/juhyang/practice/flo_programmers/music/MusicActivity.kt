package juhyang.practice.flo_programmers.music

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
import juhyang.practice.flo_programmers.R
import juhyang.practice.flo_programmers.databinding.ActivityMusicBinding

class MusicActivity : AppCompatActivity() {

    val musicViewModel : MusicViewModel by viewModels()
    lateinit var binding : ActivityMusicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_music)
        binding.viewModel = musicViewModel
        binding.lifecycleOwner = this

        musicViewModel.getMusicInform()

        musicViewModel.currentTime.observe(this, Observer {
            musicViewModel.setCurrentLyric(it)
        })

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
