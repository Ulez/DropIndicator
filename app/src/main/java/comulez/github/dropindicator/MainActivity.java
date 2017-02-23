package comulez.github.dropindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import comulez.github.droplibrary.DropIndicator;
import comulez.github.droplibrary.DropViewPager;

public class MainActivity extends AppCompatActivity {
    private DropViewPager mViewPager;
    private DropIndicator circleIndicator;
    private FragmentPagerAdapter mAdapter;
    private ArrayList<Fragment> mTabContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (DropViewPager) findViewById(R.id.mViewPager);
        circleIndicator = (DropIndicator) findViewById(R.id.circleIndicator);
        mTabContents.add(BlankFragment.newInstance("0", 0));
        mTabContents.add(BlankFragment.newInstance("1", 1));
        mTabContents.add(BlankFragment.newInstance("2", 2));
        mTabContents.add(BlankFragment.newInstance("3", 3));
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }
        };
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mAdapter);
        circleIndicator.setViewPager(mViewPager);
    }
}
