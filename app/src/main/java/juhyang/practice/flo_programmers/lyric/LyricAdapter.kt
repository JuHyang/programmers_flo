package juhyang.practice.flo_programmers.lyric

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import juhyang.practice.flo_programmers.R
import juhyang.practice.flo_programmers.databinding.ItemLyricBinding

/**

 * Created by juhyang on 2021/06/02.

 */
class LyricAdapter(val lyricClick : (lyric : Lyric) -> Unit) : RecyclerView.Adapter<LyricAdapter.ViewHolder>() {
    private var lyricList = ArrayList<Lyric>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLyricBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(lyricList[position])
    }

    override fun getItemCount(): Int {
        return lyricList.size
    }

    fun setNewLyricList(newLyricList : ArrayList<Lyric>) {
        if (lyricList.size == 0) {
            lyricList.addAll(newLyricList)
            notifyDataSetChanged()
        } else {
            for (i in 0 until lyricList.size) {
                notifyItemChanged(i)
            }
        }
    }

    inner class ViewHolder(val binding : ItemLyricBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(lyric : Lyric) {
            binding.lyric = lyric

            if (lyric.isCurrent) {
                binding.lyricText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            } else {
                binding.lyricText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
            }

            binding.root.setOnClickListener {
                lyricClick(lyric)
            }

            binding.executePendingBindings() // 이걸 안해주면 다음 프레임에서 업데이트를 한다. 이를 해줌으로써 즉시 화면 업데이트 진행 한다.
        }
    }
}
