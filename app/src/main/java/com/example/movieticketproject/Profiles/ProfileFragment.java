package com.example.movieticketproject.Profiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieticketproject.MainActivity;
import com.example.movieticketproject.Models.Users;
import com.example.movieticketproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    TextView tv_fullname_profile;
    EditText ed_fullName_profil,ed_email_profil,ed_birthdate_profil;
    ImageView ic_back_clientprof;
    String fullname, birthdate,email;
    DatabaseReference reference = null;
    SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        tv_fullname_profile = v.findViewById(R.id.tv_fullname_profile);
        ed_fullName_profil = v.findViewById(R.id.ed_fullName_profil);
        ed_email_profil = v.findViewById(R.id.ed_email_profil);
        ed_birthdate_profil = v.findViewById(R.id.ed_birthdate_profil);
        ic_back_clientprof = v.findViewById(R.id.ic_back_clientprof);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        pref = getActivity().getSharedPreferences("current_user", Context.MODE_PRIVATE);

        email=pref.getString("email","");
        fullname = pref.getString("fullname","");

        ed_fullName_profil.setText(fullname);
        ed_email_profil.setText(email);
        tv_fullname_profile.setText(fullname);
        Query query = reference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DataSnapshot data = snapshot.getChildren().iterator().next();
                    Users user = data.getValue(Users.class);
                    ed_birthdate_profil.setText(user.getBirthdate());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ic_back_clientprof.setOnClickListener(new View.OnClickListener() {
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