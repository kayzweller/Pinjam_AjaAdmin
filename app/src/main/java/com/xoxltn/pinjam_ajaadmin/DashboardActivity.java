/*
 * Copyright (c) 2020 Albert Kristaen (DBC 113 008)
 * ONLY USE UNDER PERMISSION -OR- I AM GONNA CHOP YOUR HANDS OFF!
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
import com.xoxltn.pinjam_ajaadmin.fragment.AdminFragment;
import com.xoxltn.pinjam_ajaadmin.fragment.ApprovalFragment;
import com.xoxltn.pinjam_ajaadmin.fragment.RequestFragment;
import com.xoxltn.pinjam_ajaadmin.fragment.ReturnFragment;


public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(mOnNavListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RequestFragment()).commit();
    }

    //-------------------------------------------------------------------------------------------//

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.fund_request:
                            selectedFragment = new RequestFragment();
                            break;
                        case R.id.fund_approval:
                            selectedFragment = new ApprovalFragment();
                            break;
                        case R.id.fund_return:
                            selectedFragment = new ReturnFragment();
                            break;
                        case R.id.fund_admin:
                            selectedFragment = new AdminFragment();
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }

                    return true;
                }
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, DELAY_PRESS);
    }

}
