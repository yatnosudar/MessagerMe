package core.messager.dochie.adapter;

import core.messager.dochie.bean.GroupDochie;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterGroupDochie extends ArrayAdapter<GroupDochie>{
	private int textViewResourceId;
	
	public AdapterGroupDochie(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.textViewResourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = getWorkingView(convertView);
		ViewHolder viewHolder = getViewHolder(view);
		GroupDochie entry = getItem(position);
		viewHolder.vnamagroup.setText(entry.get_namaGrb());
		viewHolder.vid_group.setText(entry.get_idGrb());
		return view;
	}
	private ViewHolder getViewHolder(View workingView) {
		Object tag = workingView.getTag();
		ViewHolder viewHolder = null;

		if (null == tag || !(tag instanceof ViewHolder)) {
			viewHolder = new ViewHolder();

			viewHolder.vnamagroup = (TextView) workingView
					.findViewById(core.messager.dochie.R.id.vnama_group_dochie);
			viewHolder.vid_group = (TextView) workingView
					.findViewById(core.messager.dochie.R.id.vid_group);
			workingView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) tag;
		}

		return viewHolder;
	}
	private View getWorkingView(View convertView) {
		View workingView = null;

		if (null == convertView) {
			Context context = getContext();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			workingView = inflater.inflate(textViewResourceId, null);
		} else {
			workingView = convertView;
		}

		return workingView;
	}
	class ViewHolder {
		public TextView vnamagroup;
		public TextView vid_group;
	}
}
