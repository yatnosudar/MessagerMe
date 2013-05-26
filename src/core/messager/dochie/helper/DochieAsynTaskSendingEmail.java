package core.messager.dochie.helper;

import java.util.List;

import core.messager.dochie.bean.PesanDochie;
import core.messager.dochie.bean.UserPesanDochie;
import core.messager.dochie.helper.callback.Abstract_DochieSendEmail;
import core.messager.dochie.helper.callback.Callback_DochieSendEmail;
import core.messager.dochie.model.ModelPesanDochie;
import core.messager.dochie.model.ModelUserPesanDochie;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Class yang berfungsi melakukan sending email setelah koneksi dalam keadaan
 * terputus. Class ini extends dari class AsynTask. Class ini dipanggil jika koneksi baru tersambung dengan server.
 * 
 * @author Yatno Sudar
 * 
 */
public class DochieAsynTaskSendingEmail extends
		AsyncTask<String, Boolean, String> {

	Context mContext;
	ModelUserPesanDochie mpd;
	Callback_DochieSendEmail dse;
	DochiePreferenceHelper dph;

	public DochieAsynTaskSendingEmail(Context mContext) {
		Log.i("TaskSendingEmail", "Run");
		this.mContext = mContext;
		mpd = new ModelUserPesanDochie(mContext);
		dse = new Callback_DochieSendEmail(mContext);
		dph = new DochiePreferenceHelper(mContext);
	}

	@Override
	protected String doInBackground(String... params) {
		mpd.open();
		List<UserPesanDochie> lpd = mpd.getAllMessageForSend();
		for (int i = 0; i < lpd.size(); i++) {
			final UserPesanDochie p = lpd.get(i);

			dse.kirimEmailAbstract(
					p.get_emailUsr(),
					dph.getNoHp() + "|" + p.get_isiPsn() + "|"
							+ p.get_timePsn(), new Abstract_DochieSendEmail() {
						@Override
						public void s_sendEmail(String toMessage,
								String bodyMessage) {
							mpd.updateMessageStatus(p.get_idPsn(), 1);
							super.s_sendEmail(toMessage, bodyMessage);
						}

						@Override
						public void b_sendEmail(String toMessage,
								String bodyMessage, String e) {
							Log.e("Error Sending Email", toMessage + " and "
									+ bodyMessage + " : " + e);
							super.b_sendEmail(toMessage, bodyMessage, e);
						}

						@Override
						public void f_sendEmail(String toMessage,
								String bodyMessage) {
							Log.e("Failure Sending Email", toMessage + " and "
									+ bodyMessage);
							super.f_sendEmail(toMessage, bodyMessage);
						}
					});
		}
		mpd.close();
		return null;
	}

}
