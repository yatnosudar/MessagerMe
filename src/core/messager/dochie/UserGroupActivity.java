package core.messager.dochie;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import core.messager.dochie.adapter.AdapterGroupUsersDochie;
import core.messager.dochie.bean.GroupUserDochie;
import core.messager.dochie.helper.DochieSendMessageHelper;
import core.messager.dochie.helper.callback.Abstract_DochieSendEmail;
import core.messager.dochie.helper.callback.Callback_DochieSendEmail;
import core.messager.dochie.model.ModelGroupUserDochie;
import core.messager.dochie.model.ModelPesanDochie;

public class UserGroupActivity extends Activity {
	private ModelGroupUserDochie mgroup;
	private ListView list_users;
	private TextView txtHeader;
	private TextView txtSecondHeader;
	private ImageButton btnLeftFirst;
	private ImageButton btnLeftSecond;
	private AdapterGroupUsersDochie adapter_users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//stricmode
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()   // or .detectAll() for all detectable problems
        .penaltyLog()
        .build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .penaltyLog()
        .penaltyDeath()
        .build());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_dochie);

		mgroup = new ModelGroupUserDochie(this);
		mgroup.open();

		list_users = (ListView) findViewById(R.id.list_users);
		adapter_users = new AdapterGroupUsersDochie(this, R.layout.user_dochie_list);
		list_users.setAdapter(adapter_users);
		for (GroupUserDochie entry : getNewsEntries()) {
			adapter_users.add(entry);
		}
		txtHeader = (TextView) findViewById(R.id.textHeader);
		txtSecondHeader = (TextView) findViewById(R.id.txtsecondheader);
		btnLeftFirst = (ImageButton) findViewById(R.id.btnLeft1);
		btnLeftSecond = (ImageButton) findViewById(R.id.btnLeft2);

		btnLeftFirst.setImageResource(R.drawable.social_group);

		// action listView
		list_users.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String nama = ((TextView) view
						.findViewById(core.messager.dochie.R.id.vnama_user_dochie))
						.getText().toString();
				String nohp = ((TextView) view
						.findViewById(core.messager.dochie.R.id.vnohp_user_dochie))
						.getText().toString();
				String vid = ((TextView) view
						.findViewById(core.messager.dochie.R.id.vid_users))
						.getText().toString();
				list_users.getItemAtPosition(position);
				Intent i = new Intent(UserGroupActivity.this,
						PesanActivity.class);
				i.putExtra("nama", nama);
				i.putExtra("nohp", nohp);
				i.putExtra("vid", vid);
				startActivity(i);
			}
		});

		txtHeader.setText("Dochie Messager");
		Bundle extras = getIntent().getExtras();
		txtSecondHeader.setText(extras.getString("param2"));
	}

	// adapter List
	private List<GroupUserDochie> getNewsEntries() {
		List<GroupUserDochie> entries = new ArrayList<GroupUserDochie>();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String bar = extras.getString("param1");
			Log.v("param1", bar);
			entries = mgroup.getAllUsersGroup(bar);
		}

		return entries;
	}

	public void tekanLeft1(View v) {
		Bundle extras = getIntent().getExtras();
		Intent i = new Intent(
				UserGroupActivity.this.getApplicationContext(),
				AddUserGroupActivity.class);
		i.putExtra("param1", extras.getString("param1"));
		i.putExtra("param2", extras.getString("param2"));
		startActivity(i);
		finish();

	}

	public void tekanLeft2(View v) {
		final Dialog d = new Dialog(UserGroupActivity.this);
		d.setContentView(R.layout.broadcast_dialog);
		d.setTitle("Broadcast Message");
		d.show();
		final Button btnd = (Button) d.findViewById(R.id.btndialog);
		final EditText edt = (EditText) d.findViewById(R.id.editTxt);

		final String[] toMessage = new String[getNewsEntries().size()];
		
		for (int i = 0; i < getNewsEntries().size(); i++) {
			GroupUserDochie ug = getNewsEntries().get(i);
			toMessage[i] = ug.get_nohpUsr();
			Log.i("Email ", ug.get_nohpUsr());
		}

		btnd.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String[] message = new String[1];
				message[0] = edt.getText().toString();
				if (edt.getText().equals("") || edt.getText().equals(null)) {
					Toast.makeText(getApplicationContext(),
							"Must be Enter Text", Toast.LENGTH_SHORT).show();
				} else {
					new KirimEmailBg(UserGroupActivity.this).execute(
							message, toMessage);
					d.dismiss();
				}
			}
		});
	}

	public void btnHomeAction(View v) {
		startActivity(new Intent(UserGroupActivity.this, PesanContactActivity.class));
	}
	@Override
	protected void onResume() {
		mgroup.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mgroup.close();
		super.onPause();
	}

	class KirimEmailBg extends AsyncTask<String[], Integer, Boolean> {
		Context c ;
		ProgressDialog pg;

		public KirimEmailBg(Context c) {
			pg = new ProgressDialog(c);
			this.c = c;
		}

		@Override
		protected void onPreExecute() {
			pg.setTitle("BroadCast Message");
			pg.setMessage("Loading...");
			pg.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String[]... params) {
			String bodyMessage[] = params[0];
			String toMessage[] = params[1];
			DochieSendMessageHelper djh = new DochieSendMessageHelper(c);
			Log.i("proses sending", bodyMessage[0]);
			final String currentDateTimeString = DateFormat.getDateTimeInstance()
					.format(new Date());
			
			new Callback_DochieSendEmail(c).kirimEmailAbstractGroub(toMessage, bodyMessage[0], new Abstract_DochieSendEmail() {
				@Override
				public void s_sendEmail(String[] array, String body) {
					ModelPesanDochie mpesan = new ModelPesanDochie(c);
					mpesan.open();
					for (int i = 0; i < array.length; i++) {
						mpesan.createMessage2(array[i], body, null, currentDateTimeString, 1, 1, 0, 0);
					}
					mpesan.close();
					super.s_sendEmail(array, body);
				}
				
				@Override
				public void f_sendEmail(String[] array, String body) {
					ModelPesanDochie mpesan = new ModelPesanDochie(c);
					mpesan.open();
					for (int i = 0; i < array.length; i++) {
						mpesan.createMessage2(array[i], body, null, currentDateTimeString, 0, 1, 0, 0);
					}
					mpesan.close();
					super.f_sendEmail(array, body);
				}
				@Override
				public void b_sendEmail(String[] array, String body, String e) {
					ModelPesanDochie mpesan = new ModelPesanDochie(c);
					mpesan.open();
					for (int i = 0; i < array.length; i++) {
						mpesan.createMessage2(array[i], body, null, currentDateTimeString, 0, 1, 0, 0);
					}
					mpesan.close();
					super.b_sendEmail(array, body, e);
				}
			});
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				pg.dismiss();
			}
			super.onPostExecute(result);
		}
	}

}
