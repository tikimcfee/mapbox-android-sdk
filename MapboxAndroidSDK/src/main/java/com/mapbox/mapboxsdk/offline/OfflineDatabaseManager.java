package com.mapbox.mapboxsdk.offline;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.Enumeration;
import java.util.Hashtable;

public class OfflineDatabaseManager {

    private static OfflineDatabaseManager offlineDatabaseManager = null;

    public Hashtable<String, OfflineDatabaseHandler> databaseHandlers = null;

    private static Context context = null;

    private OfflineDatabaseManager() {
        super();
        databaseHandlers = new Hashtable<String, OfflineDatabaseHandler>();
    }

    public static OfflineDatabaseManager getOfflineDatabaseManager(Context ctx) {
        if (offlineDatabaseManager == null) {
            offlineDatabaseManager = new OfflineDatabaseManager();
        }
        context = ctx;
        return offlineDatabaseManager;
    }

    public OfflineDatabaseHandler getOfflineDatabaseHandlerForUniqueID(String UID) {
        Enumeration<String> keys = databaseHandlers.keys();
        if (databaseHandlers.containsKey(UID)) {
            return databaseHandlers.get(UID);
        }

        return null;
    }

    public OfflineDatabaseHandler getOfflineDatabaseHandlerForMapId(String mapId) {
        if (databaseHandlers.containsKey(mapId.toLowerCase())) {
            return databaseHandlers.get(mapId);
        }

        OfflineDatabaseHandler dbh = new OfflineDatabaseHandler(context, mapId.toLowerCase() + "-PARTIAL");
        databaseHandlers.put(mapId.toLowerCase(), dbh);
        return dbh;
    }

    public OfflineDatabaseHandler getOfflineDatabaseHandlerForMapId(String mapId, boolean fromFileSystem) {
        if (!fromFileSystem) {
            return getOfflineDatabaseHandlerForMapId(mapId);
        }

        String key = mapId.toLowerCase();
        if (databaseHandlers.containsKey(key)) {
            return databaseHandlers.get(key);
        }

        OfflineDatabaseHandler dbh = new OfflineDatabaseHandler(context, key);
        databaseHandlers.put(key, dbh);
        return dbh;
    }

    public boolean switchHandlerFromPartialToRegular(String mapId) {
        if (TextUtils.isEmpty(mapId)) {
            return false;
        }
        String key = mapId.toLowerCase();
        if (!databaseHandlers.containsKey(key)) {
            return false;
        }

        OfflineDatabaseHandler dbh = new OfflineDatabaseHandler(context, key);
        databaseHandlers.remove(key);
        databaseHandlers.put(key, dbh);
        return true;
    }
}
