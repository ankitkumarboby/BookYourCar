package com.ankit_1107.project.bookyourcar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LendBookingAdapter extends RecyclerView.Adapter<LendBookingAdapter.ViewHolder> {
    List<Booking> dataList;
    Context context;
    LayoutInflater layoutInflater;
    int type;   //0-> requested  1->current   2-> completed || rejected
    FirebaseFirestore db;

    public LendBookingAdapter(Context context , List<Booking> dataList,int type){
        this.dataList = dataList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.type = type;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.lend_booking_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Booking booking = dataList.get(position);
        Picasso.get().load(booking.getPersonPhotoUri()).into(holder.personImage);
        holder.personName.setText(booking.getUserName());
        holder.personPhoneNumber.setText(booking.getUserPhoneNumber());
        holder.dateFrom.setText(booking.getDateFrom());
        holder.dateTo.setText(booking.getToDate());
        holder.amountPaid.setText(String.valueOf(booking.getAmountPaid()));

        holder.buttonAccept.setVisibility(View.VISIBLE);
        holder.buttonReject.setVisibility(View.VISIBLE);
        holder.buttonReject.setClickable(true);
        holder.buttonAccept.setClickable(true);

        if(type == 0){

            holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("bookings").document(booking.getBookingId()).update("status",1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Notification for Car renter
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    String bodyNotification = "Your booking of "+booking.getGetCarFullName()+" is accepted";
                                    FCMUtil.sendNotification(requestQueue,booking.getUserId(),bodyNotification);

                                    ((LendBookingActivity)context).refreshRecyclerView();
                                }
                            });

                }
            });

            holder.buttonReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("bookings").document(booking.getBookingId()).update("status",3)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DocumentReference ref = db.collection("cars").document(booking.getCarId());
                                    for(Long i = booking.getNoDateFrom() ; i<=booking.getNoDateTo() ; ++i){
                                        ref.update("bookedNoDate", FieldValue.arrayRemove(i));
                                    }
                                    //Notification for Car renter
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    String bodyNotification = "Your booking of "+booking.getGetCarFullName()+" is rejected";
                                    FCMUtil.sendNotification(requestQueue,booking.getUserId(),bodyNotification);

                                    ((LendBookingActivity)context).refreshRecyclerView();
                                    ((LendBookingActivity)context).refreshRecyclerView();
                                }
                            });

                }
            });

        }
        else if(type == 1){
            holder.buttonReject.setVisibility(View.GONE);
            holder.buttonAccept.setText("MARK COMPLETED");
            holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("bookings").document(booking.getBookingId()).update("status",2)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DocumentReference ref = db.collection("cars").document(booking.getCarId());
                                    for(Long i = booking.getNoDateFrom() ; i<=booking.getNoDateTo() ; ++i){
                                        ref.update("bookedNoDate",FieldValue.arrayRemove(i));
                                    }
                                    //Notification for Car renter
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    String bodyNotification = "Your booking of "+booking.getGetCarFullName()+" is completed";
                                    FCMUtil.sendNotification(requestQueue,booking.getUserId(),bodyNotification);

                                    ((LendBookingActivity)context).refreshRecyclerView();
                                }
                            });
                }
            });
        }
        else{
            holder.cardView.setAlpha(0.5f);
            if(booking.getStatus()==2){
                holder.buttonReject.setVisibility(View.GONE);
                holder.buttonAccept.setText("COMPLETED");
                holder.buttonAccept.setClickable(false);
            }
            else{
                holder.buttonAccept.setVisibility(View.GONE);
                holder.buttonReject.setText("REJECTED");
                holder.buttonReject.setClickable(false);
            }

        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView personImage;
        TextView personName,personPhoneNumber,dateFrom,dateTo,amountPaid;
        Button buttonAccept,buttonReject;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personImage = itemView.findViewById(R.id.person_image_lend_booking_item);
            personName = itemView.findViewById(R.id.person_name_lend_booking_item);
            personPhoneNumber = itemView.findViewById(R.id.phone_lend_booking_item);
            dateFrom = itemView.findViewById(R.id.date_from_lend_booking_item);
            dateTo = itemView.findViewById(R.id.date_to_lend_booking_item);
            buttonAccept = itemView.findViewById(R.id.button_accept_lend_booking_item);
            buttonReject = itemView.findViewById(R.id.button_reject_lend_booking_item);
            amountPaid = itemView.findViewById(R.id.amount_paid_lend_booking_item);
            cardView = itemView.findViewById(R.id.card_view_lend_booking_item);

        }
    }
}
