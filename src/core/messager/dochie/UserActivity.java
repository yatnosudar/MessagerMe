package core.messager.dochie;

import java.util.ArrayList;
import java.util.List;
import core.messager.dochie.adapter.AdapterUserDochie;
import core.messager.dochie.bean.UserDochie;
import core.messager.dochie.constant.StaticVariable;
import core.messager.dochie.helper.DochieAsynTaskSync;
import core.messager.dochie.model.ModelUserDochie;
import core.messager.dochie.sync.ContactSyncAdapter;
import core.messager.dochie.sync.ContactSyncService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class UserActivity extends Activity {
	private ModelUserDochie musers;
	private ListView list_users;
	private TextView txtHeader;
	private TextView txtSecondHeader;
	private ImageButton btnAction2;
	private ImageButton btnAction1;
	private BroadcastReceiver mReceiver;
	private IntentFilter intentFilter;

	public static AdapterUserDochie adapter_users;

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
		musers = new ModelUserDochie(this);
		musers.open();

		list_users = (ListView) findViewById(R.id.list_users);
		adapter_users = new AdapterUserDochie(this, R.layout.user_dochie_list);
		list_users.setAdapter(adapter_users);

		for (UserDochie entry : getNewsEntries()) {
			adapter_users.add(entry);
		}

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
				Intent i = new Intent(UserActivity.this,
						PesanActivity.class);
				i.putExtra("nama", nama);
				i.putExtra("nohp", nohp);
				i.putExtra("vid", vid);
				startActivity(i);
			}
		});

		txtHeader = (TextView) findViewById(R.id.textHeader);
		txtSecondHeader = (TextView) findViewById(R.id.txtsecondheader);
		txtHeader.setText("Dochie Messager");
		txtSecondHeader.setText("Contact");

		btnAction2 = (ImageButton) findViewById(R.id.btnLeft2);
		btnAction2.setImageResource(R.drawable.social_add_group);
		btnAction2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(UserActivity.this,
						GroupActivity.class);
				startActivity(i);
			}
		});

		btnAction1 = (ImageButton) findViewById(R.id.btnLeft1);
		btnAction1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new DochieAsynTaskSync().execute("start");
			}
		});

		intentFilter = new IntentFilter("android.intent.action.MAIN");
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle i = intent.getExtras();
				if (i.getString("contact").equals("contact")) {
					adapter_users.add(StaticVariable.getUc());
					list_users.setSelection(adapter_users.getCount());
				}
			}
		};
		this.registerReceiver(mReceiver, intentFilter);

		musers.close();
	}

	// adapter List
	private List<UserDochie> getNewsEntries() {
		List<UserDochie> entries = new ArrayList<UserDochie>();
		entries = musers.getAllMessage();
		return entries;
	}
	
	public void btnHomeAction(View v) {
		startActivity(new Intent(UserActivity.this, PesanContactActivity.class));
	}

	@Override
	protected void onPause() {
		this.unregisterReceiver(this.mReceiver);
		super.onPause();
		musers.close();

	}

	@Override
	protected void onResume() {
		this.registerReceiver(this.mReceiver, intentFilter);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		musers.close();
	}

}
