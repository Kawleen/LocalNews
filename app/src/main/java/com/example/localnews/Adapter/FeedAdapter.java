package com.example.localnews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localnews.Interface.ItemClickListener;
import com.example.localnews.Model.RSSObject;
import com.example.localnews.R;
import com.example.localnews.SqlLite.DatabaseHelper;

class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
{

    public TextView txtTitle,txtPubDate,txtContent;
    private ItemClickListener itemClickListener;

    public FeedViewHolder(View itemView) {
        super(itemView);

        txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
        txtPubDate = (TextView)itemView.findViewById(R.id.txtPubDate);

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

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private RSSObject rssObject;
    private Context mContext;
    private LayoutInflater inflater;

    DatabaseHelper mysqlDb;

    public FeedAdapter(RSSObject rssObject, Context mContext) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        mysqlDb = new DatabaseHelper(mContext);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row,parent,false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {

        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick)
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssObject.getItems().get(position).getLink()));
                    mContext.startActivity(browserIntent);
                }
                if(isLongClick){
                    boolean isInserted = mysqlDb.insertData(rssObject.getItems().get(position).getLink(),
                            rssObject.getItems().get(position).getTitle(),
                            rssObject.getItems().get(position).getPubDate(),
                            rssObject.getItems().get(position).getContent(),"checked");
                    if(isInserted == true){
                        Toast.makeText(mContext.getApplicationContext(),"Added to Favourite",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return rssObject.items.size();
    }
}