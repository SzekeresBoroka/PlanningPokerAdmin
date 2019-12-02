package com.example.planningpokeradmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction frag_trans = getSupportFragmentManager().beginTransaction();
        frag_trans.replace(R.id.fragment_container,new LoginFragment());
        frag_trans.commit();
    }

}
