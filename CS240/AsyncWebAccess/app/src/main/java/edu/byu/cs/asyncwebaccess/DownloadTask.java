package edu.byu.cs.asyncwebaccess;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class DownloadTask extends AsyncTask<URL, Integer, Long> {

    interface DownloadTaskListener {
        void progressUpdated(int progress);
        void taskCompleted(long result);
    }

    private final List<DownloadTaskListener> listeners = new ArrayList<>();


    void registerListener(DownloadTaskListener listener) {
        listeners.add(listener);
    }

    private void fireProgressUpdate(int progress) {
        for(DownloadTaskListener listener : listeners) {
            listener.progressUpdated(progress);
        }
    }

    private void fireTaskCompleted(long result) {
        for(DownloadTaskListener listener : listeners) {
            listener.taskCompleted(result);
        }
    }

    @Override
    protected Long doInBackground(URL... urls) {

        HttpClient httpClient = new HttpClient();

        long totalSize = 0;

        for (int i = 0; i < urls.length; i++) {

            String urlContent = httpClient.getUrl(urls[i]);
            if (urlContent != null) {
                totalSize += urlContent.length();
            }

            int progress;
            if (i == urls.length - 1) {
                progress = 100;
            } else {
                float cur = i + 1;
                float total = urls.length;
                progress = (int) ((cur / total) * 100);
            }

            publishProgress(progress);
        }

        return totalSize;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        fireProgressUpdate(progress[0]);
    }

    @Override
    protected void onPostExecute(Long result) {
        fireTaskCompleted(result);
    }
}
