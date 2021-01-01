package com.example.movieticketproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieticketproject.Models.SoldTicket;
import com.example.movieticketproject.Models.Ticket;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CheckoutActivity extends AppCompatActivity {
    TextView tvCardHolder,tvCardNumber,tvExpDate,tvCCV;
    TextInputEditText cardHolderED,cardNumberED,cardCCVED,cardExpMED,cardExpYED;
    ImageView backbutton ;
    Button checkout;
    DatabaseReference reference = null;
    String idFilm;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);

        Bundle b = getIntent().getExtras();
        idFilm = b.getString("idFilm");

        pref = getSharedPreferences("current_user", Context.MODE_PRIVATE);;

        tvCardHolder = findViewById(R.id.tv_cardHolder_cc);
        tvCardNumber = findViewById(R.id.tv_cardNumber_cc);
        tvCCV = findViewById(R.id.tv_cardCCV_cc);
        tvExpDate = findViewById(R.id.tv_cardExp_cc);

        tvCardHolder.setText("");
        tvCardNumber.setText("");
        tvCCV.setText("");
        tvExpDate.setText("");

        cardHolderED=findViewById(R.id.ti_cardHolder_cc);
        cardNumberED=findViewById(R.id.ti_cardNumber_cc);
        cardCCVED=findViewById(R.id.ti_cardCCV_cc);
        cardExpMED=findViewById(R.id.ti_cardMonth_cc);
        cardExpYED=findViewById(R.id.ti_cardYear_cc);

        checkout = findViewById(R.id.btn_checkout_cc);
        backbutton = findViewById(R.id.ic_back_cc);

        reference = FirebaseDatabase.getInstance().getReference();
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutActivity.this.finish();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isChecked = true;
                String cardHolder = cardHolderED.getText().toString();
                String cardNumber = cardNumberED.getText().toString();
                String cardCCV = cardCCVED.getText().toString();
                String cardYear = cardExpYED.getText().toString();
                String cardExp = cardExpMED.getText().toString();
                if(cardExp.equals(""))
                {
                    cardExp = "0";
                }
                if(cardYear.equals(""))
                {
                    cardYear = "0";
                }
                if(cardHolder.equals(""))
                {
                    cardHolderED.setError("Enter a valid name");
                    isChecked = false;
                }
                if(cardNumber.length() != 16)
                {
                    cardNumberED.setError("Enter a valid card number");
                    isChecked = false;
                }
                if(cardCCV.length() != 3)
                {
                    cardCCVED.setError("Enter a valid CCV");
                    isChecked = false;
                }
                if((cardExp.length() != 2) && ((Integer.parseInt(cardExp) > 12) || (Integer.parseInt(cardExp) < 1)))
                {
                    cardExpMED.setError("Enter a valid month");
                    isChecked = false;
                }
                if((cardYear.length() != 2))
                {
                    cardExpYED.setError("Enter a valid year");
                    isChecked = false;
                }

                if(isChecked== true)
                {
                    if(cardHolder.trim().toLowerCase().equals("abdelhamid") && cardNumber.equals("5050404031080218") && cardCCV.equals("123") && cardYear.equals("22") && cardExp.equals("08"))
                    {
                        Query query = reference.child("Ticket").orderByChild("idFilm").equalTo(idFilm);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Ticket ticket = null;
                                if(snapshot.exists())
                                {
                                    for(DataSnapshot data : snapshot.getChildren())
                                    {
                                        if(data.child("sold").getValue(int.class) == 0)
                                        {
                                            data.getRef().child("sold").setValue(1);
                                            ticket= data.getValue(Ticket.class);
                                            break;
                                        }
                                    }
                                    if(ticket != null)
                                    {
                                        SoldTicket soldTicket = new SoldTicket();
                                        soldTicket.setIdTicket(ticket.getId());
                                        soldTicket.setClient(pref.getString("email",""));
                                        reference.child("SoldTickets").push().setValue(soldTicket);
                                        Toast.makeText(CheckoutActivity.this, "Checkout Completed", Toast.LENGTH_SHORT).show();
                                        CheckoutActivity.this.finish();

                                    }
                                    else
                                    {
                                        Toast.makeText(CheckoutActivity.this, "not ticket for you", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(CheckoutActivity.this, "Check your coordinates and try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardHolderED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCardHolder.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardNumberED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCardNumber.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardCCVED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCCV.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardExpMED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvExpDate.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardExpYED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String x = cardExpMED.getText().toString() + "/";
                tvExpDate.setText(x+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}