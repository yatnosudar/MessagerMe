package core.messager.dochie.adapter;

import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import core.messager.dochie.R;
import core.messager.dochie.R.color;
import core.messager.dochie.bean.UserPesanDochie;

public class AdapterUserPesanDochie extends ArrayAdapter<UserPesanDochie> {
	private int textViewResourceId;

	public AdapterUserPesanDochie(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.textViewResourceId = textViewResourceId;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = getWorkingView(convertView);
		ViewHolder viewHolder = getViewHolder(view);
		UserPesanDochie userpesan = getItem(position);
		
		String isiPesan = userpesan.get_isiPsn();
		long _isOpen = userpesan.get_isOpen();
		StringTokenizer isiPesanToken = new StringTokenizer(isiPesan);
		
		
		if (isiPesan.length()>20) {
			viewHolder.isiPesan.setText(isiPesan.substring(0,20));
		}else{
			viewHolder.isiPesan.setText(isiPesan);
		}
		viewHolder.namaUser.setText(userpesan.get_namaUsr());
		
		if (_isOpen>0) {
			viewHolder.notificationPesan.setImageResource(R.drawable.inbox_mail);
		}
		Log.i("is_Open?",""+_isOpen);
		
		
		return view;
	}
	
	private ViewHolder getViewHolder(View workingView) {
		Object tag = workingView.getTag();
		ViewHolder viewHolder = null;

		if (null == tag || !(tag instanceof ViewHolder)) {
			viewHolder = new ViewHolder();

			viewHolder.namaUser = (TextView) workingView
					.findViewById(core.messager.dochie.R.id.vnama_pesan_user);
			viewHolder.isiPesan = (TextView) workingView
					.findViewById(core.messager.dochie.R.id.visi_pesan);
			viewHolder.notificationPesan = (ImageView) workingView
					.findViewById(core.messager.dochie.R.id.notifPesan);
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
	
	class ViewHolder{
		public TextView namaUser;
		public TextView isiPesan;
		public ImageView notificationPesan;
	}
}
