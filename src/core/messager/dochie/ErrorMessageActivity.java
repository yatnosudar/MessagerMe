package core.messager.dochie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/** Class ini digunakan untuk menangani error pada saat proses pengecekan user account dan pada saat proses registrasi.
 *  Class <b>ErrorMessageActivity</b> extends dari class Activity
 * 
 * @author Yatno Sudar
 * @version Dochie Messager 0.5 Build 7
 * */
public class ErrorMessageActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_dochie);
		Bundle b = getIntent().getExtras();
		String errorMessage = b.getString("err_message");
		errorText().setText(errorMessage);
	}
	
	/** Method <code>btnErrorMessage()</code> digunakan untuk kembali ke <b>Dochie activity</b> dari class 
	 * <b>ErrorMessageActivity</b>.
	 *  Method <code>btnErrorMessage()</code> memanggil method
 	 * <code>startActivity(new Intent(ErrorMessageActivity.this, Dochie.class));</code>
	 * Dilanjutkan memanggil method <code>finish()</code> untuk mendestroy class ErrorMessageActivity.
	 * 
	 * @param View  class view berasal dari library android
	 * 
	 * */
	public void btnErrorMessage(View v){
		startActivity(new Intent(ErrorMessageActivity.this, Dochie.class));
		finish();
	}
	/** Method <code>errorText</code> merupakan return value dari class TextView
	 * <code>return (TextView) findViewById(R.id.errorText);</code>
	 * */
	public TextView errorText(){
		return (TextView) findViewById(R.id.errorText);
	}
}
