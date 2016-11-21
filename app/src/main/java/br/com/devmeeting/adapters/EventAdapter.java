package br.com.devmeeting.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.devmeeting.R;
import br.com.devmeeting.models.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<Event> listEvent;

    public EventAdapter(List<Event> listEvent) {
        this.listEvent = listEvent;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Event event = this.listEvent.get(position);
        holder.title.setText(event.getTitle());
        holder.address.setText(event.getAddress());
        holder.date.setText(new SimpleDateFormat("EEE, dd MMM yyyy").format(event.getDate()).toUpperCase());

        holder.read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getWebsite()));
                view.getContext().startActivity(browserIntent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.events_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, title, address;
        Button read_more;

        public ViewHolder(View view) {
            super(view);
            this.date = (TextView) view.findViewById(R.id.event_date);
            this.title = (TextView) view.findViewById(R.id.event_title);
            this.address = (TextView) view.findViewById(R.id.event_local);
            this.read_more = (Button) view.findViewById(R.id.read_more);
        }
    }

}