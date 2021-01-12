package com.ankit_1107.project.bookyourcar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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

public class RentBookingFragment extends Fragment implements
        RentCompletedBookingFragment.RentCompletedBookingListener,
        RentRequestBookingFragment.RentRequestBookingListener,
        RentCurrentBookingFragment.RentCurrentBookingListener {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    RentBookingViewPagerAdapter viewPagerAdapter;

    String userId;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    List<Booking> requestBooking,currentBooking,completedBooking;
    RentBookingAdapter requestAdapter, currentAdapter, completedAdapter;

    public RentBookingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_booking, container, false);

        completedBooking = new ArrayList<Booking>();
        requestBooking = new ArrayList<Booking>();
        currentBooking = new ArrayList<Booking>();

        requestAdapter = new RentBookingAdapter(getActivity(),requestBooking,0,RentBookingFragment.this);
        completedAdapter = new RentBookingAdapter(getActivity(),completedBooking,2,RentBookingFragment.this);
        currentAdapter = new RentBookingAdapter(getActivity(),currentBooking,1,RentBookingFragment.this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        tabLayout = view.findViewById(R.id.tab_layout_rent_booking);
        viewPager = view.findViewById(R.id.view_pager_rent_booking);
        viewPagerAdapter = new RentBookingViewPagerAdapter(getChildFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        loadBookingFromDb();

        return view;
    }

    @Override
    public void setRecyclerViewCompletedBooking(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(completedAdapter);
        completedAdapter.notifyDataSetChanged();
    }

    private void loadBookingFromDb(){

        db.collection("users").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Person person = documentSnapshot.toObject(Person.class);
                        for(String bookingId : person.getBookingHistoryPerson()){
                            db.collection("bookings").document(bookingId).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Booking bookingItem = documentSnapshot.toObject(Booking.class);
                                            if(bookingItem.getStatus()==0){
                                                requestBooking.add(bookingItem);
                                                requestAdapter.notifyDataSetChanged();
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
        requestBooking.clear();
        currentBooking.clear();
        completedBooking.clear();
        requestAdapter.notifyDataSetChanged();
        currentAdapter.notifyDataSetChanged();
        completedAdapter.notifyDataSetChanged();
        loadBookingFromDb();
    }

    @Override
    public void setRecyclerViewCurrentBooking(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(currentAdapter);
        currentAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRecyclerViewRequestBooking(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(requestAdapter);
        requestAdapter.notifyDataSetChanged();
    }
}