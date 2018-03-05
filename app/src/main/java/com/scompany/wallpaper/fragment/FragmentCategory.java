package com.scompany.wallpaper.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.scompany.wallpaper.R;
import com.scompany.wallpaper.adapter.DataAdapter;
import com.scompany.wallpaper.model.Data;
import com.scompany.wallpaper.utils.Contanst;

/**
 * Created by Admin on 3/5/2018.
 */

public class FragmentCategory extends Fragment {
    private int page;
    private String[] titles;
    private String title;
    private Gson gson;
    private Spinner spinner;
    private RecyclerView rvData;
    private RecyclerView.LayoutManager layoutManager;
    private DataAdapter adapter;

    public FragmentCategory() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        gson = new Gson();
        spinner = view.findViewById(R.id.spinner);
        rvData = view.findViewById(R.id.rv_image);
        layoutManager = new GridLayoutManager(getContext(),2);
        rvData.setLayoutManager(layoutManager);
        if (getArguments() != null) {
            page = getArguments().getInt("Position");
            title = getArguments().getString(Contanst.CONTENT);
            titles = gson.fromJson(title, String[].class);
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, titles);
            spinner.setAdapter(arrayAdapter);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Arrays.fill(list, null);
                getData("http://cdn.skollabs.com/love/v1/" + titles[i] + ".js");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                getData("http://cdn.skollabs.com/love/v1/" + titles[0] + ".js");
            }
        });
        return view;
    }

    private void getData(String url) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait!!!");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("123123", response);
                    Data data = gson.fromJson(response, Data.class);
                    adapter = new DataAdapter(getContext(), data);
                    adapter.notifyDataSetChanged();
                    rvData.setAdapter(adapter);
                } catch (Exception e) {
                    Log.e("CHECK_ERROR", e.getMessage());
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });
        queue.add(stringRequest);
    }
}
