package com.bemyapp.telethonmobile.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bemyapp.telethonmobile.R;

public class CommentAdapter extends BaseAdapter {

	private final List<Comment> comments = new ArrayList<Comment>();
	private final LayoutInflater myInflater;
	private final Context ctx;

	public CommentAdapter(final List<Comment> _comments, final Context _ctx) {
		if (_comments != null) {
			comments.addAll(_comments);
		}
		myInflater = LayoutInflater.from(_ctx);
		ctx = _ctx;
	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Object getItem(final int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = myInflater.inflate(R.layout.comment_line, null);
			holder = new ViewHolder();
			holder.comments = (TextView) convertView
					.findViewById(R.id.comments);
			holder.ratingBar = (RatingBar) convertView
					.findViewById(R.id.ratingBar);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Comment comment = comments.get(position);
		holder.comments.setText(comment.getComment());
		holder.ratingBar.setRating(comment.getNote());
		return convertView;
	}

	public static class ViewHolder {
		TextView comments;
		RatingBar ratingBar;
	}

}
