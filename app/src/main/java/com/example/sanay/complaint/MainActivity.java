package com.example.sanay.complaint;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView mProfileLabel;
    private TextView mComplainLabel;
    private TextView mStatusLabel;

    private ViewPager mMainPager;

    private PagerViewAdapter mPagerViewAdapter;
    private FirebaseAuth mAuth;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser==null)
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProfileLabel =  findViewById(R.id.profileLabel);
        mComplainLabel =  findViewById(R.id.comlainLabel);
        mStatusLabel =  findViewById(R.id.statusLabel);

        mMainPager =  findViewById(R.id.mainPager);
        mMainPager.setOffscreenPageLimit(2);
        mAuth = FirebaseAuth.getInstance();
        mPagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mMainPager.setAdapter(mPagerViewAdapter);

        mProfileLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(0);
            }
        });

        mComplainLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(1);
            }
        });

        mStatusLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(2);
            }
        });

        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //method to change the text size and the highlight of the text
                changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void changeTabs(int position) {

        if(position == 0){
            mProfileLabel.setTextColor(getResources().getColor(R.color.textTabBright));
            mProfileLabel.setTextSize(22);

            mComplainLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mComplainLabel.setTextSize(16);

            mStatusLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mStatusLabel.setTextSize(16);
        }

        if(position == 1){

            mProfileLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mProfileLabel.setTextSize(16);

            mComplainLabel.setTextColor(getResources().getColor(R.color.textTabBright));
            mComplainLabel.setTextSize(22);

            mStatusLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mStatusLabel.setTextSize(16);

        }

        if(position == 2){

            mProfileLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mProfileLabel.setTextSize(16);

            mComplainLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mComplainLabel.setTextSize(16);

            mStatusLabel.setTextColor(getResources().getColor(R.color.textTabBright));
            mStatusLabel.setTextSize(22);

        }

    }
}



