package com.ankit_1107.project.bookyourcar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class LendRequestBookingFragment extends Fragment {
    private LendRequestBookingListener listener;
    public RecyclerView recyclerViewLendRequest;

    public LendRequestBookingFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lend_request_booking, container, false);
        recyclerViewLendRequest = view.findViewById(R.id.lend_booking_request_recycler_view);
        listener.setRecyclerViewRequestBooking(recyclerViewLendRequest);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LendRequestBookingListener){
            listener = (LendRequestBookingListener) context;
        }
        else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    public interface LendRequestBookingListener{
        void setRecyclerViewRequestBooking(RecyclerView recyclerView);
    }
}