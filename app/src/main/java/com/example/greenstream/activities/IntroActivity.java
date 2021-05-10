package com.example.greenstream.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.greenstream.R;
import com.example.greenstream.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class IntroActivity extends AppCompatActivity {

    private static final int[] IMAGES = {
            R.drawable.intro_0,
            R.drawable.intro_1,
            R.drawable.intro_2,
            R.drawable.intro_3,
            R.drawable.intro_4,
            R.drawable.intro_5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setTitle(R.string.intro);

        ViewPager viewPager = findViewById(R.id.intro_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, IMAGES);
        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_dots);
        tabLayout.setupWithViewPager(viewPager);
    }
}