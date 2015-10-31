package com.nhpatt.kpi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.evernote.android.job.JobRequest;
import com.nhpatt.kpi.adapters.TitleAndDateAdapter;
import com.nhpatt.kpi.async.ShowsAsyncTask;
import com.nhpatt.kpi.fragments.FilmsFragment;
import com.nhpatt.kpi.fragments.GithubFragment;
import com.nhpatt.kpi.fragments.ShowsFragment;
import com.nhpatt.kpi.models.Channel;
import com.nhpatt.kpi.models.Film;
import com.nhpatt.kpi.models.Show;
import com.nhpatt.kpi.models.XML;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class DashboardActivity extends AppCompatActivity {

    @Bind(R.id.view_pager)
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);

        super.onPause();
    }

    public void onEventMainThread(List<Film> films) {
        TitleAndDateAdapter filmsAdapter = new TitleAndDateAdapter(films);

        RecyclerView filmsRecyclerView = (RecyclerView) findViewById(R.id.films);
        filmsRecyclerView.setAdapter(filmsAdapter);
        filmsRecyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return GithubFragment.newInstance();
                case 1:
                    return ShowsFragment.newInstance();
                case 2:
                    return FilmsFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}