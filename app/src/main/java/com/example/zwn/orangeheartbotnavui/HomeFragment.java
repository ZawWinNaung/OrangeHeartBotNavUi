package com.example.zwn.orangeheartbotnavui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    Snackbar snackbar = null;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Tovuti.from(getActivity()).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                showNetworkStateSnackBar(getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE, getString(R.string.color_red), isConnected);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postReference = database.getReference(MainActivity.POSTS);
        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Posts> postsList = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Posts posts = item.getValue(Posts.class);
                    Collections.reverse(postsList);
                    postsList.add(posts);
                    Collections.reverse(postsList);
                }
                myAdapter.setData(postsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myAdapter = new MyAdapter(getActivity());
        recyclerView = view.findViewById(R.id.rvPosts);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapter);
        return view;
    }

    private void showNetworkStateSnackBar(String state, int length, String bgColor, boolean isConnected) {
        if (!isConnected) {
            snackbar = Snackbar.make(recyclerView, state, length);
            View snackBarView = snackbar.getView();
            TextView tv = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackBarView.setBackgroundColor(Color.parseColor(bgColor));
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackBarView.getLayoutParams();
            params.gravity = Gravity.BOTTOM;
            snackBarView.setLayoutParams(params);
            snackbar.show();
        } else {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        }
    }
}
