package core.messager.dochie;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import core.messager.dochie.adapter.AdapterUserAddGroupDochie;
import core.messager.dochie.bean.UserDochie;
import core.messager.dochie.model.ModelGroupUserDochie;
import core.messager.dochie.model.ModelUserDochie;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddUserGroupActivity extends Activity {
	private ModelUserDochie musers;
	private ListView list_users;
	private TextView txtHeader;
	private TextView txtSecondHeader;
	private AdapterUserAddGroupDochie adapter_users;
	private boolean startpg = false;
	ProgressDialog pg;
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_group_user_dochie);
		musers = new ModelUserDochie(this);
		musers.open();

		list_users = (ListView) findViewById(R.id.list_users);
		adapter_users = new AdapterUserAddGroupDochie(this,
				R.layout.user_dochie_list_chekbox);
		list_users.setAdapter(adapter_users);

		for (UserDochie entry : getNewsEntries()) {
			adapter_users.add(entry);
		}

		txtHeader = (TextView) findViewById(R.id.textHeader);
		txtSecondHeader = (TextView) findViewById(R.id.txtsecondheader);

		txtHeader.setText("Dochie Messager");
		txtSecondHeader.setText("Contact");
		
		 pg = new ProgressDialog(
					AddUserGroupActivity.this);
			pg.setTitle("Add User to Group");
			pg.setMessage("Loading...");

	}

	// adapter List
	private List<UserDochie> getNewsEntries() {
		List<UserDochie> entries = new ArrayList<UserDochie>();
		entries = musers.getAllMessage();
		return entries;
	}

	public void onClickDone(View v) {
		Bundle extras = getIntent().getExtras();
		final String idGrb = extras.getString("param1");
		final String namagroub = extras.getString("param2");
		new AddUserBack().execute(idGrb,namagroub);
	}
	
	public void onClickCancel(View v){
		Bundle extras = getIntent().getExtras();
		String idGrb = extras.getString("param1");
		String namagroub = extras.getString("param2");
		Intent i = new Intent(
				AddUserGroupActivity.this.getApplicationContext(),
				UserGroupActivity.class);
		i.putExtra("param1", idGrb);
		i.putExtra("param2", namagroub);
		startActivity(i);
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		musers.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		musers.close();
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		Bundle extras = getIntent().getExtras();
		String idGrb = extras.getString("param1");
		String namagroub = extras.getString("param2");
		Intent i = new Intent(
				AddUserGroupActivity.this.getApplicationContext(),
				UserGroupActivity.class);
		i.putExtra("param1", idGrb);
		i.putExtra("param2", namagroub);
		startActivity(i);
		finish();
		super.onBackPressed();
	}
	
	class AddUserBack extends AsyncTask<String, Integer, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pg.show();
		}
		@Override
		protected Boolean doInBackground(String... p) {
			final String idGrb = p[0];
			final String namagroub = p[1];
			String uri = core.messager.dochie.constant.Constants.HOST_JSON+"/addUserGroup";
			final ModelGroupUserDochie addGrb = new ModelGroupUserDochie(AddUserGroupActivity.this);

			Log.i("id groub", idGrb);
			addGrb.open();
            
                for (int i = 0; i < adapter_users.getCheked().length; i++) {
					Log.d("posisi " + i, adapter_users.getCheked()[i] + "");

					if (adapter_users.getCheked()[i] == true) {
						final long idUserGroup = adapter_users.getItem(i)
								.get_idUsr();
						RequestParams params = new RequestParams();
						params.put("id_group", idGrb);
						params.put("id_user", adapter_users.getItem(i)
								.get_nohpUsr());
						AsyncHttpClient ahc = new AsyncHttpClient();
						ahc.post(uri, params, new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(String arg0) {
								addGrb.creatUserGroup(idUserGroup, idGrb);
								super.onSuccess(arg0);
							}

							@Override
							public void onFinish() {
								Intent i = new Intent(
										AddUserGroupActivity.this
												.getApplicationContext(),
										UserGroupActivity.class);
								i.putExtra("param1", idGrb);
								i.putExtra("param2", namagroub);
								startActivity(i);
								super.onFinish();
							}
							
							@Override
							public void onFailure(Throwable arg0) {
								Toast.makeText(AddUserGroupActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
								super.onFailure(arg0);
							}
						});

					}
				}
			return true;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			if (startpg) {
				pg.dismiss();
			}
			super.onPostExecute(result);
		}
		
	}
}
