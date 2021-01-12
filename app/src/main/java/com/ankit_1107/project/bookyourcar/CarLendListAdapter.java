package com.ankit_1107.project.bookyourcar;

import android.content.Context;
import android.content.Intent;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CarLendListAdapter extends RecyclerView.Adapter<CarLendListAdapter.ViewHolder> {
    ArrayList<Car> dataList;
    Context context;
    LayoutInflater layoutInflater;
    FirebaseFirestore db;
    public CarLendListAdapter(Context context,ArrayList<Car> dataList){
        this.dataList = dataList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        db = FirebaseFirestore.getInstance();

    }
    @NonNull
    @Override
    public CarLendListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.lend_car_item,parent,false);
        final CardView downCardView = view.findViewById(R.id.down_card_view_lend_item);
        final ImageView downImageView = view.findViewById(R.id.down_arrow_lend_item);
        final ViewGroup parent1 = parent;
        downCardView.setVisibility(View.GONE);
        downImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(downCardView.getVisibility()== View.GONE){
                    downImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_up_keyboard));
                    Transition transition = new Fade();
                    transition.setDuration(600);
                    transition.addTarget(downCardView);
                    TransitionManager.beginDelayedTransition(parent1, transition);
                    downCardView.setVisibility(View.VISIBLE);
                }
                else{
                    downImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_down_keyboard));
                    Transition transition = new Fade();
                    transition.addTarget(downCardView);

                    TransitionManager.beginDelayedTransition(parent1, transition);
                    downCardView.setVisibility(View.GONE);
                }
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Car itemCar = dataList.get(position);
        holder.carName.setText(itemCar.getBrandName()+" "+itemCar.getModelName());
        holder.vehicleNumber.setText(itemCar.getVehicleNumber());
        holder.cityCar.setText(itemCar.getCity());
        holder.seatCar.setText(String.valueOf(itemCar.getSeatCount()));
        holder.acCar.setText(itemCar.getCheckAC()?"AC":"NON-AC");
        holder.fuelCar.setText(itemCar.getFuelType());
        if(itemCar.getActiveStatus()){
            holder.carStatus.setChecked(true);
        }
        else{
            holder.carStatus.setChecked(false);
        }
        holder.carStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    itemCar.setActiveStatus(true);
                    db.collection("cars").document(itemCar.getCarId()).update("activeStatus",true);
                }
                else{
                    itemCar.setActiveStatus(false);
                    db.collection("cars").document(itemCar.getCarId()).update("activeStatus",false);
                }
            }
        });
        holder.buttonBookingDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LendBookingActivity.class);
                intent.putExtra("carId",itemCar.getCarId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView carName, vehicleNumber;
        ImageView imageDown;
        Button buttonBookingDetail;
        TextView cityCar,seatCar,acCar,fuelCar;
        Switch carStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.car_name_lend_item);
            vehicleNumber = itemView.findViewById(R.id.car_vehicle_number_lend_item);
            imageDown = itemView.findViewById(R.id.down_arrow_lend_item);
            buttonBookingDetail = itemView.findViewById(R.id.button_booking_details_lend_list);
            cityCar = itemView.findViewById(R.id.city_name_lend_item);
            seatCar = itemView.findViewById(R.id.seat_lend_item);
            acCar = itemView.findViewById(R.id.ac_lend_item);
            fuelCar = itemView.findViewById(R.id.fuel_lend_item);
            carStatus = itemView.findViewById(R.id.switch_active_status_lend_item);
        }
    }
}
