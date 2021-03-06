package edu.byu.cs.client.view.main.following;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import domain.User;
import edu.byu.cs.client.view.asyncTasks.GetUserTask;
import edu.byu.cs.client.view.main.profile.ProfileActivity;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import edu.byu.cs.client.R;
import edu.byu.cs.client.presenter.FollowingPresenter;
import edu.byu.cs.client.view.asyncTasks.GetFollowingTask;
import edu.byu.cs.client.view.cache.ImageCache;
import request.FollowingRequest;
import request.GetUserRequest;
import response.FollowingResponse;
import response.GetUserResponse;

public class FollowingFragment extends Fragment implements FollowingPresenter.View, GetUserTask.GetUserTaskObserver {

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private FollowingPresenter presenter;

    private FollowingRecyclerViewAdapter followingRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, container, false);

        presenter = new FollowingPresenter(this);

        RecyclerView followingRecyclerView = view.findViewById(R.id.followingRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followingRecyclerView.setLayoutManager(layoutManager);

        followingRecyclerViewAdapter = new FollowingRecyclerViewAdapter();
        followingRecyclerView.setAdapter(followingRecyclerViewAdapter);

        followingRecyclerView.addOnScrollListener(new FollowRecyclerViewPaginationScrollListener(layoutManager));

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

    private class FollowingHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;

        FollowingHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);

            User user = presenter.getSelectedUser() != null ? presenter.getSelectedUser() : presenter.getCurrentUser();
            itemView.setOnClickListener(view -> new GetUserTask(presenter, FollowingFragment.this)
                    .execute(new GetUserRequest(userAlias.getText().toString(), user)));
        }

        void bindUser(User user) {
            userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(user));
            userAlias.setText(user.getAlias());
            userName.setText(user.getName());
        }
    }

    private class FollowingRecyclerViewAdapter extends RecyclerView.Adapter<FollowingHolder> implements GetFollowingTask.GetFolloweesObserver {

        private final List<User> users = new ArrayList<>();

        private User lastFollowee;

        private boolean hasMorePages;
        private boolean isLoading = false;

        FollowingRecyclerViewAdapter() {
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
        public FollowingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FollowingFragment.this.getContext());
            View view;

            if(isLoading) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.user_row, parent, false);
            }

            return new FollowingHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FollowingHolder followingHolder, int position) {
            if(!isLoading) {
                followingHolder.bindUser(users.get(position));
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

            GetFollowingTask getFollowingTask = new GetFollowingTask(presenter, this);
            User user = presenter.getSelectedUser() != null ? presenter.getSelectedUser() : presenter.getCurrentUser();
            FollowingRequest request = new FollowingRequest(user, PAGE_SIZE, lastFollowee);
            getFollowingTask.execute(request);
        }

        @Override
        public void followeesRetrieved(FollowingResponse followingResponse) {
            List<User> followees = new ArrayList<>();
            if (followingResponse.getFollowees() != null) {
                followees = followingResponse.getFollowees();
            }

            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() -1) : null;
            hasMorePages = followingResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            followingRecyclerViewAdapter.addItems(followees);
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

            if (!followingRecyclerViewAdapter.isLoading && followingRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    followingRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
