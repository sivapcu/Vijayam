package com.avisit.vijayam.dao;

import android.content.Context;
import android.os.AsyncTask;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by User on 5/12/2015.
 */
public class DatabaseSetupTask extends AsyncTask<Void, Integer, Exception> {
    public static final int DATABASE_COPY_BUFFER = 4096;

    private final Context context;
    private final File destination;
    private final String assetResourceName;
    private final Integer iterations;
    private final DatabaseSetupManager manager;

    public DatabaseSetupTask(Context context, File destination, String assetResourceName, int expectedSize, DatabaseSetupManager manager) {
        this.context = context;
        this.destination = destination;
        this.assetResourceName = assetResourceName;
        this.iterations = Integer.valueOf((expectedSize + DATABASE_COPY_BUFFER - 1) / DATABASE_COPY_BUFFER);
        this.manager = manager;
    }

    @Override
    protected Exception doInBackground(Void... params) {
        try {
            deleteFilesInDestinationDirectory();
            File tempFile = new File(destination.getAbsolutePath() + ".tmp");
            copyDatabaseToTempFile(tempFile);
            tempFile.renameTo(destination);
        } catch (Exception e) {
            deleteFilesInDestinationDirectory();
            return e;
        }
        return null;
    }

    private void deleteFilesInDestinationDirectory() {
        File directory = destination.getParentFile();
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    private void copyDatabaseToTempFile(File tempFile) throws IOException {
        File dir = destination.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        InputStream input = null;
        FileOutputStream output = null;

        try {

            input = context.getAssets().open(assetResourceName);
            output = new FileOutputStream(tempFile);
            byte[] buffer = new byte[DATABASE_COPY_BUFFER];

            int progress = 0;
            for (; ; ) {
                int nRead = input.read(buffer);
                if (nRead <= 0) {
                    break;
                }

                output.write(buffer, 0, nRead);
                progress++;

                publishProgress(Integer.valueOf(progress));
            }
        } finally {
            safeClose(input);
            safeClose(output);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        manager.progress(values[0], iterations);
    }

    @Override
    protected void onPostExecute(Exception result) {
        manager.setupComplete(result);
    }

    private void safeClose(Closeable item) {
        try {
            if (item != null) {
                item.close();
            }
        } catch (Exception ignored) {
        }
    }
}
