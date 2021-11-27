package com.example.fmc;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fmc.extendedModels.FullEvent;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import model.Event;
import model.Person;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
	private SearchView searchView;
	private RecyclerView recyclerView;
	private Context activity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		SearchAdapter adapter = new SearchAdapter(new ArrayList<>(), this);
		recyclerView.setAdapter(adapter);
		searchView = findViewById(R.id.search_bar);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.length() == 0) {
					recyclerView.setAdapter(new SearchAdapter(new ArrayList<>(), activity));
				}
				else {
					ArrayList<Object> newList = Datacache.getInstance().getSearchFilteredItems(newText);
					recyclerView.setAdapter(new SearchAdapter(newList, activity));
				}
				return false;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		return true;
	}

	private class SearchHolder extends RecyclerView.ViewHolder {

		TextView textView;

		public SearchHolder(@NonNull View view) {
			super(view);
			textView = view.findViewById(R.id.list_item);
		}
	}

	private class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

		private ArrayList<Object> searchResults;
		private Context context;

		public SearchAdapter(ArrayList<Object> searchResults, Context context) {
			this.searchResults = searchResults;
			this.context = context;
		}

		@NonNull
		@Override
		public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
			return new SearchHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
			Object item = searchResults.get(position);
			if (item instanceof Person) {
				Person person = (Person) item;
				String text = person.getFirstName() + " " + person.getLastName();
				holder.textView.setText(text);
				holder.textView.setCompoundDrawablesWithIntrinsicBounds(Utility.determineGenderIcon(context, person), null, null, null);
				holder.textView.setOnClickListener(v -> {
					Intent intent = new Intent(context, PersonActivity.class);
					intent.putExtra("personID", person.getPersonID());
					startActivity(intent);
				});
			}
			else {
				FullEvent event = (FullEvent) item;
				String text = event.getEventType() + ": " + event.getCity() + " "
						+ event.getCountry() + " (" + event.getYear() + ")" + "\n\n" +
						event.getPerson().getFirstName() + " " + event.getPerson().getLastName();
				holder.textView.setText(text);
				Drawable icon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker).colorRes(android.R.color.holo_blue_bright).sizeDp(40);
				holder.textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
				holder.textView.setOnClickListener(v -> {
					Intent intent = new Intent(context, EventActivity.class);
					intent.putExtra("eventID", event.getEventID());
					startActivity(intent);
				});
			}
		}

		@Override
		public int getItemCount() {
			return searchResults.size();
		}
	}
}
