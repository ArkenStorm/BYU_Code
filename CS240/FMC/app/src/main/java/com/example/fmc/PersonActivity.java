package com.example.fmc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.fmc.extendedModels.FullEvent;
import com.example.fmc.extendedModels.FullPerson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import model.Person;

import java.lang.ref.WeakReference;
import java.util.List;

public class PersonActivity extends AppCompatActivity {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		ExpandableListView expandableListView = findViewById(R.id.expandableListView);
		Datacache cache = Datacache.getInstance();
		String personID = getIntent().getExtras().getString("personID");
		List<FullEvent> events =  cache.getFilteredPersonEvents(personID);
		Person selectedPerson = cache.getFullPersons().get(personID);
		List<Person> family = cache.getPersonFamily(selectedPerson);
		expandableListView.setAdapter(new ExpandableListAdapter(this,selectedPerson, events, family));

		TextView firstName = findViewById(R.id.firstName);
		firstName.setText(selectedPerson.getFirstName());
		TextView lastName = findViewById(R.id.lastName);
		lastName.setText(selectedPerson.getLastName());
		TextView gender = findViewById(R.id.gender);
		String genderString = selectedPerson.getGender().equals("m") ? "Male" : "Female";
		gender.setText(genderString);
	}

	private class ExpandableListAdapter extends BaseExpandableListAdapter {
		private static final int EVENT_POSITION = 0;
		private static final int FAMILY_POSITION = 1;

		private final Context context;
		private final Person selectedPerson;
		private final List<FullEvent> personEvents;
		private final List<Person> personFamily;
		private Datacache cache;

		ExpandableListAdapter(Context context, Person selectedPerson, List<FullEvent> personEvents, List<Person> personFamily) {
			this.context = context;
			this.selectedPerson = selectedPerson;
			this.personEvents = personEvents;
			this.personFamily = personFamily;
			cache = Datacache.getInstance();
		}

		@Override
		public int getGroupCount() {
			return 2;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			switch(groupPosition) {
				case EVENT_POSITION:
					return personEvents.size();
				case FAMILY_POSITION:
					return personFamily.size();
				default:
					throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
			}
		}

		@Override
		public Object getGroup(int groupPosition) {
			switch (groupPosition) {
				case EVENT_POSITION:
					return getString(R.string.lifeEvents);
				case FAMILY_POSITION:
					return getString(R.string.family);
				default:
					throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
			}
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			switch (groupPosition) {
				case EVENT_POSITION:
					return personEvents.get(childPosition);
				case FAMILY_POSITION:
					return personFamily.get(childPosition);
				default:
					throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
			}
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
			}

			TextView titleView = convertView.findViewById(R.id.listTitle);

			switch (groupPosition) {
				case EVENT_POSITION:
					titleView.setText(R.string.lifeEvents);
					break;
				case FAMILY_POSITION:
					titleView.setText(R.string.family);
					break;
				default:
					throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
			}

			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			View itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
			switch(groupPosition) {
				case EVENT_POSITION:
					itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
					initializeEventView(itemView, childPosition);
					break;
				case FAMILY_POSITION:
					itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
					initializeFamilyView(itemView, childPosition);
					break;
				default:
					throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
			}

			return itemView;
		}

		private void initializeEventView(View eventItemView, int childPosition) {
			TextView eventNameView = eventItemView.findViewById(R.id.list_item);
			FullEvent event = personEvents.get(childPosition);
			String text = event.getEventType() + ": " + event.getCity() + " "
					+ event.getCountry() + " (" + event.getYear() + ")" + "\n\n" +
					event.getPerson().getFirstName() + " " + event.getPerson().getLastName();
			eventNameView.setText(text);
//			float[] colorConverter = {cache.getEventColors().get(event.getEventType()), 1f, 1f};
//			int color = Color.HSVToColor(colorConverter);
			Drawable icon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker).colorRes(android.R.color.holo_blue_bright).sizeDp(40);
			eventNameView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
			eventNameView.setOnClickListener(v -> {
				Intent intent = new Intent(context, EventActivity.class);
				intent.putExtra("eventID", event.getEventID());
				startActivity(intent);
			});
		}

		private void initializeFamilyView(View familyItemView, int childPosition) {
			TextView familyNameView = familyItemView.findViewById(R.id.list_item);
			Person person = personFamily.get(childPosition);
			String relationship;
			if (selectedPerson.getFatherID().equals(person.getPersonID())) {
				relationship = "Father";
			}
			else if (selectedPerson.getMotherID().equals(person.getPersonID())) {
				relationship = "Mother";
			}
			else {
				relationship = "Child";
			}
			String text = person.getFirstName() + " " + person.getLastName() + "\n\n" + relationship;
			familyNameView.setText(text);
			familyNameView.setCompoundDrawablesWithIntrinsicBounds(Utility.determineGenderIcon(context, person), null, null, null);
			familyNameView.setOnClickListener(v -> {
				Intent intent = new Intent(context, PersonActivity.class);
				intent.putExtra("personID", person.getPersonID());
				startActivity(intent);
			});
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}
