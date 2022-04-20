package com.atula.doanapplication.ui.user.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.atula.doanapplication.R;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.config.InfoUser;
import com.atula.doanapplication.model.CUser;
import com.atula.doanapplication.ui.user.fragment.PageOneFragment;
import com.atula.doanapplication.ui.user.fragment.PageThreeFragment;
import com.atula.doanapplication.ui.user.fragment.PageTwoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener , ValueEventListener {
    private static MainActivity activity;
    public static MainActivity getInstance(){
        if(activity == null){
            activity = new MainActivity();
        }
        return activity;
    }


    BottomNavigationView navigation;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        firebaseDatabase = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        String mssv = intent.getStringExtra("MSSV");
        databaseReference = firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_USER).child(mssv);
        databaseReference.addValueEventListener(this);

        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(this);
//

    }

    public void swapToHome(){
        navigation.setSelectedItemId(R.id.navigation_home);
        //navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

    public boolean loadFragment(int id) {
        Fragment fragment = null;
        String backStateName = "home";
        switch (id) {
            case R.id.navigation_home:
                fragment = PageOneFragment.getInstance();
                break;
            case R.id.navigation_search:
                fragment = PageTwoFragment.getInstance();
                backStateName = "tvShow";
                break;
            case R.id.navigation_more:
                fragment = PageThreeFragment.getInstance();
                backStateName = "more";
                break;
        }

        replaceFragment(fragment,backStateName,id);
        return true;
    }


    public void replaceFragment (Fragment fragment, String backStateName, int i){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentByTag(backStateName) == null){
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }else{
            getSupportFragmentManager().popBackStack(backStateName,i);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return loadFragment(item.getItemId());
    }

    boolean isLoadFirst = true;
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getValue() != null){
            CUser cUser = snapshot.getValue(CUser.class);
            InfoUser.getInstance().setcUser(cUser);
        }
        if(isLoadFirst){
            isLoadFirst = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout linearLayout = findViewById(R.id.lnLoading);
                    linearLayout.setVisibility(View.GONE);
                    loadFragment(R.id.navigation_home);
                }
            });
        }
    }

    @Override
    public void onCancelled(@NonNull  DatabaseError error) {

    }
}