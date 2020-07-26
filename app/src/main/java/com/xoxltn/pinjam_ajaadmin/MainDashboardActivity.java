/*
 * Created by Albert Kristaen (Kayzweller) on 24/06/20 18.50
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 24/06/20 00.22
 */

package com.xoxltn.pinjam_ajaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(mOnNavListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MenuRequestFragment()).commit();
    }

    //-------------------------------------------------------------------------------------------//

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavListener =
            item -> {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.fund_request:
                        selectedFragment = new MenuRequestFragment();
                        break;
                    case R.id.fund_approval:
                        selectedFragment = new MenuApprovalFragment();
                        break;
                    case R.id.fund_return:
                        selectedFragment = new MenuReturnFragment();
                        break;
                    case R.id.fund_admin:
                        selectedFragment = new MenuAdminFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace
                            (R.id.fragment_container, selectedFragment).commit();
                }

                return true;
            };

    //-------------------------------------------------------------------------------------------//

    // variabel untuk fungsi tombol BACK
    boolean doubleBackToExitPressedOnce = false;
    int DELAY_PRESS = 2000;

    // method untuk tekan tombol BACK dua kali untuk keluar
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, DELAY_PRESS);
    }

}
