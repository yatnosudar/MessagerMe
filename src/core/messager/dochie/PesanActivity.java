package core.messager.dochie;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import core.messager.dochie.adapter.AdapterPesanDochie;
import core.messager.dochie.bean.PesanDochie;
import core.messager.dochie.constant.StaticVariable;
import core.messager.dochie.helper.DochiePreferenceHelper;
import core.messager.dochie.helper.DochieSendMessageHelper;
import core.messager.dochie.helper.callback.Abstract_DochieSendEmail;
import core.messager.dochie.helper.callback.Callback_DochieSendEmail;
import core.messager.dochie.model.ModelPesanDochie;
import core.messager.dochie.service.DochieServiceGettingEmail;

public class PesanActivity extends Activity {

	private ListView pesanList;
	private AdapterPesanDochie adapter_pesan;
	private ModelPesanDochie modelPesan;
	private EditText txtPesan;
	private ImageButton btnImage;
	private TextView txtHeader;
	private TextView txtSecondHeader;
	private ImageButton btnLeft1;
	private ImageButton btnLeft2;
	private String nama = "";
	private String nohp = "";
	private int _vid = 0;
	private String vid = "";
	private DochiePreferenceHelper dph;
	private BroadcastReceiver mReceiver;
	private IntentFilter intentFilter;

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
		setContentView(R.layout.pesan_dochie);

		dph = new DochiePreferenceHelper(PesanActivity.this);
		pesanList = (ListView) findViewById(R.id.pesandochie);
		txtPesan = (EditText) findViewById(R.id.txtViewPesan);
		btnImage = (ImageButton) findViewById(R.id.btnSendButton);

		modelPesan = new ModelPesanDochie(PesanActivity.this);
		modelPesan.open();
		adapter_pesan = new AdapterPesanDochie(PesanActivity.this,
				R.layout.pesan_list_dochie);
		pesanList.setAdapter(adapter_pesan);

		Bundle b = getIntent().getExtras();
		nama = b.getString("nama");
		nohp = b.getString("nohp");
		vid = b.getString("vid");

		for (PesanDochie entry : getNewsEntries(vid)) {
			adapter_pesan.add(entry);
		}
		pesanList.setSelection(getNewsEntries(vid).size());

		txtHeader = (TextView) findViewById(R.id.textHeader);
		txtSecondHeader = (TextView) findViewById(R.id.txtsecondheader);
		btnLeft1 = (ImageButton) findViewById(R.id.btnLeft1);
		btnLeft2 = (ImageButton) findViewById(R.id.btnLeft2);

		btnLeft2.setImageResource(R.drawable.attachment_red);
		txtHeader.setText(nama);
		txtSecondHeader.setText(nohp);

		btnLeft1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Refresh",
						Toast.LENGTH_SHORT).show();
				startService(new Intent(PesanActivity.this,
						DochieServiceGettingEmail.class));
			}
		});

		btnLeft2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						"Attach belum tersedia", Toast.LENGTH_SHORT).show();
			}
		});

		intentFilter = new IntentFilter("android.intent.action.PESAN");
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				if (StaticVariable.pd.get_idUser() == Integer.parseInt(vid)) {
					adapter_pesan.add(StaticVariable.getPd());
					pesanList.setSelection(adapter_pesan.getCount());
				}

			}
		};
		this.registerReceiver(mReceiver, intentFilter);
	}

	public void btnHomeAction(View v) {
		startActivity(new Intent(PesanActivity.this, PesanContactActivity.class));
	}

	@Override
	protected void onResume() {
		modelPesan.open();
		this.registerReceiver(this.mReceiver, intentFilter);
		super.onResume();
	}

	@Override
	protected void onPause() {
		modelPesan.close();
		this.unregisterReceiver(this.mReceiver);
		super.onPause();
	}

	// adapter List
	private List<PesanDochie> getNewsEntries(String vid) {

		List<PesanDochie> entries = new ArrayList<PesanDochie>();
		entries = modelPesan.getAllMessage(Integer.parseInt(vid));

		return entries;
	}

	public void kirimPesan(View v) {
		if (txtPesan.getText().toString().equals(null)
				|| txtPesan.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "Must be Enter Text",
					Toast.LENGTH_SHORT).show();
		} else {
			String ambilData = txtPesan.getText().toString();
			String currentDateTimeString = DateFormat.getDateTimeInstance()
					.format(new Date());
			// tambah pesan
			adapter_pesan.add(modelPesan.createMessage(Integer.parseInt(vid),
					ambilData, null, currentDateTimeString, 0, 1, 0));

			new kirimEmail().execute("From : ", dph.getNoHp() + "|" + ambilData
					+ "|" + currentDateTimeString);
			txtPesan.setText("");
		}
		pesanList.setSelection(adapter_pesan.getCount());

	}

	private class kirimEmail extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.e("sendEmail", params[0] + params[1]);

			Callback_DochieSendEmail clb = new Callback_DochieSendEmail(
					PesanActivity.this);
			clb.kirimEmailAbstract(nohp + "@"
					+ core.messager.dochie.constant.Constants.HOST_EMAIL,
					params[1], new Abstract_DochieSendEmail() {
						@Override
						public void s_sendEmail(String toMessage,
								String bodyMessage) {
							int a = adapter_pesan.getCount() - 1;
							PesanDochie b = adapter_pesan.getItem(a);
							modelPesan.updateMessageStatus(b.get_idPsn(), 1);
							
							super.s_sendEmail(toMessage, bodyMessage);
						}

						@Override
						public void b_sendEmail(String toMessage,
								String bodyMessage, String e) {
							Log.e("Error Pengiriman Email", toMessage + " and "
									+ bodyMessage + " : " + e);
							super.b_sendEmail(toMessage, bodyMessage, e);
						}

						@Override
						public void f_sendEmail(String toMessage,
								String bodyMessage) {
							Log.e("Network Failure ", toMessage + " and "
									+ bodyMessage);
							super.f_sendEmail(toMessage, bodyMessage);
						}

					});
			return null;
		}
	}

}
