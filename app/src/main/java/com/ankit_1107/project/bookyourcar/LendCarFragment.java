package com.ankit_1107.project.bookyourcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class LendCarFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ArrayList<Car> lendedCarList = new ArrayList<Car>();
    RecyclerView carLendRecyclerView;
    CarLendListAdapter carLendListAdapter;
    Button buttonGotoLendCar;
    Person currUser;

    public LendCarFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_lend_car, container, false);

        carLendRecyclerView = view.findViewById(R.id.car_lended_list_recycler_view);
        carLendListAdapter = new CarLendListAdapter(getActivity(),lendedCarList);
        carLendRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        carLendRecyclerView.setAdapter(carLendListAdapter);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        lendedCarList.clear();

        firebaseFirestore.collection("users").document(firebaseUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        currUser = documentSnapshot.toObject(Person.class);
                        for(String carId : currUser.getCarsOwnedByPerson()){
                            firebaseFirestore.collection("cars").document(carId).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            lendedCarList.add(documentSnapshot.toObject(Car.class));
                                            carLendListAdapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                    }
                });


        buttonGotoLendCar = view.findViewById(R.id.button_goto_lend_car);
        buttonGotoLendCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LendCarActivity.class);
                startActivityForResult(intent,MainActivity.CAR_LEND_ACTIVITY);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == MainActivity.CAR_LEND_ACTIVITY){
            if(resultCode == RESULT_OK){

                lendedCarList.clear();

                firebaseFirestore.collection("users").document(firebaseUser.getUid()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                currUser = documentSnapshot.toObject(Person.class);
                                for(String carId : currUser.getCarsOwnedByPerson()){
                                    firebaseFirestore.collection("cars").document(carId).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    lendedCarList.add(documentSnapshot.toObject(Car.class));
                                                    carLendListAdapter.notifyDataSetChanged();
                                                }
                                            });
                                }
                            }
                        });
            }
        }

    }
}