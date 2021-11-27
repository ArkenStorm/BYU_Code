package edu.byu.cs.client.view.main.profile;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import edu.byu.cs.client.R;
import edu.byu.cs.client.view.main.followers.FollowersFragment;
import edu.byu.cs.client.view.main.following.FollowingFragment;
import edu.byu.cs.client.view.main.story.StoryFragment;

public class ProfileSectionsPagerAdapter extends FragmentPagerAdapter {
	private static final int STORY_FRAGMENT_POSITION = 0;
	private static final int FOLLOWING_FRAGMENT_POSITION = 1;

	@StringRes
	private static final int[] TAB_TITLES = new int[]{R.string.feedTabTitle, R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
	private final Context mContext;

	public ProfileSectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		if (position == STORY_FRAGMENT_POSITION) {
			return new StoryFragment();
		}
		else if (position == FOLLOWING_FRAGMENT_POSITION) {
			return new FollowingFragment();
		}
		else {
			return new FollowersFragment();
		}
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return mContext.getResources().getString(TAB_TITLES[position]);
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 3;
	}
}
