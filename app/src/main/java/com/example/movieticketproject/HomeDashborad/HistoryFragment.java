package com.example.movieticketproject.HomeDashborad;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieticketproject.Adapters.TicketRecyclerAdpater;
import com.example.movieticketproject.Decorators.CustomDecorator;
import com.example.movieticketproject.Models.SoldTicket;
import com.example.movieticketproject.R;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    RecyclerView rv_TicketHistory;
    ArrayList<SoldTicket> tickets ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_history, container, false);
        rv_TicketHistory = v.findViewById(R.id.history_ticket_rv);
        tickets = new ArrayList<>();
        SoldTicket ticket = new SoldTicket();
        ticket.setTitle("Joker");
        ticket.setHolder("Abdelhamid Ben Abdelfettah");
        ticket.setRoom("08");
        ticket.setPrice("20DT");
        ticket.setDate("01/01/2021");
        ticket.setTime("14:00");
        ticket.setSeat("B03");

        SoldTicket ticket2 = new SoldTicket();
        ticket2.setTitle("Frozen");
        ticket2.setHolder("Abdelhamid Ben Abdelfettah");
        ticket2.setRoom("04");
        ticket2.setPrice("15DT");
        ticket2.setDate("01/02/2021");
        ticket2.setTime("16:00");
        ticket2.setSeat("A03");

        tickets.add(ticket);
        tickets.add(ticket2);


        TicketRecyclerAdpater recyclerAdpater = new TicketRecyclerAdpater(getActivity(),tickets);
        CustomDecorator customDecorator = new CustomDecorator(15,0,0,0);
        rv_TicketHistory.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rv_TicketHistory.addItemDecoration(customDecorator);
        rv_TicketHistory.setAdapter(recyclerAdpater);
        return v;
    }
}