package com.example.movieticketproject.HomeDashborad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movieticketproject.Adapters.TicketRecyclerAdpater;
import com.example.movieticketproject.Decorators.CustomDecorator;
import com.example.movieticketproject.Models.DataAdpater.TicketData;
import com.example.movieticketproject.Models.SoldTicket;
import com.example.movieticketproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    RecyclerView rv_TicketHistory;
    ArrayList<TicketData> tickets ;
    DatabaseReference reference = null;
    DatabaseReference reference2 = null;
    DatabaseReference reference3 = null;
    SharedPreferences pref;
    TextView emptyView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_history, container, false);
        rv_TicketHistory = v.findViewById(R.id.history_ticket_rv);
        emptyView = v.findViewById(R.id.tv_empty_rv);
        tickets = new ArrayList<>();
        pref = getActivity().getSharedPreferences("current_user", Context.MODE_PRIVATE);
        reference = FirebaseDatabase.getInstance().getReference().child("SoldTickets");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Tickets");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Films");
        Query query = reference.orderByChild("client").equalTo(pref.getString("email",""));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    emptyView.setVisibility(View.GONE);
                    for(DataSnapshot data:snapshot.getChildren())
                    {
                        String idTicket =  data.child("idTicket").getValue(String.class);
                        Query query1 = reference2.orderByChild("id").equalTo(idTicket);
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    DataSnapshot dataTicket = snapshot.getChildren().iterator().next();
                                    String idFilm = dataTicket.child("idFilm").getValue(String.class);
                                    String seat = dataTicket.child("seat").getValue(String.class);
                                    Query query2 = reference3.orderByChild("id").equalTo(idFilm);
                                    query2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists())
                                            {
                                                DataSnapshot dataFilm = snapshot.getChildren().iterator().next();
                                                String name = dataFilm.child("name").getValue(String.class);
                                                String date = dataFilm.child("showdatetime").getValue(String.class);
                                                String[] datetime = date.trim().split(" ");
                                                String showdate = datetime[0];
                                                String showtime = datetime[1];
                                                String room = dataFilm.child("room").getValue(String.class);
                                                String price = dataFilm.child("price").getValue(String.class)+"DT";
                                                TicketData ticketData = new TicketData();
                                                ticketData.setId(idTicket);
                                                ticketData.setTitle(name);
                                                ticketData.setHolder(pref.getString("fullname",""));
                                                ticketData.setPrice(price);
                                                ticketData.setRoom(room);
                                                ticketData.setDate(showdate);
                                                ticketData.setTime(showtime);
                                                ticketData.setSeat(seat);
                                                tickets.add(ticketData);
                                            }
                                            else
                                            {
                                                System.out.println("No DATA FOR MOVIE");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            System.out.println("DATA FOR MOVIE CANCELLED");
                                        }
                                    });
                                }
                                else
                                {
                                    System.out.println("No DATA FOR TICKET");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.out.println("DATA FOR TICKET CANCELLED");
                            }
                        });
                    }
                    TicketRecyclerAdpater recyclerAdpater = new TicketRecyclerAdpater(getActivity(),tickets);
                    CustomDecorator customDecorator = new CustomDecorator(15,0,0,0);
                    rv_TicketHistory.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    rv_TicketHistory.addItemDecoration(customDecorator);
                    rv_TicketHistory.setAdapter(recyclerAdpater);
                }
                else
                {
                    System.out.println("No DATA FOR HISTORY");
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("DATA FOR HISTORY CANCELLED");
            }
        });

        return v;
    }
}