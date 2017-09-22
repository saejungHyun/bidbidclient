package com.aze51.bidbid_client.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aze51.bidbid_client.ApplicationController;
import com.aze51.bidbid_client.MainActivity;
import com.aze51.bidbid_client.Network.NetworkService;
import com.aze51.bidbid_client.Network.Product;
import com.aze51.bidbid_client.R;
import com.aze51.bidbid_client.ViewPager.ListItemData;
import com.aze51.bidbid_client.ViewPager.RecyclerItemClickListener;
import com.aze51.bidbid_client.ViewPager.SearchRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jeon3029 on 16. 7. 5..
 */
public class SearchListOnClickedFragment extends Fragment {
    View rootViewBasic;
    ArrayList<ListItemData> itemDatas;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    LinearLayoutManager mLayoutManager;
    Context mContext;
    Button searchButton;
    EditText searchText;
    NetworkService networkService;
    List<Product> products;
    String text;
    Context ctx;

    public SearchListOnClickedFragment(){
        //생성자
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("MyTag", "onCreateView");
        rootViewBasic = inflater.inflate(R.layout.search_list_onclicked_fragment,container,false);
        recyclerView = (RecyclerView)rootViewBasic.findViewById(R.id.recyclerView_search);
        searchButton = (Button)rootViewBasic.findViewById(R.id.search_button_onclicked);
        searchText = (EditText)rootViewBasic.findViewById(R.id.search_edit_text_onclicked);
        ctx = ApplicationController.getInstance().getMainActivityContext();
        initNetworkService();
        recyclerView.setHasFixedSize(true);
        mContext = ApplicationController.getInstance().getMainActivityContext();

        mLayoutManager = new LinearLayoutManager(mContext);//Mainactivity 의 this
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        //adapter 설정
        itemDatas = new ArrayList<ListItemData>();
        //TODO : 검색어 보내서 아이템 서버로 부터 받아야 함 그리고 itemDatas에 추가
        //검색어는 application controller 의 getsearchtext로 string객체로 받을 수 있음
        text = ApplicationController.getInstance().GetSearchText();
        searchText.setText(text);

        if(ApplicationController.getInstance().GetGridViewOnClicked()==1){
            ApplicationController.getInstance().SetGridViewOnClick(0);
            CallRecomandSearch(text);
        }
        else{
            CallRecomandSearch(text);
        }
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                text = searchText.getText().toString();
                ApplicationController.getInstance().SetSearchText(text);
                if(itemDatas!=null)
                    itemDatas.clear();
                if(((MainActivity)ctx).getFromState() == 1){//detail page back

                }
                else{
                    CallRecomandSearch(text);
                }
                //CallRecomandSearch(text);
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        ApplicationController.getInstance().setPos(position);
                        ((MainActivity) mContext).show_detail_list();
                        //String pos = String.valueOf(position);
                        //Toast toast = Toast.makeText(mContext,
                        //        "포지션 : " + pos, Toast.LENGTH_LONG);
                        //toast.setGravity(Gravity.CENTER, 0, 0);
                        //toast.show();
                    }
                }));
        return rootViewBasic;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("MyTag", "onViewStateRestored");
        searchText = (EditText)rootViewBasic.findViewById(R.id.search_edit_text_onclicked);
        //검색어는 application controller 의 getsearchtext로 string객체로 받을 수 있음
        text = ApplicationController.getInstance().GetSearchText();
        searchText.setText(text);
    }

    private void CallRecomandSearch(String content){
        Call<List<Product>> recommendCall = networkService.searchContents(content);
        recommendCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Response<List<Product>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    products = response.body();
                    for (Product p : products) {
                        itemDatas.add(new ListItemData(p));
                    }
                    ApplicationController.getInstance().SetProducts6(products);
                    //application controller 의 search product list 수정
                    mAdapter = new SearchRecyclerViewAdapter(mContext,itemDatas);
                    recyclerView.setAdapter(mAdapter);
                    ((MainActivity) ctx).show_search_list_onclicked();
                } else {
                    Toast.makeText(getContext(), "검색하신 상품이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void initNetworkService() {
        networkService = ApplicationController.getInstance().getNetworkService();
    }
}
