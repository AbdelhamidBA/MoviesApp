package com.example.movieticketproject.Authentication;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.movieticketproject.MainActivity;
import com.example.movieticketproject.Models.Users;
import com.example.movieticketproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupFragment extends Fragment {

    EditText ed_fullName_Signup,ed_Birthdate_signup,ed_email_signup,ed_password_signup;
    Button btn_signup_signup;
    ImageView ic_back_signup;
    String fullname, birthdate,email,password;
    Calendar c;
    DatePickerDialog dp;
    DatabaseReference reference = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_signup, container, false);

        ed_fullName_Signup = v.findViewById(R.id.ed_fullName_Signup);
        ed_Birthdate_signup = v.findViewById(R.id.ed_Birthdate_signup);
        ed_email_signup = v.findViewById(R.id.ed_email_signup);
        ed_password_signup = v.findViewById(R.id.ed_password_signup);
        btn_signup_signup = v.findViewById(R.id.btn_signup_signup);
        ic_back_signup = v.findViewById(R.id.ic_back_signup);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        ed_Birthdate_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                dp = new DatePickerDialog(SignupFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int Myear, int Mmonth, int Mday) {
                        ed_Birthdate_signup.setText(Mday + "/" + (Mmonth+1) + "/" + Myear);
                    }
                }, day, month, year);
                dp.show();
            }
        });


        ic_back_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.mainfragement, MainActivity.splashScreen);
                fragmentTransaction.commit();
            }
        });



        btn_signup_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = ed_fullName_Signup.getText().toString();
                birthdate = ed_Birthdate_signup.getText().toString();
                email = ed_email_signup.getText().toString();
                password = ed_password_signup.getText().toString();

                if (!isFullname(fullname) && fullname.equals("")){
                    ed_fullName_Signup.setError("Enter a valid Fullname");
                }
                else if(birthdate.equals(""))
                {
                    ed_Birthdate_signup.setError("Enter a valid Birthdate");

                }else if(!LoginFragment.isEmailValid(email) && email.equals("")){
                    ed_email_signup.setError("Enter a valid Email");
                }
                else if (password.equals("") && isValidPassword(password)){
                    ed_password_signup.setError("Enter a valid Password");
                }
                else
                {
                    Users user = new Users();
                    user.setFullname(fullname);
                    user.setBirthdate(birthdate);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setRole("client");
                    reference.push().setValue(user);
                    Toast.makeText(SignupFragment.this.getActivity(), "User Successfully Registered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    public static boolean isFullname(String str) {
        String expression = "^[a-zA-Z\\s]+";
        return str.matches(expression);
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}