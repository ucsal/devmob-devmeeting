package br.com.devmeeting.networks;

import android.content.Context;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import br.com.devmeeting.App;
import br.com.devmeeting.R;
import br.com.devmeeting.models.DaoSession;
import br.com.devmeeting.models.Event;
import br.com.devmeeting.models.EventDao;

public class RSSReaderService {

    private static final String RSS_URL = "http://www.kodlr.com/rss/rss.xml";

    private Context context;
    private EventDao eventDao;

    private static RSSReaderService instance = new RSSReaderService();

    private RSSReaderService() {

    }

    public static RSSReaderService getInstance(Context context) {
        instance.context = context;
        DaoSession daoSession = ((App)instance.context.getApplicationContext()).getDaoSession();
        instance.eventDao = daoSession.getEventDao();

        return instance;
    }

    public void fetchAsync() {
        try {
            new RSSReaderTask(this.context).execute(new URL(RSS_URL));
        } catch (MalformedURLException e) {
            Toast.makeText(this.context, this.context.getString(R.string.update_error),
                    Toast.LENGTH_LONG).show();
        }
    }

    public List<Event> getAll() {
        return this.eventDao.loadAll();
    }

    public void saveOrUpdate(Event event) {
        if (event.getId() != null) {
            eventDao.update(event);
        } else {
            eventDao.save(event);
        }
    }

    public void delete(Event event) {
        eventDao.delete(event);
    }

    public void saveOrUpdateAll(List<Event> eventList) {
        eventDao.deleteAll();
        eventDao.saveInTx(eventList);
    }
}
