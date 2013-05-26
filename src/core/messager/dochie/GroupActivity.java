package core.messager.dochie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import core.messager.dochie.adapter.AdapterGroupDochie;
import core.messager.dochie.bean.GroupDochie;
import core.messager.dochie.constant.StaticVariable;
import core.messager.dochie.helper.DochiePreferenceHelper;
import core.messager.dochie.model.ModelGroupDochie;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager.Request;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupActivity extends Activity {
	private ModelGroupDochie mgroups;
	private ListView list_groups;
	private TextView txtHeader;
	private TextView txtSecondHeader;
	private ImageButton btnLeft1;
	private ImageButton btnLeft2;
	private AdapterGroupDochie adapter_groups;

	
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
		setContentView(R.layout.group_dochie);
		mgroups = new ModelGroupDochie(this);
		mgroups.open();
		
		list_groups = (ListView) findViewById(R.id.list_goups);
		adapter_groups = new AdapterGroupDochie(this,
				R.layout.group_dochie_list);
		list_groups.setAdapter(adapter_groups);
		
		for (GroupDochie entry : getNewsEntries()) {
			adapter_groups.add(entry);
		}
		txtHeader = (TextView) findViewById(R.id.textHeader);
		txtSecondHeader = (TextView) findViewById(R.id.txtsecondheader);
		btnLeft1 = (ImageButton) findViewById(R.id.btnLeft1);
		btnLeft2 = (ImageButton) findViewById(R.id.btnLeft2);

		btnLeft2.setImageResource(R.drawable.social_add_group);
		txtHeader.setText("Dochie Messager");
		txtSecondHeader.setText("Groups");

		list_groups.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String idGroups = ((TextView) arg1.findViewById(R.id.vid_group))
						.getText().toString();
				String namaGroup = ((TextView) arg1
						.findViewById(R.id.vnama_group_dochie)).getText()
						.toString();
				Intent i = new Intent(GroupActivity.this
						.getApplicationContext(),
						UserGroupActivity.class);
				i.putExtra("param1", idGroups);
				i.putExtra("param2", namaGroup);
				startActivity(i);
			}
		});
		
		list_groups.setOnItemLongClickListener(	new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						final String idGroups = ((TextView) arg1.findViewById(R.id.vid_group))
								.getText().toString();
//						final group_dochie g = null;
//						String namaGroup = ((TextView) arg1
//								.findViewById(R.id.vnama_group_dochie)).getText()
//								.toString();
						final int listposition = arg2;
						TextView a = (TextView)arg1.findViewById(R.id.vnama_group_dochie);
						final String dataforUpdate = a.getText().toString();
						final CharSequence[] items = {"Update Group", "Delete Group"};

						AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
						builder.setTitle("Pick a color");
						builder.setItems(items, new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog, int item) {
						         //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
						         if (item==0) {
									final Dialog dialogUpdate = new Dialog(GroupActivity.this);
									dialogUpdate.setTitle("Update Group");
									dialogUpdate.setContentView(R.layout.add_grub_dialog);
									TextView dataUpdate = (TextView)dialogUpdate.findViewById(R.id.txtGroupAdd);
									dataUpdate.setText(dataforUpdate);
									
									
									//mgroups = new mgroup_dochie(group_dochie_activity.this);
									//mgroups.open();
									final Button tambahGroup = (Button)dialogUpdate.findViewById(R.id.btnTambahGroup);
									final EditText tambahNamaGroup = (EditText)dialogUpdate.findViewById(R.id.txtGroupAdd);
									tambahGroup.setOnClickListener(new OnClickListener() {
										public void onClick(View v) {
											adapter_groups.remove(adapter_groups.getItem(listposition));
											adapter_groups.notifyDataSetChanged();
											Log.v("Sukses", "sukses");
											
											String group = tambahNamaGroup.getText().toString();
											String owner = new DochiePreferenceHelper(GroupActivity.this).getNoHp();
											String url = core.messager.dochie.constant.Constants.HOST_JSON+"/updateGroup";
											
											RequestParams params = new RequestParams();
											params.put("group", group);
											params.put("id_group", idGroups);
											params.put("owner", owner);
											
											AsyncHttpClient ahc = new AsyncHttpClient();
											ahc.post(url, params, new AsyncHttpResponseHandler(){
												public void onSuccess(String arg0) {
													adapter_groups.add(mgroups.updateGroup(idGroups,tambahNamaGroup.getText().toString()));
													adapter_groups.notifyDataSetChanged();
													dialogUpdate.dismiss();
												};

												public void onFailure(Throwable arg0) {
													Toast.makeText(GroupActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
												};
											});
											
											
										}
									});
									
									dialogUpdate.show();
									
								}else if (item==1) {
									String url =core.messager.dochie.constant.Constants.HOST_JSON+"/deleteGroup";
									RequestParams params = new RequestParams();
									params.put("id_group",idGroups);
									
									AsyncHttpClient ahc = new AsyncHttpClient();
									ahc.post(url,params,new AsyncHttpResponseHandler(){
										public void onSuccess(String arg0) {
											mgroups.deleteUsers(new GroupDochie(idGroups));
											adapter_groups.remove(adapter_groups.getItem(listposition));
											adapter_groups.notifyDataSetChanged();
										};
										
										public void onFailure(Throwable arg0) {
											Toast.makeText(GroupActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
										};
									});
									
								}
						    }
						});
						AlertDialog alert = builder.create();
						alert.show();
						return false;
					}
		});
	}

	// adapter List
	private List<GroupDochie> getNewsEntries() {
		List<GroupDochie> entries = new ArrayList<GroupDochie>();
		entries = mgroups.getAllMessage();
		return entries;
	}
