package com.example.administrator.discussapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class CustomAdapter extends ParseQueryAdapter<ParseObject> {
	

	
	public CustomAdapter(Context context) {
		// Use the QueryFactory to construct a PQA that will only show
		// Todos marked as high-pri
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery create() {
				ParseUser currentUser = ParseUser.getCurrentUser();
				ParseQuery query = new ParseQuery(currentUser.getUsername());
				// query.whereEqualTo("high", true);

				return query;
			}
		});
	}

	// Customize the layout by overriding getItemView
	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {

		if (v == null) {
			v = View.inflate(getContext(), R.layout.activity_column_cate,
					null);
		}

		super.getItemView(object, v, parent);
		
		// Add and download the image
		ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.icon);
		ParseFile imageFile = object.getParseFile("Photo");
		if (imageFile != null) {
			todoImage.setParseFile(imageFile);
			todoImage.loadInBackground();
		}

		// Add the title view
		TextView NameTextView = (TextView) v.findViewById(R.id.text1);
		NameTextView.setText(object.getString("Name"));
		
		// sum((Integer)object.getNumber("Price"));
		TextView PriceTextView = (TextView) v.findViewById(R.id.text2);
		PriceTextView.setText(object.getNumber("Price").toString() + " "
				+ "�ҷ");
		
		// Add a reminder of how long this item has been outstanding
		TextView timestampView = (TextView) v.findViewById(R.id.timestamp);
		timestampView.setText(object.getCreatedAt().toString());
		return v;
	}

}