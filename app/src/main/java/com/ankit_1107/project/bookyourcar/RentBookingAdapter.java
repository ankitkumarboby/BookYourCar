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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RentBookingAdapter extends RecyclerView.Adapter<RentBookingAdapter.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<Booking> dataList;
    int type;    //0-> requested  1->current   2-> completed || rejected
    FirebaseFirestore db;
    RentBookingFragment fragment;

    public RentBookingAdapter(Context context, List<Booking> dataList, int type, RentBookingFragment fragment){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.type = type;
        db = FirebaseFirestore.getInstance();
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rent_booking_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Booking booking = dataList.get(position);
        Picasso.get().load(booking.getCarPhotoUri()).into(holder.imageView);
        holder.carName.setText(booking.getGetCarFullName());
        holder.amountPaid.setText(String.valueOf(booking.getAmountPaid()));
        holder.dateFrom.setText(booking.getDateFrom());
        holder.dateTo.setText(booking.getToDate());
        holder.phoneNumber.setText(booking.getOwnerPhoneNumber());

        holder.buttonReject.setVisibility(View.VISIBLE);
        holder.buttonAccept.setVisibility(View.VISIBLE);
        holder.buttonReject.setClickable(true);
        holder.buttonAccept.setClickable(true);

        if(type == 0){
            holder.buttonAccept.setText("REQUEST SENT");
            holder.buttonAccept.setAlpha(0.5f);
            holder.buttonReject.setText("CANCEL REQUEST");

            holder.buttonAccept.setClickable(false);
            holder.buttonReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //cancel request
                    Task task1 = db.collection("bookings").document(booking.getBookingId()).delete();

                    Task task2 = db.collection("users").document(booking.getUserId())
                            .update("bookingHistoryPerson", FieldValue.arrayRemove(booking.getBookingId()));

                    Task task3 = db.collection("cars").document(booking.getCarId())
                            .update("bookingHistoryCar", FieldValue.arrayRemove(booking.getBookingId()));

                    Task task = Tasks.whenAllSuccess(task1,task2,task3).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> objects) {
                            DocumentReference ref = db.collection("cars").document(booking.getCarId());
                            for(Long i = booking.getNoDateFrom() ; i<=booking.getNoDateTo() ; ++i){
                                ref.update("bookedNoDate",FieldValue.arrayRemove(i));
                            }
                            //Notification for Car owner
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            String bodyNotification = "The request for "+booking.getGetCarFullName()+" is cancelled";
                            FCMUtil.sendNotification(requestQueue,booking.getOwnerId(),bodyNotification);

                            fragment.refreshRecyclerView();
                        }
                    });
                }
            });

        }
        else if(type == 1){
            holder.buttonAccept.setText("BOOKING CONFIRMED");
            holder.buttonAccept.setAlpha(0.5f);
            holder.buttonReject.setText("CANCEL BOOKING");

            holder.buttonAccept.setClickable(false);
            holder.buttonReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //cancel booking
                    Task task1 = db.collection("bookings").document(booking.getBookingId()).delete();

                    Task task2 = db.collection("users").document(booking.getUserId())
                            .update("bookingHistoryPerson", FieldValue.arrayRemove(booking.getBookingId()));

                    Task task3 = db.collection("cars").document(booking.getCarId())
                            .update("bookingHistoryCar", FieldValue.arrayRemove(booking.getBookingId()));

                    Task task = Tasks.whenAllSuccess(task1,task2,task3).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> objects) {
                            DocumentReference ref = db.collection("cars").document(booking.getCarId());
                            for(Long i = booking.getNoDateFrom() ; i<=booking.getNoDateTo() ; ++i){
                                ref.update("bookedNoDate",FieldValue.arrayRemove(i));
                            }
                            //Notification for Car owner
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            String bodyNotification = "The booking for "+booking.getGetCarFullName()+" is cancelled";
                            FCMUtil.sendNotification(requestQueue,booking.getOwnerId(),bodyNotification);

                            fragment.refreshRecyclerView();
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
        ImageView imageView;
        TextView carName,phoneNumber,dateFrom,dateTo,amountPaid;
        Button buttonAccept,buttonReject;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.car_image_rent_booking_item);
            carName = itemView.findViewById(R.id.car_name_rent_booking_item);
            phoneNumber = itemView.findViewById(R.id.phone_rent_booking_item);
            dateFrom = itemView.findViewById(R.id.date_from_rent_booking_item);
            dateTo = itemView.findViewById(R.id.date_to_rent_booking_item);
            amountPaid = itemView.findViewById(R.id.amount_paid_rent_booking_item);
            buttonAccept = itemView.findViewById(R.id.button_accept_rent_booking_item);
            buttonReject = itemView.findViewById(R.id.button_reject_rent_booking_item);
            cardView = itemView.findViewById(R.id.card_view_rent_booking_item);

        }
    }
}
