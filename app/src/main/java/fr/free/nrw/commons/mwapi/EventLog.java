package fr.free.nrw.commons.mwapi;

import android.os.Build;

import fr.free.nrw.commons.Utils;
import fr.free.nrw.commons.kvstore.BasicKvStore;

public class EventLog {
    static final String DEVICE;

    static {
        if (Build.MODEL.startsWith(Build.MANUFACTURER)) {
            DEVICE = Utils.capitalize(Build.MODEL);
        } else {
            DEVICE = Utils.capitalize(Build.MANUFACTURER) + " " + Build.MODEL;
        }
    }

    private static LogBuilder schema(String schema, long revision, MediaWikiApi mwApi, BasicKvStore prefs) {
        return new LogBuilder(schema, revision, mwApi, prefs);
    }

    public static LogBuilder schema(Object[] scid, MediaWikiApi mwApi, BasicKvStore prefs) {
        if (scid.length != 2) {
            throw new IllegalArgumentException("Needs an object array with schema as first param and revision as second");
        }
        return schema((String) scid[0], (Long) scid[1], mwApi, prefs);
    }
}
