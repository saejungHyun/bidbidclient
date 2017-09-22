package com.aze51.bidbid_client.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aze51.bidbid_client.ApplicationController;
import com.aze51.bidbid_client.MainActivity;
import com.aze51.bidbid_client.Network.NetworkService;
import com.aze51.bidbid_client.Network.Product;
import com.aze51.bidbid_client.R;
import com.aze51.bidbid_client.ViewPager.ListItemData;
import com.aze51.bidbid_client.ViewPager.MyPageRecyclerViewAdapter;
import com.aze51.bidbid_client.ViewPager.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jeon3029 on 16. 7. 4..
 */
public class MypageFragment extends Fragment {
    NetworkService networkService;
    View rootViewBasic;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    LinearLayoutManager mLayoutManager;
    Context mContext;
    ArrayList<ListItemData> itemDatas;
    List<Product> myProducts;

    public MypageFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootViewBasic = inflater.inflate(R.layout.mypage_list_fragment,container,false);
        recyclerView = (RecyclerView)rootViewBasic.findViewById(R.id.recyclerView_mypage);
        mContext = ApplicationController.getInstance().getMainActivityContext();
        initNetworkService();
        mLayoutManager = new LinearLayoutManager(mContext);//Mainactivity 의 this
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        //adapter 설정
        itemDatas = new ArrayList<ListItemData>();

         //TODO : 서버에 유저 아이디 보내서 유저가 입찰하고 있는 리스트 받아야 함
        String user = ApplicationController.getInstance().getUserId();

        Call<List<Product>> userCall = networkService.getMyPage(user);
        userCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Response<List<Product>> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    if(response.body().get(0).product_img != null){ // 사용자가 입찰하지 않을 경우 서버에서 어떻게 처리해야되나?
                    myProducts = response.body();
                    Log.i("TAG","get response");
                    ApplicationController.getInstance().SetProducts5(myProducts);
                        Log.i("TAG","response not empty");
                        //ApplicationController.getInstance().SetProducts5(myProducts);
                        for (Product product : myProducts) {
                            itemDatas.add(new ListItemData(product));
                        }
                        mAdapter = new MyPageRecyclerViewAdapter(mContext,itemDatas);
                        recyclerView.setAdapter(mAdapter);
                        ((MainActivity)mContext).show_mypage_list();
                    }
                    else{
                        Toast.makeText(getContext(),"입찰하신 상품이 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                 }
                }
            @Override
            public void onFailure(Throwable t) {

            }
        });
        /*Product p = new Product();
        p.store_name = ApplicationController.getInstance().GetSearchText();
        p.register_minprice = 1000;
        ListItemData tempitem = new ListItemData(p);
        itemDatas.add(tempitem);*/

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //TODO :여기서 프로덕트의 리스트를 사용하면 안되고 서버로 부터 받은 유저가 입찰하고 있는 리스트 사용해야 됨
                        ApplicationController.getInstance().setPos(position);
                        ((MainActivity) mContext).show_detail_list();
                        String pos = String.valueOf(position);
                        Toast toast = Toast.makeText(mContext,
                                "포지션 : " + pos, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }));
        return rootViewBasic;
    }
    private void initNetworkService() {
        networkService = ApplicationController.getInstance().getNetworkService();
    }
}