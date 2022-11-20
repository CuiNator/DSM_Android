package com.example.digitalsignmanagement;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.digitalsignmanagement.ui.login.LoginActivity;
import com.example.digitalsignmanagement.unterschriften.Sign;
import com.example.digitalsignmanagement.unterschriften.SignAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.databinding.ActivityScrollingBinding;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private RecyclerView sign;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<Sign> signs = initSigns();

        this.sign = (RecyclerView)findViewById(R.id.unterschrifen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.sign.setLayoutManager(mLayoutManager);

        adapter = new SignAdapter(signs);
        this.sign.setAdapter(adapter);


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        Context context = getBaseContext();

        SignAdapter mAdapter = new SignAdapter(signs);

        RecyclerView recyclerView = findViewById(R.id.unterschrifen);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent1 = new Intent(ScrollingActivity.this, activity_sign.class);
                        startActivity(intent1);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }



    private ArrayList<Sign> initSigns() {
        ArrayList<Sign> list = new ArrayList<>();
        list.add(new Sign("Urlaubsantrag", "20.11.22", "Yanikaa", false));
        list.add(new Sign("Urlaubsantrag", "20.11.22", "Yanik1", true));
        list.add(new Sign("Urlaubsantrag", "20.11.22", "Yanik2", false));
        list.add(new Sign("Urlaubsantrag", "20.11.22", "Yanik3", true));
        return list;

    }
}