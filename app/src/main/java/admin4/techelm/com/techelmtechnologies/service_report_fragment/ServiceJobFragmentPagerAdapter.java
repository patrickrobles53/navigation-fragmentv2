package admin4.techelm.com.techelmtechnologies.service_report_fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import admin4.techelm.com.techelmtechnologies.fragment_sample.SentFragment_OLD;
import admin4.techelm.com.techelmtechnologies.fragment_sample.UpdatesFragment;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

// BEING DONEEEEE
public class ServiceJobFragmentPagerAdapter extends FragmentPagerAdapter {

    private ServiceJobWrapper mServiceJobFromBundle;
    private FragmentManager mFragmentManager;

    private String[] titles = {
            "1. BEFORE",
            "2. AFTER",
            "3. PART REPLACEMENT",
            "4. SIGNING OFF"
    };

    public ServiceJobFragmentPagerAdapter(FragmentManager fm, ServiceJobWrapper serviceJob) {
        super(fm);
        this.mFragmentManager = fm;
        this.mServiceJobFromBundle = serviceJob;
    }

    @Override
    public Fragment getItem(int position) {
        System.out.print("ServiceJobFragmentPagerAdapter : This is from getITEM ");
        switch (position) {
            case 0 :
                return ServiceReport_FRGMT_1.newInstance(position, this.mServiceJobFromBundle);
            case 1 :
                return ServiceReport_FRGMT_1.newInstance(position, this.mServiceJobFromBundle);
            case 2 :
                return PartReplacement_FRGMT_2.newInstance(position, this.mServiceJobFromBundle);
            case 3 :
                return SigningOff_FRGMT_4.newInstance(position, this.mServiceJobFromBundle);

        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return mFragmentManager.findFragmentByTag(name);
    }

    /**
     * @param containerViewId the ViewPager this adapter is being supplied to
     * @param id              pass in getItemId(position) as this is whats used internally in this class
     * @return the tag used for this pages fragment
     */
    public static String makeFragmentName(int containerViewId, long id) {
        return "android:switcher:" + containerViewId + ":" + id;
    }
}
