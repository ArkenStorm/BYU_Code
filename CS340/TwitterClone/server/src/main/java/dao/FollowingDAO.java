package dao;

import domain.Follow;
import domain.User;
import request.FollowRequest;
import request.FollowingRequest;
import response.FollowResponse;
import response.FollowingResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowingDAO {

	private static Map<User, List<User>> followeesByUser;

	/**
	 * Gets the users from the database that the user specified in the request is following. Uses
	 * information in the request object to limit the number of followees returned and to return the
	 * next set of followees after any that were returned in a previous request. The current
	 * implementation returns generated data and doesn't actually access a database.
	 *
	 * @param request contains information about the user whose followees are to be returned and any
	 *                other information required to satisfy the request.
	 * @return the followees.
	 */
	public FollowingResponse getFollowees(FollowingRequest request) {

		assert request.getLimit() >= 0;
		assert request.getFollower() != null;

		if(followeesByUser == null) {
			followeesByUser = initializeFollowees();
		}

		List<User> allFollowees = followeesByUser.get(request.getFollower());
		List<User> responseFollowees = new ArrayList<>(request.getLimit());

		boolean hasMorePages = false;

		if(request.getLimit() > 0) {
			if (allFollowees != null) {
				int followeesIndex = getFolloweesStartingIndex(request.getLastFollowee(), allFollowees);

				for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
					responseFollowees.add(allFollowees.get(followeesIndex));
				}

				hasMorePages = followeesIndex < allFollowees.size();
			}
		}
		return new FollowingResponse(responseFollowees, hasMorePages);
	}

	/**
	 * Generates the followee data.
	 */
	private Map<User, List<User>> initializeFollowees() {
		Map<User, List<User>> followeesByUser = new HashMap<>();

		List<Follow> follows = getFollowGenerator().generateUsersAndFollows(50,
				0, 30, FollowGenerator.Sort.FOLLOWER_FOLLOWEE);

		// Populate a map of followees, keyed by follower so we can easily handle followee requests
		for(Follow follow : follows) {
			List<User> followees = followeesByUser.get(follow.getFollower());

			if(followees == null) {
				followees = new ArrayList<>();
				followeesByUser.put(follow.getFollower(), followees);
			}

			followees.add(follow.getFollowee());
		}

		return followeesByUser;
	}

	/**
	 * Determines the index for the first followee in the specified 'allFollowees' list that should
	 * be returned in the current request. This will be the index of the next followee after the
	 * specified 'lastFollowee'.
	 *
	 * @param lastFollowee the last followee that was returned in the previous request or null if
	 *                     there was no previous request.
	 * @param allFollowees the generated list of followees from which we are returning paged results.
	 * @return the index of the first followee to be returned.
	 */
	private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {

		int followeesIndex = 0;

		if(lastFollowee != null) {
			// This is a paged request for something after the first page. Find the first item
			// we should return
			for (int i = 0; i < allFollowees.size(); i++) {
				if(lastFollowee.equals(allFollowees.get(i))) {
					// We found the index of the last item returned last time. Increment to get
					// to the first one we should return
					followeesIndex = i + 1;
				}
			}
		}

		return followeesIndex;
	}

	/**
	 * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
	 * written as a separate method to allow mocking of the generator.
	 *
	 * @return the generator.
	 */
	FollowGenerator getFollowGenerator() {
		return FollowGenerator.getInstance();
	}

	public FollowResponse updateFollow(FollowRequest request) {
//		User currentUser = new User("Phrike", "Dragonsbane", "@Phrike",
//				"https://vignette.wikia.nocookie.net/leagueoflegends/images/3/3a/Demacia_profileicon.png/revision/latest/scale-to-width-down/340?cb=20170504232501");
//		if (request.isUpdate()) { // if they changed to be following
//			allFollows.add(new Follow(currentUser, request.getUser()));
//			followeesByUser.get(currentUser).add(request.getUser());
//		}
//		else {
//			Follow compare = new Follow(currentUser, request.getUser());
//			for (Follow follow : allFollows) {
//				if (follow.equals(compare)) {
//					allFollows.remove(follow);
//					break;
//				}
//			}
//			followeesByUser.get(currentUser).removeIf(user -> user.equals(request.getUser()));
//		}
		return new FollowResponse(request.isUpdate());
	}
}