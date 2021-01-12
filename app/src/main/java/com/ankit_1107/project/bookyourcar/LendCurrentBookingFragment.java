package com.ankit_1107.project.bookyourcar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class LendCurrentBookingFragment extends Fragment {
    private LendCurrentBookingListener listener;
    public RecyclerView recyclerViewLendCurrent;

    public LendCurrentBookingFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lend_current_booking, container, false);
        recyclerViewLendCurrent = view.findViewById(R.id.lend_booking_current_recycler_view);
        listener.setRecyclerViewCurrentBooking(recyclerViewLendCurrent);
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LendCurrentBookingListener){
            listener = (LendCurrentBookingListener) context;
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

    public interface LendCurrentBookingListener{
        void setRecyclerViewCurrentBooking(RecyclerView recyclerView);
    }
}