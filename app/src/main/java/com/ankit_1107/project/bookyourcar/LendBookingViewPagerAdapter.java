package com.ankit_1107.project.bookyourcar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LendBookingViewPagerAdapter extends FragmentPagerAdapter {

    LendRequestBookingFragment lendRequestBookingFragment;
    LendCurrentBookingFragment lendCurrentBookingFragment;
    LendCompletedBookingFragment lendCompletedBookingFragment;

    public LendBookingViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        lendCompletedBookingFragment = new LendCompletedBookingFragment();
        lendCurrentBookingFragment = new LendCurrentBookingFragment();
        lendRequestBookingFragment = new LendRequestBookingFragment();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = lendRequestBookingFragment;
        else if (position == 1)
            fragment = lendCurrentBookingFragment;
        else if (position == 2)
            fragment = lendCompletedBookingFragment;

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
            title = "Current";
        else if (position == 2)
            title = "Completed";
        return title;
    }
}