//	private List<group_dochie> getNewsEntries2(String _namaGrb){
//		List<group_dochie> entries = new ArrayList<group_dochie>();
//		entries.add(new group_dochie(_namaGrb));
//		return entries;
//	}
	
	public void btnHomeAction(View v) {
		startActivity(new Intent(GroupActivity.this, PesanContactActivity.class));
	}
	public void tekanLeft1(View v) {

	}

	public void tekanLeft2(View v) {
		final Dialog dialog = new Dialog(GroupActivity.this);
		dialog.setContentView(R.layout.add_grub_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Add Group");
		//mgroups = new mgroup_dochie(group_dochie_activity.this);
		//mgroups.open();
		final Button tambahGroup = (Button)dialog.findViewById(R.id.btnTambahGroup);
		final EditText tambahNamaGroup = (EditText)dialog.findViewById(R.id.txtGroupAdd);
		final Button cancleGroup = (Button) dialog.findViewById(R.id.btnCancel);
		cancleGroup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        
		tambahGroup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//mgroups.createMessage(tambahNamaGroup.getText().toString());
				final String owner = new DochiePreferenceHelper(GroupActivity.this).getNoHp();
				final String group = tambahNamaGroup.getText().toString();
				String uri = core.messager.dochie.constant.Constants.HOST_JSON+"/addGroup";
				RequestParams params = new RequestParams();
				params.put("group", group);
				params.put("owner", owner);
				
				AsyncHttpClient ahc = new AsyncHttpClient();
				ahc.post(uri,params, new AsyncHttpResponseHandler(){
					@Override
					public void onSuccess(String arg0) {
						try {
							JSONObject job;
							job = new JSONObject(arg0);
							JSONArray arrJob = job.getJSONArray("group");
							for (int i = 0; i < arrJob.length(); i++) {
								JSONObject jarr = arrJob.getJSONObject(i);
								adapter_groups.add(mgroups.createMessage(jarr.getString("id_group"),group));
								adapter_groups.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						super.onSuccess(arg0);
					}
					
					@Override
					public void onFailure(Throwable arg0) {
						Toast.makeText(GroupActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
					};
					
				});
				
				dialog.dismiss();	
			}
		});
		dialog.show();
		
		
	}
	
	@Override
	protected void onResume() {
		mgroups.open();
		super.onResume();
	}
	@Override
	protected void onPause() {
		mgroups.close();
		super.onPause();
	}
	
	
}
