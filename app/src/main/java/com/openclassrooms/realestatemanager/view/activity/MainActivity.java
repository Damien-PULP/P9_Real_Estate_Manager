package com.openclassrooms.realestatemanager.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.view.fragment.DetailFragment;
import com.openclassrooms.realestatemanager.view.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {



    //Fragment
    private MainFragment mainFragment;
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureFragment();
    }

    private void configureFragment() {

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main);
        if(mainFragment == null){
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_frame_main, mainFragment)
                    .commit();
        }
        //DOUBLE VIEW LARGE SCREEN
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_detail);
        if(detailFragment == null && findViewById(R.id.activity_main_frame_detail) != null){
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_frame_detail, detailFragment)
                    .commit();
        }
    }

    public void switchFragment (int index){
        if(index == 1 && findViewById(R.id.activity_main_frame_detail) == null){
            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_detail);
            if(detailFragment == null){
                detailFragment = new DetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_frame_main, detailFragment)
                        .commit();
            }
        }else{
            mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main);
            if(mainFragment == null){
                mainFragment = new MainFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_frame_main, mainFragment)
                        .commit();
            }
        }
    }
}