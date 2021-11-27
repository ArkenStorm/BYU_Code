package edu.byu.cs.client.view.main.profile;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import domain.User;
import edu.byu.cs.client.R;
import edu.byu.cs.client.presenter.ProfilePresenter;
import edu.byu.cs.client.view.asyncTasks.FollowTask;
import edu.byu.cs.client.view.asyncTasks.LoadImageTask;
import edu.byu.cs.client.view.cache.ImageCache;
import request.FollowRequest;
import response.FollowResponse;

public class ProfileActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, ProfilePresenter.View, FollowTask.FollowTaskObserver {
	private ProfilePresenter presenter;
	private User user;
	private ImageView userImageView;
	private boolean isFollowedByCurrentUser;
	private Button followButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		presenter = new ProfilePresenter(this);

		ProfileSectionsPagerAdapter sectionsPagerAdapter = new ProfileSectionsPagerAdapter(this, getSupportFragmentManager());
		ViewPager viewPager = findViewById(R.id.view_pager);
		viewPager.setAdapter(sectionsPagerAdapter);
		TabLayout tabs = findViewById(R.id.tabs);
		tabs.setupWithViewPager(viewPager);

		followButton = findViewById(R.id.followButton);
		userImageView = findViewById(R.id.userImage);
		user = presenter.getSelectedUser();
		if (user.equals(presenter.getCurrentUser())) {
			followButton.setVisibility(View.GONE);
		}
		isFollowedByCurrentUser = getIntent().getBooleanExtra("isFollowedByCurrentUser", false);
		updateButtonText(isFollowedByCurrentUser);
		followButton.setOnClickListener(v -> {
			FollowRequest request = new FollowRequest(isFollowedByCurrentUser, presenter.getSelectedUser());
			request.setCurrentUser(presenter.getCurrentUser());
			new FollowTask(presenter, this).execute(request);
		});

		// Asynchronously load the user's image
		LoadImageTask loadImageTask = new LoadImageTask(this);
		loadImageTask.execute(presenter.getSelectedUser().getImageUrl());

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

	private void updateButtonText(boolean isFollowedByCurrentUser) {
		if (isFollowedByCurrentUser) {
			followButton.setText(R.string.unfollow);
		}
		else {
			followButton.setText(R.string.follow);
		}
	}

	@Override
	public void followUpdated(FollowResponse response) {
		isFollowedByCurrentUser = response.isIsFollowed();
		updateButtonText(isFollowedByCurrentUser);
	}
}
