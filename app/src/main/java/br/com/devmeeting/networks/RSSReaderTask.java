package br.com.devmeeting.networks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.devmeeting.R;
import br.com.devmeeting.exceptions.FetchRSSException;
import br.com.devmeeting.models.Event;
import br.com.devmeeting.networks.RSSParser;

public class RSSReaderTask extends AsyncTask<URL, Void, List<Event>> {

    private final Context context;

    private ProgressDialog progress;

    private Boolean hasFailed = false;

    public RSSReaderTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progress = new ProgressDialog(context);
        this.progress.setMessage(context.getString(R.string.progress_message));
        this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progress.setCancelable(false);
        this.progress.setIndeterminate(true);
        this.progress.show();
    }

    @Override
    protected List<Event> doInBackground(URL... urls) {
        List<Event> eventList = new ArrayList<>();

        int failures = 0;
        for (URL url : urls) {
            try {
                RSSParser parser = new RSSParser(url);
                parser.parse();
                eventList.addAll(parser.getEventList());
            } catch (FetchRSSException e) {
                failures++;
            }
        }

        if (failures == urls.length) {
            this.hasFailed = true;
        }

        return eventList;
    }

    @Override
    protected void onPostExecute(List<Event> eventList) {
        super.onPostExecute(eventList);
        if (this.hasFailed) {
            this.progress.dismiss();
            ((OnFetchEventsErrorCallback) this.context).onFetchEventsError();
        } else {
            ((OnFetchEventsCompleteCallback) this.context).onFetchEventsComplete(eventList);
            this.progress.dismiss();
        }

    }

    public interface OnFetchEventsCompleteCallback {
        void onFetchEventsComplete(List<Event> eventList);
    }

    public interface OnFetchEventsErrorCallback {
        void onFetchEventsError();
    }

    public Context getContext() {
        return this.context;
    }
}
