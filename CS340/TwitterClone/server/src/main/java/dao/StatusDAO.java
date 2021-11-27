package dao;

import domain.Status;
import domain.User;
import request.PostRequest;
import request.StatusRequest;
import response.PostResponse;
import response.StatusResponse;

import java.util.*;

public class StatusDAO {
	public StatusResponse getFeed(StatusRequest request) {
		List<Status> userFeed = new ArrayList<>();
		User newUser = new User("Phrike", "Dragonsbane", "@Phrike",
				"https://vignette.wikia.nocookie.net/leagueoflegends/images/3/3a/Demacia_profileicon.png/revision/latest/scale-to-width-down/340?cb=20170504232501");
		for (int i = 0; i < 20; i++) {
			userFeed.add(new Status(newUser, "Hardcoded statuses, huzzah!"));
		}
		return getResponseStatuses(request, userFeed);
	}

	private int getStatusesStartingIndex(Status lastStatus, List<Status> allStatuses) {
		int statusIndex = 0;
		if(lastStatus != null) {
			for (int i = 0; i < allStatuses.size(); i++) {
				if(lastStatus.equals(allStatuses.get(i))) {
					statusIndex = i + 1;
				}
			}
		}
		return statusIndex;
	}

	public StatusResponse getStory(StatusRequest request) {
		List<Status> userStory = new ArrayList<>();
		User newUser = new User("Phrike", "Dragonsbane", "@Phrike",
				"https://vignette.wikia.nocookie.net/leagueoflegends/images/3/3a/Demacia_profileicon.png/revision/latest/scale-to-width-down/340?cb=20170504232501");
		for (int i = 0; i < 20; i++) {
			userStory.add(new Status(newUser, "Hardcoded statuses, huzzah!"));
		}
		return getResponseStatuses(request, userStory);
	}

	private StatusResponse getResponseStatuses(StatusRequest request, List<Status> userStatuses) {
		List<Status> responseStatuses = new ArrayList<>(request.getLimit());

		boolean hasMorePages = false;

		if (request.getLimit() > 0) {
			if (userStatuses != null) {
				int statusIndex = getStatusesStartingIndex(request.getLastStatus(), userStatuses);
				for (int limitCounter = 0; statusIndex < userStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
					responseStatuses.add(userStatuses.get(statusIndex));
				}
				hasMorePages = statusIndex < userStatuses.size();
			}
		}
		Collections.reverse(responseStatuses);
		return new StatusResponse(responseStatuses, true);
	}

	public PostResponse postStatus(PostRequest request) {
		Status newStatus = new Status(request.getOwner(), request.getStatusText());
		return new PostResponse(newStatus);
	}
}
