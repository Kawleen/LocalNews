package com.example.localnews.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.localnews.Interface.rssClickListner;
import com.example.localnews.Model.RSSModelAdmin;
import com.example.localnews.NewsFragment;
import com.example.localnews.R;

import java.util.List;

public class RSSAdapter extends RecyclerView.Adapter<RSSHolder> {

    private List<RSSModelAdmin> rssList;
    private Context mContext;
    private LayoutInflater inflater;

    public RSSAdapter(List<RSSModelAdmin> rssList, Context mContext) {
        this.rssList = rssList;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public RSSHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.channel_row,viewGroup,false);
        return new RSSHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull RSSHolder rssHolder, int i) {
        rssHolder.txtCountry.setText(rssList.get(i).getCountry());

        rssHolder.setItemClickListener(new rssClickListner() {
            @Override
            public void onClick(Context context, int position, boolean isLongClick) {
                Bundle bundle = new Bundle();
                bundle.putString("RssUrl",rssList.get(position).getUrl() );
                NewsFragment fragInfo = new NewsFragment();
                fragInfo.setArguments(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssList.size();
    }
}


class RSSHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

    public TextView txtCountry;
    private rssClickListner itemClickListener;

    public RSSHolder(View itemView) {
        super(itemView);

        txtCountry = (TextView)itemView.findViewById(R.id.txtCountry);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(rssClickListner itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v.getContext(),getAdapterPosition(),false);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v.getContext(),getAdapterPosition(),true);
        return true;
    }
}