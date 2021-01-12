package com.ankit_1107.project.bookyourcar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter {
    public static final int CAR_RENT_DETAIL_REQUEST = 55;
    List<Car> dataList;
    LayoutInflater layoutInflater;
    Context context;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    Calendar calendarFrom,calendarTo;
    double locationLatitude,locationLongitude;

    public CarListAdapter(Context context, List<Car> dataList ) {
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            View view = layoutInflater.inflate(R.layout.blank_top_view,parent,false);
            return new CarListAdapter.BlankViewHolder(view);
        }
        View view = layoutInflater.inflate(R.layout.car_list_item,parent,false);
        return  new CarListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(position == 0){
            return;
        }
        Car carItem = dataList.get(position-1);
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.modelNameTextView.setText(carItem.getModelName());
        viewHolder.brandNameTextView.setText(carItem.getBrandName());
        viewHolder.priceTextView.setText(String.valueOf(carItem.getPrice()));
        viewHolder.fuelTypeTextView.setText(carItem.getFuelType());
        viewHolder.seatCountTextView.setText(String.valueOf(carItem.getSeatCount()));
        StorageReference ref = storageReference.child("cars").child(carItem.getVehicleNumber()).child("mainImage");
        Picasso.get().load(carItem.getLendCarUri()).into(viewHolder.carListItemImageView);
        //activityMain -> activityRentDetails
        viewHolder.buttonListItemBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CarDetailsRent.class);

                intent.putExtra("itemCar",dataList.get(position-1));
                intent.putExtra("itemCarId",  dataList.get(position-1).getCarId());
                intent.putExtra("calFrom",calendarFrom);
                intent.putExtra("calTo",calendarTo);
                intent.putExtra("personLatitude",locationLatitude);
                intent.putExtra("personLongitude",locationLongitude);
                ((MainActivity)context).startActivityForResult(intent,CAR_RENT_DETAIL_REQUEST);
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        if(position == 0) return 1;
        else return 0;
    }

    @Override
    public int getItemCount() {
        return dataList.size() +1 ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView brandNameTextView,modelNameTextView,priceTextView,fuelTypeTextView,seatCountTextView;
        ImageView carListItemImageView;
        Button buttonListItemBook;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            brandNameTextView = itemView.findViewById(R.id.brand_name_item);
            modelNameTextView =itemView.findViewById(R.id.model_name_item);
            priceTextView = itemView.findViewById(R.id.price_item);
            fuelTypeTextView = itemView.findViewById(R.id.fuel_type_item);
            carListItemImageView = itemView.findViewById(R.id.car_list_item_demo);
            buttonListItemBook = itemView.findViewById(R.id.button_list_item_book);
            seatCountTextView = itemView.findViewById(R.id.seat_count_item);

        }
    }
    public class BlankViewHolder extends RecyclerView.ViewHolder{

        public BlankViewHolder(@NonNull View itemView){
            super(itemView);

        }
    }
    public void setLatlng(double latitude,double longitude){
        locationLatitude = latitude;
        locationLongitude = longitude;
    }
    public void setCal(Calendar calFrom , Calendar calTo){
//        calFrom.set(calFrom.get(Calendar.YEAR) , calFrom.get(Calendar.MONTH) , calFrom.get(Calendar.DAY_OF_MONTH) , 0 ,0,0);
//        calTo.set(calTo.get(Calendar.YEAR) , calTo.get(Calendar.MONTH) , calTo.get(Calendar.DAY_OF_MONTH) , 0 ,0,0);

        this.calendarFrom = calFrom;
        this.calendarTo = calTo;
    }
}
