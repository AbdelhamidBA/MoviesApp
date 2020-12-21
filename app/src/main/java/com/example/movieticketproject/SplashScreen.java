package com.example.movieticketproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.movieticketproject.Authentication.LoginFragment;
import com.example.movieticketproject.R;

public class SplashScreen extends Fragment {
    Button signInBTN,signUpBTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_main, container, false);
        signInBTN = v.findViewById(R.id.btn_signin_splash);
        signUpBTN = v.findViewById(R.id.btn_signup_splash);

        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.mainfragement,MainActivity.loginFragment);
                fragmentTransaction.commit();

            }
        });

        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.mainfragement,MainActivity.signupFragment);
                fragmentTransaction.commit();
            }
        });

        return v;
    }
}