package com.example.fmc;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.example.fmc.extendedModels.FullEvent;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import model.Event;
import model.Person;
import result.EventArrayResult;
import result.MessageResult;
import result.PersonArrayResult;
import result.Result;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class Utility {
	public static class PersonTask extends AsyncTask<Void, Void, Result> {
		private URL serverURL;
		private WeakReference<Context> activity;

		public PersonTask(URL url, Context activity) {
			serverURL = url;
			this.activity = new WeakReference<>(activity);
		}

		@Override
		protected Result doInBackground(Void... voids) { // PersonArray result or just a person result amd store all the persons in the datacache?
			return ServerProxy.getPersons(serverURL);
		}

		@Override
		protected void onPostExecute(Result result) {
			if (result instanceof PersonArrayResult) {
				Datacache cache = Datacache.getInstance();
				List<Person> data = ((PersonArrayResult) result).getData();
				cache.setFamilyData(data);
				for (Person person : data) {
					if (person.getPersonID().equals(cache.getUserPersonID())) {
						cache.setUser(person);
						break;
					}
				}
				String toastText = "Welcome, " + cache.getUser().getFirstName() + " " + cache.getUser().getLastName();
				Toast.makeText(activity.get(), toastText, Toast.LENGTH_LONG).show();
			}
			else {
				if (activity != null) {
					Toast.makeText(activity.get(), ((MessageResult) result).getMessage(), Toast.LENGTH_LONG).show();
				}
				else {
					// should never get here
					Log.e("PersonAsync", "Error: The Activity is undefined.");
				}
			}
		}
	}

	public static class EventTask extends AsyncTask<Void, Void, Result> {
		private URL serverURL;
		private WeakReference<Context> activity;

		public EventTask(URL url, Context activity) {
			serverURL = url;
			this.activity = new WeakReference<>(activity);
		}

		@Override
		protected Result doInBackground(Void... voids) {
			return ServerProxy.getEvents(serverURL);
		}

		@Override
		protected void onPostExecute(Result result) {
			if (result instanceof EventArrayResult) {
				Datacache cache = Datacache.getInstance();
				List<Event> data = ((EventArrayResult) result).getData();
				cache.setFullEvents(data);
			}
			else {
				if (activity != null) {
					Toast.makeText(activity.get(), ((MessageResult) result).getMessage(), Toast.LENGTH_LONG).show();
				}
				else {
					// should never get here
					Log.e("EventAsync", "Error: The Activity is undefined.");
				}
			}
		}
	}

	public static Drawable determineGenderIcon(Context context, Person person) {
		if (person.getGender().equals("m")) {
			return new IconDrawable(context, FontAwesomeIcons.fa_male).colorRes(android.R.color.holo_green_light).sizeDp(40);
		} else {
			return new IconDrawable(context, FontAwesomeIcons.fa_female).colorRes(android.R.color.holo_purple).sizeDp(40);
		}
	}
}
