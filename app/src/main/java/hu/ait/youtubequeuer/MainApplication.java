package hu.ait.youtubequeuer;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {

    private Realm realmVideoQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        realmVideoQueue = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmVideoQueue.close();
    }

    public Realm getRealmCity() {
        return realmVideoQueue;
    }
}
