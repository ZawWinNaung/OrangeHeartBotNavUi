package com.example.zwn.orangeheartbotnavui;


import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadLaterFragment extends Fragment {
    RecyclerView recyclerView;
    DownloadLaterAdapter myAdapter;
    List<DownloadLaterPosts> downloadLaterPostsLists;
    DownloadLaterPostsDb db;


    public DownloadLaterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download_later, container, false);

        db = DownloadLaterPostsDb.getDownloadLaterPostsDataBase(getContext());
        downloadLaterPostsLists = new ArrayList<>();

        myAdapter = new DownloadLaterAdapter(downloadLaterPostsLists,getActivity(), db);
        recyclerView = view.findViewById(R.id.rvDownloadLater);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new MarginItemDecoration(getResources().getInteger(R.integer.item_spacing)));
        recyclerView.setAdapter(myAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDownloadLaterPosts();
    }

    private void getDownloadLaterPosts(){
        db.daoAccess().getAllPost().observe(this, new Observer<List<DownloadLaterPosts>>() {
            @Override
            public void onChanged(@Nullable List<DownloadLaterPosts> downloadLaterPosts) {
                downloadLaterPostsLists = downloadLaterPosts;
                myAdapter.setPostsList(downloadLaterPostsLists);
            }
        });
    }
}
