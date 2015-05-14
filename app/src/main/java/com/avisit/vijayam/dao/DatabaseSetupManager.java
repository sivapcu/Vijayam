package com.avisit.vijayam.dao;

import android.content.Context;

import java.io.File;

/**
 * Created by User on 5/12/2015.
 */
public class DatabaseSetupManager {
    public static enum State {
        UNKNOWN, IN_PROGRESS, READY
    }

    private final Context context;
    private final String assetResourceName;
    private final int size;
    private final String databaseFileName;
    private State state;
    private DatabaseSetupTask setupTask;
    private Listener listener;

    public DatabaseSetupManager(Context context, String assetResourceName, int size, String databaseFileName) {
        this.context = context;
        this.assetResourceName = assetResourceName;
        this.size = size;
        this.databaseFileName = databaseFileName;
        this.state = State.UNKNOWN;
    }

    public State getState() {
        if (state == State.UNKNOWN) {
            File path = context.getDatabasePath(databaseFileName);
            if (path.exists()) {
                state = State.READY;
            } else {
                state = State.IN_PROGRESS;
                setupTask = new DatabaseSetupTask(context, context.getDatabasePath(databaseFileName), assetResourceName, size, this);
                setupTask.execute((Void[]) null);
            }
        }
        return state;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    void progress(Integer completed, Integer total) {
        if (listener != null) {
            listener.progress(completed.intValue(), total.intValue());
        }
    }

    void setupComplete(Exception result) {
        if (listener != null) {
            if (result != null) {
                File path = context.getDatabasePath(databaseFileName);
                path.delete();
                state = State.UNKNOWN;
                listener.complete(false, result);
            } else {
                state = State.READY;
                listener.complete(true, null);
            }
        }
    }

    public interface Listener {
        public void progress(int completed, int total);
        public void complete(boolean success, Exception result);
    }
}
