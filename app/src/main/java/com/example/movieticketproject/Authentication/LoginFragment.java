package com.example.movieticketproject.Authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.movieticketproject.HomeActivity;
import com.example.movieticketproject.MainActivity;
import com.example.movieticketproject.Models.Users;
import com.example.movieticketproject.Profiles.ClientProfile;
import com.example.movieticketproject.Profiles.ProfileFragment;
import com.example.movieticketproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginFragment extends Fragment {
    EditText emailED,passED;
    SwitchCompat rememberme;
    Button loginBTN;
    ImageView back;
    DatabaseReference reference = null;
    String email,password;
    SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        emailED = v.findViewById(R.id.ed_email_login);
        passED = v.findViewById(R.id.ed_password_signup);
        loginBTN = v.findViewById(R.id.btn_login_login);
        rememberme = v.findViewById(R.id.sw_remember_login);
        back = v.findViewById(R.id.ic_back_signup);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        pref = getActivity().getSharedPreferences("current_user", Context.MODE_PRIVATE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.mainfragement, MainActivity.splashScreen);
                fragmentTransaction.commit();
            }
        });
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailED.getText().toString();
                password = passED.getText().toString();
                if(emailED.getText().toString().equals("") || !isEmailValid(emailED.getText().toString()))
                {
                    emailED.setError("Enter a valid Email");
                }
                else if(passED.getText().toString().equals("") || passED.getText().toString().length() < 6)
                {
                    passED.setError("Enter a valid password");
                }
                else
                {
                    Query query = reference.orderByChild("email").equalTo(emailED.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                DataSnapshot data = snapshot.getChildren().iterator().next();
                                System.out.println(data.child("password").getValue(String.class));
                                String passwordDB = data.child("password").getValue(String.class);
                                if(passwordDB.equals(password))
                                {
                                    Users currentUser = data.getValue(Users.class);
                                    System.out.println(currentUser);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("fullname",currentUser.getFullname());
                                    editor.putString("email",currentUser.getEmail());
                                    editor.commit();
                                    if(currentUser.getRole().equals("client"))
                                    {
                                        Intent intent = new Intent(LoginFragment.this.getActivity(), ClientProfile.class);
                                        getActivity().startActivity(intent);
                                        getActivity().finishActivity(0);
                                    }
                                }
                                else
                                {
                                    passED.setError("Please Check Your Password");
                                }
                            }
                            else {
                                emailED.setError("Email does not exist");
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
             }
            }
        });
        return v;
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}