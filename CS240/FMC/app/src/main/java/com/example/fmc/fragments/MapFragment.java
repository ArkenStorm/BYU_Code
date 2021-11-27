package com.example.fmc.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.example.fmc.*;
import com.example.fmc.R;
import com.example.fmc.extendedModels.FullEvent;
import com.example.fmc.extendedModels.FullPerson;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.*;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;


public class MapFragment extends Fragment implements OnMapReadyCallback {
	private GoogleMap map;
	private ArrayList<Float> markerColors = new ArrayList<>(Arrays.asList(BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_BLUE,
			BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_MAGENTA, BitmapDescriptorFactory.HUE_ORANGE,
			BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_ROSE, BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_YELLOW));
	private Map<String, Float> eventColors = new TreeMap<>();
	private int colorIndex = -1;
	private TextView selectText;
	private int lifeStoryColor = Color.BLUE;
	private int familyTreeColor = Color.GREEN;
	private int spouseColor = Color.MAGENTA;
	private List<Polyline> currentLines = new ArrayList<>();
	private Marker currentMarker = null;

	public MapFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		updateSettings();
		View v = inflater.inflate(R.layout.fragment_map, container, false);
		selectText = v.findViewById(R.id.mapTextView);
		Drawable defaultIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).colorRes(android.R.color.holo_green_light).sizeDp(40);
		selectText.setCompoundDrawablesWithIntrinsicBounds(defaultIcon, null, null, null);
		selectText.setOnClickListener(v1 -> {
			if (currentMarker != null) {
				Intent intent = new Intent(getActivity(), PersonActivity.class);
				intent.putExtra("personID", ((FullEvent) currentMarker.getTag()).getPersonID());
				startActivity(intent);
			}
		});
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		setHasOptionsMenu(true);
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (getActivity() instanceof MainActivity) {
			inflater.inflate(R.menu.map_menu, menu);
			menu.findItem(R.id.mapMenuSearch).setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search).colorRes(R.color.colorPrimaryText).actionBarSize());
			menu.findItem(R.id.mapMenuSettings).setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear).colorRes(R.color.colorPrimaryText).actionBarSize());
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.setMapStyle((MapStyleOptions.loadRawResourceStyle(this.getContext().getApplicationContext(), R.raw.maps_style)));
		map.clear();
		updateSettings();
		addMarkers();
		String eventID = getActivity().getIntent().getStringExtra("eventID");
		if (eventID != null && !eventID.isEmpty()) {
			FullEvent event = Datacache.getInstance().getEventByID(eventID);
			LatLng latlng = new LatLng(event.getLatitude(), event.getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
			setEventDetails(event);
		}
		map.setOnMarkerClickListener(marker -> {
			currentMarker = marker;
			FullEvent event = (FullEvent) marker.getTag();
			setEventDetails(event);
			drawLines(event);
			return false;
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (map != null && Datacache.getInstance().isSettingsUpdated()) {
			clearLines();
			map.clear();
			updateSettings();
			addMarkers();
			Drawable defaultIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).colorRes(android.R.color.holo_green_light).sizeDp(40);
			selectText.setCompoundDrawablesWithIntrinsicBounds(defaultIcon, null, null, null);
			selectText.setText("Click on a marker to see event details");
			Datacache.getInstance().setSettingsUpdated(false);
		}
	}

	private void setEventDetails(FullEvent event) {
		String eventDetails = event.getPerson().getFirstName() + " " + event.getPerson().getLastName() + "\n"
				+ event.getEventType() + ": " + event.getCity() + " "
				+ event.getCountry() + " (" + event.getYear() + ")";
		Drawable genderIcon = Utility.determineGenderIcon(getActivity(), event.getPerson());
		selectText.setCompoundDrawablesWithIntrinsicBounds(genderIcon, null, null, null);
		selectText.setText(eventDetails);
		drawLines(event);
	}

	private void addMarkers() {
		Datacache cache = Datacache.getInstance();
		List<FullEvent> allEvents = cache.getFilteredEvents();
		for (FullEvent event : allEvents) {
			LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
			float markerColor;
			String eventType = event.getEventType().toLowerCase();
			if (eventColors.containsKey(eventType)) {
				markerColor = eventColors.get(eventType);
			} else {
				markerColor = markerColors.get(nextColor());
				eventColors.put(eventType, markerColor);
			}
			String markerTitle = event.getPerson().getFirstName() + " " + event.getPerson().getLastName() + "'s " + event.getEventType();
			MarkerOptions options = new MarkerOptions().position(location).title(markerTitle)
					.icon(defaultMarker(markerColor));
			Marker marker = map.addMarker(options);
			marker.setTag(event);
		}
		cache.setEventColors(eventColors);
	}

	private void updateSettings() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Datacache.getInstance().setSettings(
				sharedPreferences.getBoolean("lifeStoryLines", true),
				sharedPreferences.getBoolean("familyTreeLines", true),
				sharedPreferences.getBoolean("spouseLines", true),
				sharedPreferences.getBoolean("fatherSide", true),
				sharedPreferences.getBoolean("motherSide", true),
				sharedPreferences.getBoolean("male", true),
				sharedPreferences.getBoolean("female", true)
		);
	}

	private void clearLines() {
		for (Polyline line : currentLines) {
			line.remove();
		}
		currentLines.clear();
	}

	private void drawLines(FullEvent event) {
		clearLines();
		Datacache cache = Datacache.getInstance();
		if (cache.getShowLifeStoryLines()) {
			drawLifeStoryLines(event, cache);
		}
		if (cache.getShowFamilyTreeLines()) {
			drawFamilyTreeLines(event, cache, 10);
		}
		if (cache.getShowSpouseLines()) {
			drawSpouseLines(event, cache);
		}
	}

	private void drawLifeStoryLines(FullEvent event, Datacache cache) {
		List<FullEvent> personEvents = cache.getPersonEvents(event.getPersonID());
		for (int i = 0; i < personEvents.size() - 1; i++) {
			PolylineOptions options = new PolylineOptions()
					.add(new LatLng(personEvents.get(i).getLatitude(), personEvents.get(i).getLongitude()),
							new LatLng(personEvents.get(i + 1).getLatitude(), personEvents.get(i + 1).getLongitude()))
					.color(lifeStoryColor);
			currentLines.add(map.addPolyline(options));
		}
	}

	private void drawFamilyTreeLines(FullEvent event, Datacache cache, float width) {
		// call nextAncestorLine on the person's father and mother
		FullPerson person = cache.getFullPersons().get(event.getPersonID());
		if (person.getFatherID() != null && !person.getFatherID().isEmpty()) {
			FullEvent fatherEvent = cache.getPersonEvents(person.getFatherID()).get(0);
			PolylineOptions options = new PolylineOptions()
					.add(new LatLng(event.getLatitude(), event.getLongitude()),
							new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude()))
					.color(familyTreeColor)
					.width(width);
			currentLines.add(map.addPolyline(options));
			drawFamilyTreeLines(fatherEvent, cache, (float) (width / 1.5));
		}
		if (person.getMotherID() != null && !person.getMotherID().isEmpty()) {
			FullEvent motherEvent = cache.getPersonEvents(person.getMotherID()).get(0);
			PolylineOptions options = new PolylineOptions()
					.add(new LatLng(event.getLatitude(), event.getLongitude()),
							new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude()))
					.color(familyTreeColor)
					.width(width);
			currentLines.add(map.addPolyline(options));
			drawFamilyTreeLines(motherEvent, cache, (float) (width / 1.5));
		}
	}

	private void drawSpouseLines(FullEvent event, Datacache cache) {
		if (event.getPerson().getFatherID() != null && !event.getPerson().getSpouseID().isEmpty()) {
			FullEvent spouseEvent = cache.getPersonEvents(event.getPerson().getSpouseID()).get(0);
			PolylineOptions options = new PolylineOptions()
					.add(new LatLng(event.getLatitude(), event.getLongitude()),
							new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude()))
					.color(spouseColor);
			currentLines.add(map.addPolyline(options));
		}
	}

	private int nextColor() {
		colorIndex = (colorIndex + 1) % markerColors.size();
		return colorIndex;
	}
}
