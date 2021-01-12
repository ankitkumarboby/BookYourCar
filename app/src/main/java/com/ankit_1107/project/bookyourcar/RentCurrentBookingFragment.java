package com.ankit_1107.project.bookyourcar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RentCurrentBookingFragment extends Fragment {
    private RentCurrentBookingListener listener;
    public RecyclerView recyclerViewRentCurrent;

    public RentCurrentBookingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_current_booking, container, false);
        onAttachToParentFragment(getParentFragment());
        recyclerViewRentCurrent = view.findViewById(R.id.rent_booking_current_recycler_view);
        listener.setRecyclerViewCurrentBooking(recyclerViewRentCurrent);
        return view;

    }
    public void onAttachToParentFragment(Fragment fragment) {
        try {
            listener = (RentCurrentBookingListener)fragment;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString());
        }
    }

    public interface RentCurrentBookingListener{
        void setRecyclerViewCurrentBooking(RecyclerView recyclerView);
    }
}