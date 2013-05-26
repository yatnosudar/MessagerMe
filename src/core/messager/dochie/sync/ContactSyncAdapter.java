package core.messager.dochie.sync;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import core.messager.dochie.UserActivity;
import core.messager.dochie.bean.UserContactDochie;
import core.messager.dochie.bean.UserDochie;
import core.messager.dochie.constant.StaticVariable;
import core.messager.dochie.helper.DochiePreferenceHelper;
import core.messager.dochie.model.UserFunctionJson;
import core.messager.dochie.model.ModelUserDochie;

public class ContactSyncAdapter extends AbstractThreadedSyncAdapter {
	private Context mContext;
	String KEY_SUCCESS = "success";
	DochiePreferenceHelper dph;
	ModelUserDochie dbusr;

	public ContactSyncAdapter(Context context, boolean autoInitialize) {
		
		super(context, true);
		mContext = context;
		dph = new DochiePreferenceHelper(context);
		dbusr = new ModelUserDochie(context);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		sync_on_server();
		
		
	}

	public ArrayList<UserContactDochie> getListContact() {
		ArrayList<UserContactDochie> usrdata = new ArrayList<UserContactDochie>();
		ContentResolver cr = mContext.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						String namePhone = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						String lnumber = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

						String regexReplace = lnumber.replaceAll("[-+]", "");
						String fixcontact = regexReplace.replaceAll("^0", "62");

						dbusr.open();
						if (dbusr.getTotIdUser(fixcontact) > 0) {
							Log.d(ContactSyncAdapter.class + "", fixcontact);
						} else {
							Log.d(ContactSyncAdapter.class + "", "add to list "+ fixcontact);
							UserContactDochie usr_contact = new UserContactDochie();
							usr_contact.setNama(namePhone);
							usr_contact.setPhone(fixcontact);
							usrdata.add(usr_contact);

						}
						dbusr.close();
					}
					pCur.close();

				}
			}
		}
		return usrdata;
	}

	public void sync_on_server() {

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(core.messager.dochie.constant.Constants.HOST_JSON, null,
				new JsonHttpResponseHandler() {
			
					
					@Override
					public void onSuccess(JSONObject arg0) {
						ArrayList<UserContactDochie> userContact = getListContact();
						boolean foundSwitch = false;
						try {
							JSONArray jsonMainArr = arg0
									.getJSONArray("contact");
							for (int i = 0; i < userContact.size(); i++) {

								for (int j = 0; j < jsonMainArr.length(); j++) {
									String emailContact = userContact.get(i)
											.getPhone()
											+ "@"
											+ core.messager.dochie.constant.Constants.HOST_EMAIL;
									String emailJSON = jsonMainArr
											.getJSONObject(j).get("email")
											.toString();
									if (emailContact.endsWith(emailJSON)) {
										foundSwitch = true;
										dbusr.open();
										UserDochie dataUser = dbusr
												.createMessage(
														userContact.get(i)
																.getNama(),
														emailContact,
														userContact.get(i)
																.getPhone(),
														null, 1);
										core.messager.dochie.constant.StaticVariable.setUc(dataUser);
										Intent iz = new Intent("android.intent.action.MAIN")
												.putExtra("contact","contact");
										mContext.sendBroadcast(iz);
										
										UserFunctionJson addContact = new UserFunctionJson();

										addContact.add_contact(
												new DochiePreferenceHelper(
														mContext).getNoHp(),
												emailContact, userContact
														.get(i).getNama());

										cekMessageBroadcast(true, dataUser);
										dbusr.close();
									}
								}
								if (foundSwitch == false) {
									Log.d(ContactSyncAdapter.this + "",
											"email tidak ditemukan");
								}
								foundSwitch = false;
							}
						} catch (JSONException e) {
							Log.e(ContactSyncAdapter.this + "", "error sync : "
									+ e.getMessage());
						}
					}
				});
	}

	public void cekMessageBroadcast(boolean data, UserDochie usr) {
		if (data) {
			UserActivity.adapter_users.add(usr);
		}
		data = false;
	}

}