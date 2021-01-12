package com.ankit_1107.project.bookyourcar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RentCompletedBookingFragment extends Fragment {
    private RentCompletedBookingListener listener;
    public RecyclerView recyclerViewRentCompleted;

    public RentCompletedBookingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_completed_booking, container, false);
        onAttachToParentFragment(getParentFragment());
        recyclerViewRentCompleted = view.findViewById(R.id.rent_booking_completed_recycler_view);
        listener.setRecyclerViewCompletedBooking(recyclerViewRentCompleted);
        return view;
    }


    public void onAttachToParentFragment(Fragment fragment) {
        try {
            listener = (RentCompletedBookingListener)fragment;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString());
        }
    }

    public interface RentCompletedBookingListener{
        void setRecyclerViewCompletedBooking(RecyclerView recyclerView);
    }
}