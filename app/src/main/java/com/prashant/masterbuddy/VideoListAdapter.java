package com.prashant.masterbuddy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by tanmay.agnihotri on 5/19/18.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {

    private ArrayList<VideoList> mListValues;
    private Context context;
    private VideoListFragment videoListFragment;
    private static final String imagePrefix = "http://masterbuddy.com";

    public VideoListAdapter(VideoListFragment fragment, ArrayList<VideoList> videoLists) {
        this.mListValues = videoLists;
        this.context = fragment.getActivity();
        videoListFragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_container, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.tvVideoTitle.setText(mListValues.get(position).getVideoTitle());
        holder.tvVideoDescription.setText(mListValues.get(position).getVideoDescription());
        Glide.with(context)
                .load(imagePrefix+mListValues.get(position).getVideoThumbnail())
                .apply(new RequestOptions().placeholder(R.drawable.thumbnail_default))
                .into(holder.ivThumbnail);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlayVideoActivity.class);
            intent.putExtra("ID", mListValues.get(holder.getAdapterPosition()).getVideoId());
            context.startActivity(intent);
        });
        if (position == getItemCount()-1) {
            videoListFragment.addNewVideosInList(position);
        }
        holder.ibOption.setOnClickListener(v ->
                videoListFragment.addToPlayList(v, mListValues.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return mListValues.size();
    }

    public void refresh(ArrayList<VideoList> list) {
        mListValues = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvVideoTitle;
        private TextView tvVideoDescription;
        private ImageView ivThumbnail;
        private ImageButton ibOption;
        MyViewHolder(View view) {
            super(view);
            tvVideoTitle = view.findViewById(R.id.tvVideoTitle);
            tvVideoDescription = view.findViewById(R.id.tvVideoDescription);
            ivThumbnail = view.findViewById(R.id.ivThumbnail);
            ibOption = view.findViewById(R.id.ibOption);
        }
    }

}


