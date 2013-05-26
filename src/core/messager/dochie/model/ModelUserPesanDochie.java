package core.messager.dochie.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import core.messager.dochie.bean.GroupPesanDochie;
import core.messager.dochie.bean.UserPesanDochie;
import core.messager.dochie.helper.DochieSQLiteHelper;

/*long _idUsr, String _isiPsn, String _filePsn,
 String _timePsn, int _isSend, int _isMe, long _idGrb,
 String _namaUsr, String _emailUsr, String _nohpUsr*/

public class ModelUserPesanDochie {
	private SQLiteDatabase dbUserPesan;
	private DochieSQLiteHelper dbHelper;

	public ModelUserPesanDochie(Context context) {
		dbHelper = new DochieSQLiteHelper(context);
	}

	public void open() throws SQLException {
		dbUserPesan = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public List<UserPesanDochie> getAllMessage() {
		List<UserPesanDochie> pesans = new ArrayList<UserPesanDochie>();

		String sql = "SELECT " + "P."
				+ DochieSQLiteHelper.COLOMN_IDPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_IDUSER
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISIPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_FILEPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_TIMEPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISSEND
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISME
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_IDGROUP
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISOPEN
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_NAMAUSER
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_EMAILUSER
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_NOHPUSER
				+ " FROM user U, pesan P WHERE U._idUsr = P._idUsr AND P._idGrb = 0 AND P."
				+ DochieSQLiteHelper.COLOMN_ISME + "= 0 group by U."
				+ DochieSQLiteHelper.COLOMN_NOHPUSER
				+ " order by P._isOpen desc";

		Cursor cursor = dbUserPesan.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserPesanDochie pesan = cursorToMessage(cursor);
			pesans.add(pesan);
			cursor.moveToNext();
		}
		cursor.close();
		return pesans;
	}

	public int updateMessageStatus(long _idPesan,int _isSend){
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_ISSEND, _isSend);
		int updateID = dbUserPesan.update(DochieSQLiteHelper.TABLE_PESAN, values,
				DochieSQLiteHelper.COLOMN_IDPESAN + " = " + _idPesan, null);
		return updateID;
	}
	
	public List<UserPesanDochie> getAllMessageForSend() {
		List<UserPesanDochie> pesans = new ArrayList<UserPesanDochie>();

		String sql = "SELECT " + "P."
				+ DochieSQLiteHelper.COLOMN_IDPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_IDUSER
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISIPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_FILEPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_TIMEPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISSEND
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISME
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_IDGROUP
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISOPEN
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_NAMAUSER
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_EMAILUSER
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_NOHPUSER
				+ " FROM user U, pesan P WHERE U._idUsr = P._idUsr AND P._idGrb = 0 AND P."
				+ DochieSQLiteHelper.COLOMN_ISME + "= 1 AND P."+DochieSQLiteHelper.COLOMN_ISSEND+"= 0";

		Cursor cursor = dbUserPesan.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserPesanDochie pesan = cursorToMessage(cursor);
			pesans.add(pesan);
			cursor.moveToNext();
		}
		cursor.close();
		return pesans;
	}

	public List<UserPesanDochie> getDataMessage() {
		List<UserPesanDochie> pesans = new ArrayList<UserPesanDochie>();
		String sql = "SELECT * " + "P."
				+ DochieSQLiteHelper.COLOMN_IDPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_IDUSER
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISIPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_FILEPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_TIMEPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISSEND
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISME
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_IDGROUP
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_NAMAUSER
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_EMAILUSER
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_NOHPUSER
				+ "FROM user U, pesan P WHERE U._idUsr = P._idUsr AND P._idGrb = 0 group by U._idUsr";

		Cursor cursor = dbUserPesan.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserPesanDochie pesan = cursorToMessage(cursor);
			pesans.add(pesan);
			cursor.moveToNext();
		}
		cursor.close();
		return pesans;
	}

	public List<GroupPesanDochie> getAllMessageGrb(long _idGrb) {
		List<GroupPesanDochie> pesans = new ArrayList<GroupPesanDochie>();

		String sql = "SELECT * " + "P."
				+ DochieSQLiteHelper.COLOMN_IDPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_IDUSER
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISIPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_FILEPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_TIMEPESAN
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISSEND
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_ISME
				+ ",P."
				+ DochieSQLiteHelper.COLOMN_IDGROUP
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_NAMAUSER
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_EMAILUSER
				+ ",U."
				+ DochieSQLiteHelper.COLOMN_NOHPUSER
				+ ",G."
				+ DochieSQLiteHelper.COLOMN_NOHPUSER
				+ "FROM user U, pesan P, group G WHERE U._idUsr = P._idUsr AND P._idGrb = G._idGrb AND P._idGrb="
				+ _idGrb;

		Cursor cursor = dbUserPesan.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GroupPesanDochie pesan = cursorToMessageGrb(cursor);
			pesans.add(pesan);
			cursor.moveToNext();
		}
		cursor.close();
		return pesans;
	}

	public int updateDataPesan(long _idUser) {
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_ISOPEN, 0);
		int update = dbUserPesan.update(DochieSQLiteHelper.TABLE_PESAN, values,
				DochieSQLiteHelper.COLOMN_IDUSER + " = " + _idUser, null);
		return update;
	}

	/*
	 * long _idUsr, String _isiPsn, String _filePsn, String _timePsn, int
	 * _isSend, int _isMe, long _idGrb, String _namaUsr, String _emailUsr,
	 * String _nohpUsr
	 */

	private UserPesanDochie cursorToMessage(Cursor cursor) {
		UserPesanDochie _message = new UserPesanDochie();
		_message.set_idPsn(cursor.getLong(0));
		_message.set_idUsr(cursor.getLong(1));
		_message.set_isiPsn(cursor.getString(2));
		_message.set_filePsn(cursor.getString(3));
		_message.set_timePsn(cursor.getString(4));
		_message.set_isSend(cursor.getInt(5));
		_message.set_isMe(cursor.getInt(6));
		_message.set_idGrb(cursor.getLong(7));
		_message.set_isOpen(cursor.getInt(8));
		_message.set_namaUsr(cursor.getString(9));
		_message.set_emailUsr(cursor.getString(10));
		_message.set_nohpUsr(cursor.getString(11));
		return _message;
	}

	private GroupPesanDochie cursorToMessageGrb(Cursor cursor) {
		GroupPesanDochie _message = new GroupPesanDochie();
		_message.set_idPsn(cursor.getLong(0));
		_message.set_idUsr(cursor.getLong(1));
		_message.set_isiPsn(cursor.getString(2));
		_message.set_filePsn(cursor.getString(3));
		_message.set_timePsn(cursor.getString(4));
		_message.set_isSend(cursor.getInt(5));
		_message.set_isMe(cursor.getInt(6));
		_message.set_idGrb(cursor.getLong(7));
		_message.set_namaUsr(cursor.getString(8));
		_message.set_emailUsr(cursor.getString(9));
		_message.set_nohpUsr(cursor.getString(10));
		_message.set_namaGrb(cursor.getString(11));

		return _message;
	}
}
