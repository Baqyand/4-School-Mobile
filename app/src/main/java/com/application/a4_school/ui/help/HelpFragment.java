package com.application.a4_school.ui.help;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.a4_school.Models.Help;
import com.application.a4_school.R;
import com.application.a4_school.RestAPI.APIClient;
import com.application.a4_school.RestAPI.APIService;
import com.application.a4_school.RestAPI.ResponseData;
import com.application.a4_school.adapter.HelpViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpFragment extends Fragment {
    private ArrayList<Help> list = new ArrayList<>();
    private RecyclerView rv_faq;
    HelpViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_help, container, false);
        rv_faq = root.findViewById(R.id.listhelp);
        getdata();

        return root;
    }

    public void getdata(){
        APIService api = APIClient.getClient().create(APIService.class);
        Call<ResponseData> callHelp = api.gethelp();
        callHelp.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                String data = response.body().toString();
                Log.d("datanya " , data);
                if(response.isSuccessful()){
                    if (response.body().getFaq_list() != null){
                        list.addAll(response.body().getFaq_list());
                    }

                    adapter = new HelpViewAdapter(list);
                    rv_faq.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_faq.setAdapter(adapter);
                }else{
                    Log.d("help", "response: not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("gagal", "gagal");
            }
        });
    }
}