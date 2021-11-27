package dao;

import domain.Follow;
import domain.User;
import request.FollowersRequest;
import response.FollowersResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowerDAO {
	private static Map<User, List<User>> followersByUser;

	public FollowersResponse getFollowers(FollowersRequest request) {
		assert request.getLimit() >= 0;
		assert request.getFollowee() != null;

		if(followersByUser == null) {
			followersByUser = initializeFollowers();
		}

		List<User> allFollowers = followersByUser.get(request.getFollowee());
		List<User> responseFollowers = new ArrayList<>(request.getLimit());

		boolean hasMorePages = false;

		if(request.getLimit() > 0) {
			if (allFollowers != null) {
				int followersIndex = getFollowersStartingIndex(request.getLastFollower(), allFollowers);

				for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
					responseFollowers.add(allFollowers.get(followersIndex));
				}

				hasMorePages = followersIndex < allFollowers.size();
			}
		}

		return new FollowersResponse(responseFollowers, hasMorePages);
	}

	private Map<User, List<User>> initializeFollowers() {
		Map<User, List<User>> followersByUser = new HashMap<>();
		List<Follow> follows = FollowGenerator.getInstance().generateUsersAndFollows(50, 0, 30,
				FollowGenerator.Sort.FOLLOWER_FOLLOWEE);

		// Populate a map of followers, keyed by followee so we can easily handle follower requests
		for(Follow follow : follows) {
			List<User> followers = followersByUser.computeIfAbsent(follow.getFollower(), k -> new ArrayList<>());

			followers.add(follow.getFollowee());
		}

		return followersByUser;
	}

	private int getFollowersStartingIndex(User lastFollowee, List<User> allFollowers) {

		int followersIndex = 0;

		if(lastFollowee != null) {
			// This is a paged request for something after the first page. Find the first item
			// we should return
			for (int i = 0; i < allFollowers.size(); i++) {
				if(lastFollowee.equals(allFollowers.get(i))) {
					// We found the index of the last item returned last time. Increment to get
					// to the first one we should return
					followersIndex = i + 1;
				}
			}
		}

		return followersIndex;
	}
}
