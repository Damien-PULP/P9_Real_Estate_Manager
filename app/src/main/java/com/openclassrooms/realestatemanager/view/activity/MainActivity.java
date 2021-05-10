package com.openclassrooms.realestatemanager.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.view.fragment.AddFragment;
import com.openclassrooms.realestatemanager.view.fragment.DetailFragment;
import com.openclassrooms.realestatemanager.view.fragment.MainFragment;
import com.openclassrooms.realestatemanager.view.fragment.MapFragment;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Fragment
    private MainFragment mainFragment;
    private DetailFragment detailFragment;
    private AddFragment addFragment;
    private MapFragment mapFragment;
    //VIEW MODEL
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configureViewModel();
        this.getUser();
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void getUser() {
        mainViewModel.init();
        mainViewModel.insertUser("User", "user", "user@email.com", "0448888888", "iconpath", "password");
        assert mainViewModel.getUser() != null;
        mainViewModel.getUser().observe(this, this::successGetCurrentUser);
    }
    private void successGetCurrentUser(User user) {
        this.configureFragment();
        this.configureUI();
    }

    private void configureUI() {
        //NAVIGATION
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateNavigationFragment(item.getItemId()));
    }
    private Boolean updateNavigationFragment(Integer integer){
        final int ADD_PAGE = R.id.add_page;
        final int HOME_PAGE = R.id.home_page;
        final int MAP_PAGE = R.id.map_page;
        switch (integer) {
            case ADD_PAGE:
                switchFragment(2);
                break;
            case HOME_PAGE:
                switchFragment(0);
                break;
            case MAP_PAGE :
                switchFragment(3);
                break;
            default:
                return false;
        }
        return true;
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
        switch (index){
            case 0 :
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
                break;
            case 1 :
                if(findViewById(R.id.activity_main_frame_detail) == null){
                    if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
                        if (Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main)).getClass() == DetailFragment.class) {
                            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main);
                        }
                    }
                    if(detailFragment == null){
                        detailFragment = new DetailFragment();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.activity_main_frame_main, detailFragment)
                            .commit();
                }else{
                    if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
                        detailFragment = new DetailFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.activity_main_frame_detail, detailFragment)
                                .commit();
                    }
                }
                break;
            case 2 :
                if(findViewById(R.id.activity_main_frame_detail) == null){
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
                }else{
                    if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
                        addFragment = new AddFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.activity_main_frame_detail, addFragment)
                                .commit();
                    }
                }
                break;
            case 3 :
                if(findViewById(R.id.activity_main_frame_detail) == null){
                    if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
                        if (Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main)).getClass() == MapFragment.class) {
                            mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main);
                        }
                    }
                    if(mapFragment == null){
                        mapFragment = new MapFragment();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.activity_main_frame_main, mapFragment)
                            .commit();
                }else{
                    if(getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_main) != null) {
                        mapFragment = new MapFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.activity_main_frame_detail, mapFragment)
                                .commit();
                    }
                }

                break;

        }

    }
    public void setCurrentProperty(long id){
        mainViewModel.setCurrentIndexPropertyDetail(id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}