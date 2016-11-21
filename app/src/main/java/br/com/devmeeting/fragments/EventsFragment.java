package br.com.devmeeting.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.devmeeting.App;
import br.com.devmeeting.adapters.EventAdapter;
import br.com.devmeeting.R;
import br.com.devmeeting.models.Event;
import br.com.devmeeting.networks.RSSReaderService;
import br.com.devmeeting.networks.RSSReaderTask;

public class EventsFragment extends Fragment {

    private List<Event> eventList = new ArrayList<>();
    private EventAdapter eventsAdapter = new EventAdapter(eventList);

    public static EventsFragment newInstance() {

        Bundle args = new Bundle();

        EventsFragment fragment = new EventsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);


        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventsAdapter);

        RSSReaderService rssReaderService = RSSReaderService.getInstance(this.getContext());
        this.eventList.addAll(rssReaderService.getAll());

        return view;
    }

    public void updateEvents(List<Event> events) {
        this.eventList.clear();
        this.eventList.addAll(events);
        this.eventsAdapter.notifyDataSetChanged();
    }
}
