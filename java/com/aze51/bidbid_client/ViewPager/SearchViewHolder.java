package com.aze51.bidbid_client.ViewPager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aze51.bidbid_client.R;

/**
 * Created by Leekh on 2016-07-05.
 */
public class SearchViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView text1;
    TextView text2;
    //TextView text3;
    public SearchViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.search_ImageView);
        text1 = (TextView)itemView.findViewById(R.id.search_item_name);
        text2 = (TextView)itemView.findViewById(R.id.search_price);
        //text3 = (TextView)itemView.findViewById(R.id.mypage_remain_time);
    }
}