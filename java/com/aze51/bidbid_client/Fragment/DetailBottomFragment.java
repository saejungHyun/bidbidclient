package com.aze51.bidbid_client.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aze51.bidbid_client.ApplicationController;
import com.aze51.bidbid_client.MainActivity;
import com.aze51.bidbid_client.Network.Auction;
import com.aze51.bidbid_client.Network.NetworkService;
import com.aze51.bidbid_client.Network.Product;
import com.aze51.bidbid_client.R;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jeon3029 on 16. 7. 4..
 */
public class DetailBottomFragment extends Fragment {
    View rootViewBasic;
    Auction auction;
    ImageView upDownImage;
    String tmpMessage;
    NetworkService networkService;
    EditText bidText;
    Button bidBtn;
    int position;
    int tmpRegisterId;
    List<Product> products;
    LinearLayout detailLinearBid;
    RelativeLayout relativeLayout;
    Context ctx;
    boolean upDownState;
    public DetailBottomFragment() {
        upDownState = false;//false = down, ture = up
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initNetworkService();
        ctx = ApplicationController.getInstance().getMainActivityContext();

        int pos = ApplicationController.getInstance().getPos();
        if (((MainActivity) ctx).getFromState() == 6) { //from search on clicked  detail
            products = ApplicationController.getInstance().getProducts(5);
            ApplicationController.getInstance().setRegisterId(products.get(pos).register_id);
        } else if (((MainActivity) ctx).getFromState() == 2) { //from favorite detail
            products = ApplicationController.getInstance().getProducts(3);
            ApplicationController.getInstance().setRegisterId(products.get(pos).register_id);
        } else if (((MainActivity) ctx).getFromState() == 3) { //from mypage detail
            products = ApplicationController.getInstance().getProducts(4);
            ApplicationController.getInstance().setRegisterId(products.get(pos).register_id);
        } else {//on viewPager
            position = ApplicationController.getInstance().getPosition();
            products = ApplicationController.getInstance().getProducts(position);
            ApplicationController.getInstance().setRegisterId(products.get(pos).register_id);
        }
        //position = ApplicationController.getInstance().getPos();
        tmpRegisterId = products.get(pos).register_id;
        //final int tmpPrice = products.get(position).register_minprice;
        rootViewBasic = inflater.inflate(R.layout.detail_bottom_fragment,container,false);
        //image1 = (ImageView)rootViewBasic.findViewById(R.id.logo_image);
        //text = (TextView)rootViewBasic.findViewById(R.id.bidbid_text);
        //image2 = (ImageView)rootViewBasic.findViewById(R.id.setting_image);
        upDownImage = (ImageView)rootViewBasic.findViewById(R.id.detail_up_image);
        bidBtn = (Button)rootViewBasic.findViewById(R.id.bidbtn);
        bidText = (EditText)rootViewBasic.findViewById(R.id.inputPrice);
        detailLinearBid = (LinearLayout)rootViewBasic.findViewById(R.id.detail_linear_bid);
        relativeLayout = (RelativeLayout)rootViewBasic.findViewById(R.id.detail_bottom_relative);
        //업다운 클릭 2016 07 03 태준
        upDownImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("TAG","업 버튼 누름");
                if(upDownState == false){//현재 다운
                    upDownState = true;
                    detailLinearBid.setVisibility(LinearLayout.VISIBLE);
                    upDownImage.setImageResource(R.mipmap.bidbar_down);

                    int paddingPixel = 0;
                    float density = ctx.getResources().getDisplayMetrics().density;
                    int paddingDp = (int)(paddingPixel * density);
                    relativeLayout.setPaddingRelative(0,paddingDp,0,0);
                }
                else if(upDownState == true){//현재 업
                    upDownState = false;
                    detailLinearBid.setVisibility(LinearLayout.GONE);
                    upDownImage.setImageResource(R.mipmap.bidbar_up);

                    int paddingPixel = 50;
                    float density = ctx.getResources().getDisplayMetrics().density;
                    int paddingDp = (int)(paddingPixel * density);
                    relativeLayout.setPaddingRelative(0,paddingDp,0,0);
                }
            }
        });
        bidBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // getAuction = ApplicationController.getAuction();
                if(upDownState==false){
                    upDownState = true;
                    detailLinearBid.setVisibility(LinearLayout.VISIBLE);
                    upDownImage.setImageResource(R.mipmap.bidbar_down);
                    int paddingPixel = 0;
                    float density = ctx.getResources().getDisplayMetrics().density;
                    int paddingDp = (int)(paddingPixel * density);
                    relativeLayout.setPaddingRelative(0,paddingDp,0,0);
                }
                else if(!TextUtils.isEmpty(bidText.getText())) {
                    auction = new Auction();
                    auction.user_id = ApplicationController.getUserId();
                    auction.register_id = tmpRegisterId;

                    auction.deal_price = Integer.parseInt(bidText.getText().toString());
                    postBidResult(auction);
                    Log.i("TAG", "입찰 버튼 투름");
                    //Toast.makeText(ctx,"입찰하셨습니다",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx, "가격을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootViewBasic;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        bidText.setText("");
    }

    public void postBidResult(Auction auction){
        Call<Auction> auctionCall = networkService.finishbid(auction);
        auctionCall.enqueue(new Callback<Auction>() {
            @Override
            public void onResponse(Response<Auction> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Log.i("TAG","입찰 성공");
                    //tmpMessage = "입찰 성공";
                    tmpMessage = response.body().resultMessage;
                    bidText.setText("");
                    if(tmpMessage.equals("성공"))
                    {

                    }
                    else if(tmpMessage.equals("실패"))
                    {

                    }
                    Toast.makeText(getContext(), tmpMessage, Toast.LENGTH_SHORT).show();
                }
                else{

                }
            }
            @Override
            public void onFailure(Throwable t) {

            }
        });
    } // BottomMenuFragment

    private void initNetworkService() {
        networkService = ApplicationController.getInstance().getNetworkService();
    }

}
