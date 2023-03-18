package com.smg.itemmarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smg.itemmarket.chatlist.ChatFragment;
import com.smg.itemmarket.home.HomeFragment;
import com.smg.itemmarket.mypage.MypageFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        HomeFragment homeFragment = new HomeFragment();
        ChatFragment chatFragment = new ChatFragment();
        MypageFragment mypageFragment = new MypageFragment();

        replaceFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        replaceFragment(homeFragment);
                        break;
                    case R.id.chatList:
                        replaceFragment(chatFragment);
                        break;
                    case R.id.myPage:
                        replaceFragment(mypageFragment);
                        break;
                }
                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }
}