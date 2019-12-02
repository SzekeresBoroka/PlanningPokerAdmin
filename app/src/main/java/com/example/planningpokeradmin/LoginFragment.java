package com.example.planningpokeradmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {
    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_login, container, false);

        final Button btn_login = v.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et_name = v.findViewById(R.id.et_name);
                String name = et_name.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(context, "Name is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.active_admin), name);
                editor.apply();

                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container, new GroupsFragment());
                frag_trans.addToBackStack("PlanningPokerAdmin");
                frag_trans.commit();
            }
        });

        return v;
    }
}
