package sachin.com.nanodegreefinalproject.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sachin.com.nanodegreefinalproject.R;
import sachin.com.nanodegreefinalproject.Utils.AppConstants;

import static sachin.com.nanodegreefinalproject.Utils.AppConstants.TAB_NAMES;


public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private FragmentPagerAdapter adapterViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        adapterViewPager = new MyAdapter(getChildFragmentManager());

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(adapterViewPager);
        viewPager.setOffscreenPageLimit(1);

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {

            return PrimaryFragment.newInstance(position, TAB_NAMES[position]);

        }

        @Override
        public int getCount() {

            return AppConstants.TAB_NAMES.length;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {


            return AppConstants.TAB_NAMES[position];


        }

    }
}
