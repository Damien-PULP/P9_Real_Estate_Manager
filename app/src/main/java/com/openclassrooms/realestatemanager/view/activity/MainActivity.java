package com.openclassrooms.realestatemanager.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.view.fragment.AddFragment;
import com.openclassrooms.realestatemanager.view.fragment.DetailFragment;
import com.openclassrooms.realestatemanager.view.fragment.MainFragment;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {



    //Fragment
    private MainFragment mainFragment;
    private DetailFragment detailFragment;
    private AddFragment addFragment;
    //VIEW MODEL
    private MainViewModel mainViewModel;
    //NAVIGATION
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configureFragment();
        this.configureViewModel();
        this.configureUI();
        this.updateUIWithData();
    }

    private void updateUIWithData() {
        /* TODO Suppress just for init */
        mainViewModel.init();
        mainViewModel.insertUser("User", "user", "user@email.com", "0448888888", "iconpath", "password");
        mainViewModel.getUser().observe(this, this::successGetCurrentUser);
    }

    private void successGetCurrentUser(User user) {
        Log.d("MainActivity", "The user is " + user.getFirstName());
    }

    private void configureUI() {
        bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateNavigationFragment(item.getItemId()));
    }
    private Boolean updateNavigationFragment(Integer integer){
        switch (integer) {
            case R.id.add_page:
                switchFragment(2);
                break;
            case R.id.home_page:
                switchFragment(0);
                break;
        }
        return true;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void configureFragment() {

        if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
            if (Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main)).getClass() == MainFragment.class) {
                mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main);
            }
        }

        if(mainFragment == null){
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_main, mainFragment)
                    .commit();
        }
        //DOUBLE VIEW LARGE SCREEN

        if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
            if (Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main)).getClass() == DetailFragment.class) {
                detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_detail);
            }
        }

        if(detailFragment == null && findViewById(R.id.activity_main_frame_detail) != null){
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_detail, detailFragment)
                    .commit();
        }
    }
    public void switchFragment (int index){
        if(index == 1 && findViewById(R.id.activity_main_frame_detail) == null){
            if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
                if (Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main)).getClass() == DetailFragment.class) {
                    detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main);
                }
            }
            if(detailFragment == null){
                detailFragment = new DetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_frame_main, detailFragment)
                        .commit();
            }else{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_frame_main, detailFragment)
                        .commit();
            }
        } else if(index == 0){
            if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
                if (Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main)).getClass() == MainFragment.class) {
                    mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main);
                }
            }
            if(mainFragment == null){
                mainFragment = new MainFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_main, mainFragment)
                    .commit();

        } else if (index == 2){
            if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
                if (Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main)).getClass() == AddFragment.class) {
                    addFragment = (AddFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main);
                }
            }
            if(addFragment == null){
                addFragment = new AddFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_main, addFragment)
                    .commit();
        }
    }
    public void setCurrentProperty(long id){
        mainViewModel.setCurrentIndexPropertyDetail(id);
    }
}