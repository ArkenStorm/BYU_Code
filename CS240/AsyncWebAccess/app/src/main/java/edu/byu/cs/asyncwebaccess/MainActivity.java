package edu.byu.cs.asyncwebaccess;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements DownloadTask.DownloadTaskListener {

    private static final String LOG_TAG = "MainActivity";

    private ProgressBar progressBar;
    private TextView totalSizeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        totalSizeTextView = findViewById(R.id.totalSizeTextView);

        Button downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    resetViews();

                    DownloadTask task = new DownloadTask();
                    task.registerListener(MainActivity.this);
                    task.execute(new URL("https://www.byu.edu"),
                            new URL("https://www.whitehouse.gov/"),
                            new URL("https://www.oracle.com/index.html"));
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            }
        });

        resetViews();
    }

    private void resetViews() {
        progressBar.setProgress(0);
        totalSizeTextView.setText(getString(R.string.downloadSizeEmptyLabel));
    }

    @Override
    public void progressUpdated(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void taskCompleted(long result) {
        totalSizeTextView.setText(getString(R.string.downloadSizeLabel, result));
    }
}
