package com.ankit_1107.project.bookyourcar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class LendCompletedBookingFragment extends Fragment {
    private LendCompletedBookingListener listener;
    public RecyclerView recyclerViewLendCompleted;

    public LendCompletedBookingFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lend_completed_booking, container, false);
        recyclerViewLendCompleted = view.findViewById(R.id.lend_booking_completed_recycler_view);
        listener.setRecyclerViewCompletedBooking(recyclerViewLendCompleted);
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LendCompletedBookingListener){
            listener = (LendCompletedBookingListener) context;
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

    public interface LendCompletedBookingListener{
        void setRecyclerViewCompletedBooking(RecyclerView recyclerView);
    }
}