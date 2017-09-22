package com.aze51.bidbid_client.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aze51.bidbid_client.ApplicationController;
import com.aze51.bidbid_client.MainActivity;
import com.aze51.bidbid_client.Network.NetworkService;
import com.aze51.bidbid_client.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jeon3029 on 16. 7. 3..
 */
public class PushListFragment extends Fragment {
    View rootViewBasic;
    //ImageView image1;
    ListView listView;
    private ArrayList<PushListViewItem> itemDatas = null;
    PushListCustomAdapter pushListCustomAdapter;
    NetworkService networkService;
    Context ctx;
    String userId;
    List<PushListViewItem> tmpList;
    public PushListFragment(){
        //생성자
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootViewBasic = inflater.inflate(R.layout.push_list_fragment,container,false);
        listView = (ListView)rootViewBasic.findViewById(R.id.push_list_view);
        initNetworkService();
        userId = ApplicationController.getInstance().getUserId();
        String tmp = "dpdnjs1222";
        itemDatas = new ArrayList<PushListViewItem>();
        ctx = ApplicationController.getInstance().getMainActivityContext();

        //TODO : 서버로부터 푸시 리스트 받아서 itemDatas에 add해준다.
        final PushListViewItem tempItem = new PushListViewItem();
        /*tempItem.img = R.drawable.food;
        tempItem.title = "제품 이름";
        tempItem.bidPrice = 5000;
        tempItem.remainTime = 15;//분*/
        Call<List<PushListViewItem>> getList = networkService.getPushList(tmp);
        getList.enqueue(new Callback<List<PushListViewItem>>() {
                            @Override
                            public void onResponse(Response<List<PushListViewItem>> response, Retrofit retrofit) {
                                if(response.isSuccess()){
                                    tmpList = response.body();
                                    for(PushListViewItem p : tmpList){
                                        itemDatas.add(p);
                                    }
                                    pushListCustomAdapter = new PushListCustomAdapter(itemDatas,ctx);
                                    listView.setAdapter(pushListCustomAdapter);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });

                listView.setOnItemClickListener(listener);
        return rootViewBasic;
    }
    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {

        //ListView의 아이템 중 하나가 클릭될 때 호출되는 메소드
        //첫번째 파라미터 : 클릭된 아이템을 보여주고 있는 AdapterView 객체(여기서는 ListView객체)
        //두번째 파라미터 : 클릭된 아이템 뷰
        //세번째 파라미터 : 클릭된 아이템의 위치(ListView이 첫번째 아이템(가장위쪽)부터 차례대로 0,1,2,3.....)
        //네번재 파리미터 : 클릭된 아이템의 아이디(특별한 설정이 없다면 세번째 파라이터인 position과 같은 값)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ctx = ApplicationController.getInstance().getMainActivityContext();
            ((MainActivity)ctx).show_detail_list();
        }
    };

    private void initNetworkService() {
        networkService = ApplicationController.getInstance().getNetworkService();
    }
}
