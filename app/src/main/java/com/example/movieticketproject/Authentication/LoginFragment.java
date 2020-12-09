package com.example.movieticketproject.Authentication;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.movieticketproject.MainActivity;
import com.example.movieticketproject.R;
import com.example.movieticketproject.SplashScreen;


public class LoginFragment extends Fragment {
    EditText emailED,passED;
    SwitchCompat rememberme;
    Button loginBTN;
    ImageView back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        emailED = v.findViewById(R.id.ed_email_login);
        passED = v.findViewById(R.id.ed_password_login);
        loginBTN = v.findViewById(R.id.btn_login_login);
        rememberme = v.findViewById(R.id.sw_remember_login);
        back = v.findViewById(R.id.ic_back_login);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.mainfragement, MainActivity.splashScreen);
                fragmentTransaction.commit();
            }
        });

        return v;
    }
}