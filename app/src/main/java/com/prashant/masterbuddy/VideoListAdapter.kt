package com.prashant.masterbuddy

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import java.util.ArrayList

/**
 * Created by tanmay.agnihotri on 5/19/18.
 */

class VideoListAdapter(private val videoListFragment: VideoListFragment, private var mListValues: ArrayList<VideoList>?) : RecyclerView.Adapter<VideoListAdapter.MyViewHolder>() {
    private val context: Context

    init {
        this.context = videoListFragment.activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_list_container, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvVideoTitle.text = mListValues!![position].videoTitle
        holder.tvVideoDescription.text = mListValues!![position].videoDescription
        Glide.with(context)
                .load(imagePrefix + mListValues!![position].videoThumbnail!!)
                .apply(RequestOptions().placeholder(R.drawable.thumbnail_default))
                .into(holder.ivThumbnail)
        holder.itemView.setOnClickListener { v ->
            val intent = Intent(context, PlayVideoActivity::class.java)
            intent.putExtra("ID", mListValues!![holder.adapterPosition].videoId)
            context.startActivity(intent)
        }
        if (position == itemCount - 1) {
            videoListFragment.addNewVideosInList(position)
        }
        //holder.ibOption.setOnClickListener { v -> videoListFragment.addToPlayList(v, mListValues!![holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return mListValues!!.size
    }

    fun refresh(list: ArrayList<VideoList>) {
        mListValues = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvVideoTitle: TextView = view.findViewById(R.id.tvVideoTitle)
        val tvVideoDescription: TextView = view.findViewById(R.id.tvVideoDescription)
        val ivThumbnail: ImageView = view.findViewById(R.id.ivThumbnail)
        val ibOption: ImageButton = view.findViewById(R.id.ibOption)
    }

    companion object {
        private val imagePrefix = "http://masterbuddy.com"
    }

}


