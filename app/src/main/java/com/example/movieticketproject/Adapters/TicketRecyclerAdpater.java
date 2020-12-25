package com.example.movieticketproject.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.Models.SoldTicket;
import com.example.movieticketproject.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TicketRecyclerAdpater extends RecyclerView.Adapter<TicketViewHolder> {

    Context context;
    ArrayList<SoldTicket> tickets ;

    public TicketRecyclerAdpater(Context context,ArrayList<SoldTicket> tickets)
    {
        this.context = context;
        this.tickets = tickets;
    }


    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        CardView view = (CardView)inflater.inflate(R.layout.customticket, parent,false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        holder.titleTV.setText(tickets.get(position).getTitle());
        holder.holderTV.setText(tickets.get(position).getHolder());
        holder.priceTV.setText(tickets.get(position).getPrice());
        holder.seatTV.setText(tickets.get(position).getSeat());
        holder.dateTV.setText(tickets.get(position).getDate());
        holder.timeTV.setText(tickets.get(position).getTime());
        holder.roomTV.setText(tickets.get(position).getRoom());
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}

class TicketViewHolder extends RecyclerView.ViewHolder{

    TextView titleTV,holderTV,priceTV,seatTV,dateTV,timeTV,roomTV;

    public TicketViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTV = itemView.findViewById(R.id.ticket_moviename);
        holderTV = itemView.findViewById(R.id.ticket_holdername);
        priceTV = itemView.findViewById(R.id.ticket_movieprice);
        seatTV = itemView.findViewById(R.id.ticket_movieseat);
        dateTV = itemView.findViewById(R.id.ticket_moviedate);
        timeTV = itemView.findViewById(R.id.ticket_movietime);
        roomTV = itemView.findViewById(R.id.ticket_movieroom);

    }
}
