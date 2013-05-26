package core.messager.dochie;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import core.messager.dochie.adapter.AdapterUserPesanDochie;
import core.messager.dochie.bean.UserPesanDochie;
import core.messager.dochie.constant.StaticVariable;
import core.messager.dochie.helper.DochieLogManager;
import core.messager.dochie.model.ModelUserPesanDochie;

public class PesanContactActivity extends Activity {
	private ModelUserPesanDochie mpd;
	
	private ListView listUserPesan;
	private TextView txtHeader;
	private TextView txtSecondHeader;
	private BroadcastReceiver mReceiver;
	private IntentFilter intentFilter;
	
	public static AdapterUserPesanDochie apd;
	
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
		
		mpd = new ModelUserPesanDochie(PesanContactActivity.this);
		mpd.open();
		listUserPesan = (ListView) findViewById(R.id.list_users);
		apd = new AdapterUserPesanDochie(this,R.layout.pesan_user_dochie_list);
		
		listUserPesan.setAdapter(apd);
		for (UserPesanDochie entry : getNewsEntries()) {
			apd.add(entry);
		}
		
		listUserPesan.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				UserPesanDochie entryP = apd.getItem(position);
				Intent i = new Intent(PesanContactActivity.this,
						PesanActivity.class);
				i.putExtra("nama", entryP.get_namaUsr());
				i.putExtra("nohp", entryP.get_nohpUsr());
				i.putExtra("vid",String.valueOf( entryP.get_idUsr()));
				mpd.updateDataPesan(entryP.get_idUsr());
				
				ImageView iv = (ImageView) arg1.findViewById(R.id.notifPesan);
				iv.setImageResource(R.drawable.mail_mail);
				
				startActivity(i);
			}
		});
		txtHeader = (TextView) findViewById(R.id.textHeader);
		txtSecondHeader = (TextView) findViewById(R.id.txtsecondheader);
		
		txtHeader.setText("Dochie Messager");
		txtSecondHeader.setText("Message");
		
		intentFilter = new IntentFilter("android.intent.action.PESAN");
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				apd.clear();
				for (UserPesanDochie entry : getNewsEntries()) {
					apd.add(entry);
				}
				apd.notifyDataSetChanged();
			}			
		};
		this.registerReceiver(mReceiver, intentFilter);
		mpd.close();
	}
	
	public void btnHomeAction(View view){
		DochieLogManager.INFORMATION("Tekan Home", "Tekan Tombol HOME");
	}
	
	public void tekanLeft1(View view){
		DochieLogManager.INFORMATION("Tekan Left 1","Tekan Left 1");
	}
	//btn action bar
	public void tekanLeft2(View view){
		startActivity(new Intent(PesanContactActivity.this, UserActivity.class));
	}
	
	// adapter List
		private List<UserPesanDochie> getNewsEntries() {
			List<UserPesanDochie> entries = new ArrayList<UserPesanDochie>();
			entries = mpd.getAllMessage();
			return entries;
		}
		
		@Override
		protected void onDestroy() {
			mpd.close();
			super.onDestroy();
			
		}
		@Override
		protected void onPause() {
			
			mpd.close();
			this.unregisterReceiver(this.mReceiver);
			super.onStop();
			
		}
		@Override
		protected void onResume() {
			mpd.open();
			this.registerReceiver(this.mReceiver, intentFilter);
			super.onResume();
			
		}
}
