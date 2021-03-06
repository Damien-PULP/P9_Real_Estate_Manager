/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.net.wifi.WifiManager;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ConnectivityInternetInstrumentalTest {

    @Rule
    public final ActivityScenarioRule<MainActivity> mActivityTestRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void checkInternetConnectivityWhenIsFalseTest () throws InterruptedException {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable");

        TimeUnit.SECONDS.sleep(2);

        mActivityTestRule.getScenario().onActivity(activity -> {

            WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(false);

            boolean state = Utils.isInternetAvailable(activity);
            assertFalse(state);
        });

    }
    @Test
    public void checkInternetConnectivityWhenIsTrueTest () throws InterruptedException {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");



        TimeUnit.SECONDS.sleep(2);

        mActivityTestRule.getScenario().onActivity(activity -> {

            WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);

            boolean state = Utils.isInternetAvailable(activity);
            assertTrue(state);
        });

    }
}
