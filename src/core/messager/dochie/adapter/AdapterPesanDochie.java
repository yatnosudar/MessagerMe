package core.messager.dochie.adapter;

import core.messager.dochie.R;
import core.messager.dochie.adapter.AdapterUserDochie.ViewHolder;
import core.messager.dochie.bean.PesanDochie;
import core.messager.dochie.bean.UserDochie;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.LiveFolders;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AdapterPesanDochie extends ArrayAdapter<PesanDochie>{
	private int textViewResourceId;
	
	public AdapterPesanDochie(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.textViewResourceId=textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = getWorkingView(convertView);
		ViewHolder viewHolder = getViewHolder(view);
		PesanDochie entry = getItem(position);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.LEFT;

		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.gravity = Gravity.RIGHT;
		
		if (entry.get_isMe()==0) {
			viewHolder.vimage1.setVisibility(view.VISIBLE);
			viewHolder.vimage2.setVisibility(view.INVISIBLE);
			viewHolder.vlinearBox.setLayoutParams(params);
			viewHolder.vlinearPesan.setBackgroundResource(R.drawable.shape2);
			viewHolder.vtxtPesan.setText(entry.get_isiPsn());
			if (entry.get_isSend()==1) {
				viewHolder.cek_1.setVisibility(view.INVISIBLE);
				viewHolder.cek_2.setVisibility(view.INVISIBLE);
			}else if(entry.get_isSend()==0){
				viewHolder.cek_1.setVisibility(view.INVISIBLE);
				viewHolder.cek_2.setVisibility(view.INVISIBLE);
			}
		}else if (entry.get_isMe()==1) {
			viewHolder.vimage1.setVisibility(view.INVISIBLE);
			viewHolder.vimage2.setVisibility(view.VISIBLE);
			viewHolder.vlinearBox.setLayoutParams(params2);
			viewHolder.vlinearPesan.setBackgroundResource(R.drawable.shape);
			viewHolder.vtxtPesan.setText(entry.get_isiPsn());
			if (entry.get_isSend()==1) {
				viewHolder.cek_1.setVisibility(view.VISIBLE);
				viewHolder.cek_2.setVisibility(view.VISIBLE);
			}else if(entry.get_isSend()==0){
				viewHolder.cek_1.setVisibility(view.VISIBLE);
				viewHolder.cek_2.setVisibility(view.INVISIBLE);
			}
			
		}
		
		return view;
	}
	private ViewHolder getViewHolder(View workingView) {
		Object tag = workingView.getTag();
		ViewHolder viewHolder = null;

		if (null == tag || !(tag instanceof ViewHolder)) {
			viewHolder = new ViewHolder();

			viewHolder.vtxtPesan = (TextView) workingView
					.findViewById(core.messager.dochie.R.id.txtpesan);
			viewHolder.vlinearPesan = (LinearLayout) workingView
					.findViewById(core.messager.dochie.R.id.txtshapebox);
			viewHolder.vimage1 = (ImageView) workingView
					.findViewById(core.messager.dochie.R.id.shape1);
			viewHolder.vimage2 = (ImageView) workingView
					.findViewById(core.messager.dochie.R.id.shape2);
			viewHolder.vlinearBox = (LinearLayout) workingView
					.findViewById(core.messager.dochie.R.id.linearBox);
			viewHolder.cek_1 = (ImageView) workingView.findViewById(R.id.cek_1);
			viewHolder.cek_2 = (ImageView) workingView.findViewById(R.id.cek_2);
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
		public TextView vtxtPesan;
		public LinearLayout vlinearPesan;
		public LinearLayout vlinearBox;
		public ImageView vimage1;
		public ImageView vimage2;
		public ImageView cek_1;
		public ImageView cek_2;
		
	}
}
