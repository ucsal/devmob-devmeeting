package br.com.devmeeting;

import android.support.multidex.MultiDexApplication;

import org.greenrobot.greendao.database.Database;

import br.com.devmeeting.models.DaoMaster;
import br.com.devmeeting.models.DaoSession;

public class App extends MultiDexApplication {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "events-db");
        Database db = helper.getWritableDb();
        this.daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return this.daoSession;
    }
}
