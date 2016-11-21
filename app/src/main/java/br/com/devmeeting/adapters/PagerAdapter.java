package br.com.devmeeting.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.devmeeting.R;
import br.com.devmeeting.fragments.EventsFragment;
import br.com.devmeeting.fragments.MapFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    public static final int NUMBER_OF_TABS = 2;
    public static final int TAB_POSITION_MAPS = 1;
    public static final int TAB_POSITION_EVENTS = 0;

    private FragmentManager fragmentManager;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        String name = this.makeFragmentName(R.id.pager, position);
        Fragment fragmentInstance = this.fragmentManager.findFragmentByTag(name);
        switch (position) {
            case TAB_POSITION_EVENTS:
                return (fragmentInstance == null) ? EventsFragment.newInstance() : fragmentInstance;
            case TAB_POSITION_MAPS:
                return (fragmentInstance == null) ? MapFragment.newInstance() : fragmentInstance;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
}
