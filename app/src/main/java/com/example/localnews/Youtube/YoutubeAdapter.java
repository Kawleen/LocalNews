package com.example.localnews.Youtube;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.localnews.Interface.ItemClickListener;
import com.example.localnews.R;
import com.example.localnews.YoutubeVideo;

import java.util.List;


public class YoutubeAdapter extends RecyclerView.Adapter<ChannelHolder> {

    private List<ChannelModel> channelList;
    private Context mContext;
    private LayoutInflater inflater;

    public YoutubeAdapter(List<ChannelModel> channelList, Context mContext) {
        this.channelList = channelList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ChannelHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.channel_row,viewGroup,false);
        return new ChannelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelHolder channelHolder, int i) {
        channelHolder.txtCountry.setText(channelList.get(i).getCountry());

        channelHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick)
                {
                    Intent viewNewsIntent = new Intent(mContext, YoutubeVideo.class);
                    viewNewsIntent.putExtra("link", channelList.get(position).getUrl());
                    mContext.startActivity(viewNewsIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }
}

class ChannelHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    public TextView txtCountry;
    private ItemClickListener itemClickListener;

    public ChannelHolder(View itemView) {
        super(itemView);

        txtCountry = (TextView)itemView.findViewById(R.id.txtCountry);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }
}