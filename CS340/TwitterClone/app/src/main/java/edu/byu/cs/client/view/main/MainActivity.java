package edu.byu.cs.client.view.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.widget.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import domain.User;
import edu.byu.cs.client.R;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.presenter.MainPresenter;
import edu.byu.cs.client.view.asyncTasks.GetUserTask;
import edu.byu.cs.client.view.asyncTasks.LoadImageTask;
import edu.byu.cs.client.view.asyncTasks.LogoutTask;
import edu.byu.cs.client.view.cache.ImageCache;
import edu.byu.cs.client.view.main.post.PostActivity;
import edu.byu.cs.client.view.main.profile.ProfileActivity;
import request.GetUserRequest;
import request.LogoutRequest;
import response.GetUserResponse;
import response.LogoutResponse;

public class MainActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, MainPresenter.View,
        LogoutTask.LogoutTaskObserver, GetUserTask.GetUserTaskObserver {

    private MainPresenter presenter;
    private User user;
    private ImageView userImageView;
    private Button logoutButton;
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);

        MainSectionsPagerAdapter sectionsPagerAdapter = new MainSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(this, PostActivity.class));
        });

        searchView = findViewById(R.id.searchUsers);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new GetUserTask(presenter, MainActivity.this)
                        .execute(new GetUserRequest(searchView.getQuery().toString(), LoginServiceProxy.getInstance().getCurrentUser()));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        userImageView = findViewById(R.id.userImage);
        user = presenter.getCurrentUser();
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            new LogoutTask(presenter, this)
                    .execute(new LogoutRequest(user));
        });

        // Asynchronously load the user's image
        LoadImageTask loadImageTask = new LoadImageTask(this);
        loadImageTask.execute(presenter.getCurrentUser().getImageUrl());

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());
    }

    @Override
    public void imageLoadProgressUpdated(Integer progress) {
        // We're just loading one image. No need to indicate progress.
    }

    @Override
    public void imagesLoaded(Drawable[] drawables) {
        ImageCache.getInstance().cacheImage(user, drawables[0]);

        if(drawables[0] != null) {
            userImageView.setImageDrawable(drawables[0]);
        }
    }

    @Override
    public void logoutFinish(LogoutResponse response) {
        Intent intent = new Intent(this, StartupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void userRetrieved(GetUserResponse response) {
        if (response.isSuccess()) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("isFollowedByCurrentUser", response.isFollowedByCurrentUser());
            startActivity(intent);
        }
        else {
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}