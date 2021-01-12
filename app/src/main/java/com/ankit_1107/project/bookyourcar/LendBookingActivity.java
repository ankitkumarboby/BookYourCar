package com.ankit_1107.project.bookyourcar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class LendBookingActivity extends AppCompatActivity
        implements  LendRequestBookingFragment.LendRequestBookingListener ,
                    LendCurrentBookingFragment.LendCurrentBookingListener,
                    LendCompletedBookingFragment.LendCompletedBookingListener {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    LendBookingViewPagerAdapter viewPagerAdapter;

    String userId,carId;


    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    List<Booking> completedBooking,requestedBooking,currentBooking;
    LendBookingAdapter requestedAdapter,currentAdapter,completedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_booking);

        completedBooking = new ArrayList<Booking>();
        requestedBooking = new ArrayList<Booking>();
        currentBooking = new ArrayList<Booking>();

        requestedAdapter = new LendBookingAdapter(this,requestedBooking,0);
        completedAdapter = new LendBookingAdapter(this,completedBooking,2);
        currentAdapter = new LendBookingAdapter(this,currentBooking,1);

        Intent intent = getIntent();
        carId = intent.getStringExtra("carId");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        viewPager = findViewById(R.id.view_pager_lend_booking);
        tabLayout = findViewById(R.id.tab_layout_lend_booking);

        viewPagerAdapter = new LendBookingViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        // It is used to join TabLayout with ViewPager.
        tabLayout.setupWithViewPager(viewPager);


        loadBookingFromDb();


    }

    int tmp=0;
    @Override
    public void setRecyclerViewRequestBooking(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(requestedAdapter);
        requestedAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRecyclerViewCurrentBooking(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(currentAdapter);
        currentAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRecyclerViewCompletedBooking(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(completedAdapter);
        completedAdapter.notifyDataSetChanged();
    }


    private void loadBookingFromDb(){

        db.collection("cars").document(carId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Car car = documentSnapshot.toObject(Car.class);
                        for(String bookingId : car.getBookingHistoryCar()){
                            db.collection("bookings").document(bookingId).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Booking bookingItem = documentSnapshot.toObject(Booking.class);
                                            if(bookingItem.getStatus()==0){
                                                requestedBooking.add(bookingItem);
                                                requestedAdapter.notifyDataSetChanged();
                                            }
                                            else if(bookingItem.getStatus()==1){
                                                currentBooking.add(bookingItem);
                                                currentAdapter.notifyDataSetChanged();
                                            }
                                            else{
                                                completedBooking.add(bookingItem);
                                                completedAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    public void refreshRecyclerView(){
        requestedBooking.clear();
        currentBooking.clear();
        completedBooking.clear();
        requestedAdapter.notifyDataSetChanged();
        currentAdapter.notifyDataSetChanged();
        completedAdapter.notifyDataSetChanged();
        loadBookingFromDb();
    }



}