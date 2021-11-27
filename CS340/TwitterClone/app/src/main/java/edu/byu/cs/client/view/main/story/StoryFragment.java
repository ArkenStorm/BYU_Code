package edu.byu.cs.client.view.main.story;


import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import domain.Status;
import domain.User;
import edu.byu.cs.client.R;
import edu.byu.cs.client.presenter.StoryPresenter;
import edu.byu.cs.client.view.asyncTasks.GetStoryTask;
import edu.byu.cs.client.view.asyncTasks.GetUserTask;
import edu.byu.cs.client.view.cache.ImageCache;
import edu.byu.cs.client.view.main.profile.ProfileActivity;
import org.jetbrains.annotations.NotNull;
import request.GetUserRequest;
import request.StatusRequest;
import response.GetUserResponse;
import response.StatusResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoryFragment extends Fragment implements StoryPresenter.View, GetUserTask.GetUserTaskObserver {

	public StoryFragment() {
		// Required empty public constructor
	}

	private static final int LOADING_DATA_VIEW = 0;
	private static final int ITEM_VIEW = 1;

	private static final int PAGE_SIZE = 10;

	private StoryPresenter presenter;

	private StoryRecyclerViewAdapter storyRecyclerViewAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_story, container, false);

		presenter = new StoryPresenter(this);

		RecyclerView feedRecyclerView = view.findViewById(R.id.storyRecyclerView);

		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
		feedRecyclerView.setLayoutManager(layoutManager);

		storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
		feedRecyclerView.setAdapter(storyRecyclerViewAdapter);

		feedRecyclerView.addOnScrollListener(new StoryRecyclerViewPaginationScrollListener(layoutManager));

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

	private ClickableSpan createSpan(String alias) {
		return new ClickableSpan() {
			@Override
			public void onClick(@NonNull View widget) {
				User user = presenter.getSelectedUser() != null ? presenter.getSelectedUser() : presenter.getCurrentUser();
				new GetUserTask(presenter, StoryFragment.this)
						.execute(new GetUserRequest(alias, user));
			}
		};
	}

	private class StoryHolder extends RecyclerView.ViewHolder {
		private final ImageView userImage;
		private final TextView userAlias;
		private final TextView userName;
		private final TextView userStatus;
		private final TextView statusTimestamp;

		StoryHolder(@NonNull View itemView) {
			super(itemView);

			userImage = itemView.findViewById(R.id.userImage);
			userAlias = itemView.findViewById(R.id.userAlias);
			userName = itemView.findViewById(R.id.userName);
			userStatus = itemView.findViewById(R.id.userStatus);
			statusTimestamp = itemView.findViewById(R.id.timestamp);

			User user = presenter.getSelectedUser() != null ? presenter.getSelectedUser() : presenter.getCurrentUser();
			itemView.setOnClickListener(view -> new GetUserTask(presenter, StoryFragment.this)
					.execute(new GetUserRequest(userAlias.getText().toString(), user)));
		}

		void bindStatus(Status status) {
			userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(status.getOwner()));
			userAlias.setText(status.getOwner().getAlias());
			userName.setText(status.getOwner().getName());
			SpannableString mentionString = new SpannableString(status.getStatus());
			Pattern pattern = Pattern.compile("@\\w+");
			Matcher matcher = pattern.matcher(status.getStatus());
			while (matcher.find()) {
				String match = matcher.group();
				ClickableSpan mention = createSpan(match);
				int start = status.getStatus().indexOf(match);
				int end = start + match.length();
				mentionString.setSpan(mention, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			userStatus.setText(mentionString);
			userStatus.setMovementMethod(LinkMovementMethod.getInstance());
			statusTimestamp.setText(status.getTimestamp());
		}
	}

	private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryHolder> implements GetStoryTask.GetStoryObserver {
		private final List<Status> story = new ArrayList<>();

		private Status lastStatus;

		private boolean hasMorePages;
		private boolean isLoading = false;

		StoryRecyclerViewAdapter() {
			loadMoreItems();
		}

		void addItems(List<Status> newStatuses) {
			int startInsertPosition = story.size();
			story.addAll(newStatuses);
			this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
		}

		void addItem(Status status) {
			story.add(status);
			this.notifyItemInserted(story.size() - 1);
		}

		void removeItem(Status status) {
			int position = story.indexOf(status);
			story.remove(position);
			this.notifyItemRemoved(position);
		}

		@NonNull
		@Override
		public StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(StoryFragment.this.getContext());
			View view;

			if(isLoading) {
				view = layoutInflater.inflate(R.layout.loading_row, parent, false);

			} else {
				view = layoutInflater.inflate(R.layout.status_row, parent, false);
			}

			return new StoryHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull StoryHolder storyHolder, int position) {
			if(!isLoading) {
				storyHolder.bindStatus(story.get(position));
			}
		}

		@Override
		public int getItemCount() {
			return story.size();
		}

		@Override
		public int getItemViewType(int position) {
			return (position == story.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
		}


		void loadMoreItems() {
			isLoading = true;
			addLoadingFooter();

			GetStoryTask getStoryTask = new GetStoryTask(presenter, this);
			User user = presenter.getSelectedUser() != null ? presenter.getSelectedUser() : presenter.getCurrentUser();
			StatusRequest request = new StatusRequest(user, PAGE_SIZE, lastStatus);
			getStoryTask.execute(request);
		}

		@Override
		public void storyRetrieved(StatusResponse response) {
			List<Status> story = new ArrayList<>();
			if (response.getStatuses() != null) {
				story = response.getStatuses();
			}

			lastStatus = (story.size() > 0) ? story.get(story.size() -1) : null;
			hasMorePages = response.hasMorePages();

			isLoading = false;
			removeLoadingFooter();
			storyRecyclerViewAdapter.addItems(story);
		}

		private void addLoadingFooter() {
			addItem(new Status(new User("", "", ""), "Dummy Status"));
		}

		private void removeLoadingFooter() {
			removeItem(story.get(story.size() - 1));
		}
	}

	private class StoryRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

		private final LinearLayoutManager layoutManager;

		StoryRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
			this.layoutManager = layoutManager;
		}

		@Override
		public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);

			int visibleItemCount = layoutManager.getChildCount();
			int totalItemCount = layoutManager.getItemCount();
			int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

			if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages) {
				if ((visibleItemCount + firstVisibleItemPosition) >=
						totalItemCount && firstVisibleItemPosition >= 0) {
					storyRecyclerViewAdapter.loadMoreItems();
				}
			}
		}
	}
}
