package br.com.devmeeting.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import br.com.devmeeting.R;
import br.com.devmeeting.adapters.PagerAdapter;
import br.com.devmeeting.fragments.EventsFragment;
import br.com.devmeeting.fragments.MapFragment;
import br.com.devmeeting.models.Event;
import br.com.devmeeting.networks.RSSReaderService;
import br.com.devmeeting.networks.RSSReaderTask;

public class MainActivity extends AppCompatActivity implements RSSReaderTask.OnFetchEventsCompleteCallback,
        RSSReaderTask.OnFetchEventsErrorCallback {

    private final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
    private RSSReaderService rssReaderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        TabLayout tabs = (TabLayout) findViewById(R.id.tab_layout);
        tabs.addTab(tabs.newTab().setText(R.string.tab_events));
        tabs.addTab(tabs.newTab().setText(R.string.tab_map));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        this.rssReaderService = RSSReaderService.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_update) {
            this.rssReaderService.fetchAsync();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFetchEventsComplete(List<Event> eventList) {
        ((EventsFragment) this.adapter.getItem(PagerAdapter.TAB_POSITION_EVENTS))
                .updateEvents(eventList);
        ((MapFragment) this.adapter.getItem(PagerAdapter.TAB_POSITION_MAPS))
                .updateMap(eventList);
        Toast.makeText(this, this.getString(R.string.update_success), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFetchEventsError() {
        Toast.makeText(this, this.getString(R.string.update_error), Toast.LENGTH_LONG).show();
    }
}
