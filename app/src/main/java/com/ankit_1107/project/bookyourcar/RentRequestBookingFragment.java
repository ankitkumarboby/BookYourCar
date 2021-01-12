package com.ankit_1107.project.bookyourcar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RentRequestBookingFragment extends Fragment {
    private RentRequestBookingListener listener;
    public RecyclerView recyclerViewRentRequest;

    public RentRequestBookingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_request_booking, container, false);
        onAttachToParentFragment(getParentFragment());
        recyclerViewRentRequest = view.findViewById(R.id.rent_booking_request_recycler_view);
        listener.setRecyclerViewRequestBooking(recyclerViewRentRequest);
        return view;
    }

    public void onAttachToParentFragment(Fragment fragment) {
        try {
            listener = (RentRequestBookingListener)fragment;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString());
        }
    }

    public interface RentRequestBookingListener{
        void setRecyclerViewRequestBooking(RecyclerView recyclerView);
    }

}