package edu.byu.cs.client.view.main.followers;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import domain.User;
import edu.byu.cs.client.R;
import edu.byu.cs.client.presenter.FollowersPresenter;
import edu.byu.cs.client.view.asyncTasks.GetFollowersTask;
import edu.byu.cs.client.view.asyncTasks.GetUserTask;
import edu.byu.cs.client.view.cache.ImageCache;
import edu.byu.cs.client.view.main.profile.ProfileActivity;
import org.jetbrains.annotations.NotNull;
import request.FollowersRequest;
import request.GetUserRequest;
import response.FollowersResponse;
import response.GetUserResponse;

import java.util.ArrayList;
import java.util.List;

public class FollowersFragment extends Fragment implements FollowersPresenter.View, GetUserTask.GetUserTaskObserver {
	private static final int LOADING_DATA_VIEW = 0;
	private static final int ITEM_VIEW = 1;

	private static final int PAGE_SIZE = 10;

	private FollowersPresenter presenter;

	private FollowersRecyclerViewAdapter followersRecyclerViewAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_followers, container, false);

		presenter = new FollowersPresenter(this);

		RecyclerView followersRecyclerView = view.findViewById(R.id.followersRecyclerView);

		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
		followersRecyclerView.setLayoutManager(layoutManager);

		followersRecyclerViewAdapter = new FollowersRecyclerViewAdapter();
		followersRecyclerView.setAdapter(followersRecyclerViewAdapter);

		followersRecyclerView.addOnScrollListener(new FollowRecyclerViewPaginationScrollListener(layoutManager));

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		presenter.setSelectedUser(null);
	}

	@Override
	public void userRetrieved(GetUserResponse response) {
		if (response.isSuccess()) {
			presenter.setSelectedUser(response.getUser());
			Intent intent = new Intent(getContext(), ProfileActivity.class);
			intent.putExtra("isFollowedByCurrentUser", response.isFollowedByCurrentUser());
			startActivity(intent);
		}
		else {
			Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_LONG).show();
		}
	}


	private class FollowersHolder extends RecyclerView.ViewHolder {

		private final ImageView userImage;
		private final TextView userAlias;
		private final TextView userName;

		FollowersHolder(@NonNull View itemView) {
			super(itemView);

			userImage = itemView.findViewById(R.id.userImage);
			userAlias = itemView.findViewById(R.id.userAlias);
			userName = itemView.findViewById(R.id.userName);

			User user = presenter.getSelectedUser() != null ? presenter.getSelectedUser() : presenter.getCurrentUser();
			itemView.setOnClickListener(view -> new GetUserTask(presenter, FollowersFragment.this)
					.execute(new GetUserRequest(userAlias.getText().toString(), user)));
		}

		void bindUser(User user) {
			userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(user));
			userAlias.setText(user.getAlias());
			userName.setText(user.getName());
		}
	}

	private class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersHolder> implements GetFollowersTask.GetFollowersObserver {

		private final List<User> users = new ArrayList<>();

		private User lastFollowee;

		private boolean hasMorePages;
		private boolean isLoading = false;

		FollowersRecyclerViewAdapter() {
			loadMoreItems();
		}

		void addItems(List<User> newUsers) {
			int startInsertPosition = users.size();
			users.addAll(newUsers);
			this.notifyItemRangeInserted(startInsertPosition, newUsers.size());
		}

		void addItem(User user) {
			users.add(user);
			this.notifyItemInserted(users.size() - 1);
		}

		void removeItem(User user) {
			int position = users.indexOf(user);
			users.remove(position);
			this.notifyItemRemoved(position);
		}

		@NonNull
		@Override
		public FollowersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(FollowersFragment.this.getContext());
			View view;

			if(isLoading) {
				view = layoutInflater.inflate(R.layout.loading_row, parent, false);
			} else {
				view = layoutInflater.inflate(R.layout.user_row, parent, false);
			}

			return new FollowersHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull FollowersHolder followersHolder, int position) {
			if(!isLoading) {
				followersHolder.bindUser(users.get(position));
			}
		}

		@Override
		public int getItemCount() {
			return users.size();
		}

		@Override
		public int getItemViewType(int position) {
			return (position == users.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
		}


		void loadMoreItems() {
			isLoading = true;
			addLoadingFooter();

			GetFollowersTask getFollowersTask = new GetFollowersTask(presenter, this);
			User user = presenter.getSelectedUser() != null ? presenter.getSelectedUser() : presenter.getCurrentUser();
			FollowersRequest request = new FollowersRequest(user, PAGE_SIZE, lastFollowee);
			getFollowersTask.execute(request);
		}

		@Override
		public void followersRetrieved(FollowersResponse followersResponse) {
			List<User> followers = new ArrayList<>();
			if (followersResponse.getFollowers() != null) {
				followers = followersResponse.getFollowers();
			}

			lastFollowee = (followers.size() > 0) ? followers.get(followers.size() -1) : null;
			hasMorePages = followersResponse.hasMorePages();

			isLoading = false;
			removeLoadingFooter();
			followersRecyclerViewAdapter.addItems(followers);
		}

		private void addLoadingFooter() {
			addItem(new User("Dummy", "User", ""));
		}

		private void removeLoadingFooter() {
			removeItem(users.get(users.size() - 1));
		}
	}

	private class FollowRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

		private final LinearLayoutManager layoutManager;

		FollowRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
			this.layoutManager = layoutManager;
		}

		@Override
		public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);

			int visibleItemCount = layoutManager.getChildCount();
			int totalItemCount = layoutManager.getItemCount();
			int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

			if (!followersRecyclerViewAdapter.isLoading && followersRecyclerViewAdapter.hasMorePages) {
				if ((visibleItemCount + firstVisibleItemPosition) >=
						totalItemCount && firstVisibleItemPosition >= 0) {
					followersRecyclerViewAdapter.loadMoreItems();
				}
			}
		}
	}
}
