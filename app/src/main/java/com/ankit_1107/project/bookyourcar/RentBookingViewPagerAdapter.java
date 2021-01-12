package com.ankit_1107.project.bookyourcar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RentBookingViewPagerAdapter extends FragmentPagerAdapter {

    RentRequestBookingFragment rentRequestBookingFragment;
    RentCompletedBookingFragment rentCompletedBookingFragment;
    RentCurrentBookingFragment rentCurrentBookingFragment;

    public RentBookingViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        rentCompletedBookingFragment = new RentCompletedBookingFragment();
        rentRequestBookingFragment = new RentRequestBookingFragment();
        rentCurrentBookingFragment = new RentCurrentBookingFragment();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = rentRequestBookingFragment;
        else if (position == 1)
            fragment = rentCurrentBookingFragment;
        else if (position == 2)
            fragment = rentCompletedBookingFragment;

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "Requests";
        else if (position == 1)
            title = "Confirmed Booking";
        else if (position == 2)
            title = "Past Booking";
        return title;
    }
}
