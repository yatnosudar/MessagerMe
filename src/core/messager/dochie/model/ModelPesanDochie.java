package core.messager.dochie.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import core.messager.dochie.bean.PesanDochie;
import core.messager.dochie.helper.DochieSQLiteHelper;

public class ModelPesanDochie {
	private SQLiteDatabase dbPesan;
	private DochieSQLiteHelper dbHelper;
	private String allcolomns[] = { DochieSQLiteHelper.COLOMN_IDPESAN,
			DochieSQLiteHelper.COLOMN_IDUSER,
			DochieSQLiteHelper.COLOMN_ISIPESAN,
			DochieSQLiteHelper.COLOMN_FILEPESAN,
			DochieSQLiteHelper.COLOMN_TIMEPESAN,
			DochieSQLiteHelper.COLOMN_ISSEND, DochieSQLiteHelper.COLOMN_ISME,DochieSQLiteHelper.COLOMN_ISOPEN };

	
	public ModelPesanDochie(Context context) {
		dbHelper = new DochieSQLiteHelper(context);
	}

	public void open() throws SQLException {

		dbPesan = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public String getIdUser(String _nohpUsr) {
		String _idUsr = "";
		String sql = "SELECT * FROM " + DochieSQLiteHelper.TABLE_USER
				+ " WHERE " + DochieSQLiteHelper.COLOMN_NOHPUSER + " = '"
				+ _nohpUsr + "'";

		Cursor c = dbPesan.rawQuery(sql, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			Log.i("id user", c.getString(0));
			_idUsr = c.getString(0);
			break;
		}
		c.close();
		return _idUsr;
	}
	
	public boolean isUser(String _nohpUsr) {
		String sql = "SELECT * FROM " + DochieSQLiteHelper.TABLE_USER
				+ " WHERE " + DochieSQLiteHelper.COLOMN_NOHPUSER + " = '"
				+ _nohpUsr + "'";

		Cursor c = dbPesan.rawQuery(sql, null);	
		
		if (c.getCount()>0) {
			return true;
		}
		c.close();
		return false;
	}

	public PesanDochie createMessage2(String _nohp, String _isiPsn,
			String _filePsn, String _timePsn, int _isSend, int _isMe,
			long _idGrb,long _isOpen) {
		
		long _idUsr = Long.parseLong(getIdUser(_nohp));
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_IDUSER, _idUsr);
		values.put(DochieSQLiteHelper.COLOMN_ISIPESAN, _isiPsn);
		values.put(DochieSQLiteHelper.COLOMN_FILEPESAN, _filePsn);
		values.put(DochieSQLiteHelper.COLOMN_TIMEPESAN, _timePsn);
		values.put(DochieSQLiteHelper.COLOMN_ISSEND, _isSend);
		values.put(DochieSQLiteHelper.COLOMN_ISME, _isMe);
		values.put(DochieSQLiteHelper.COLOMN_IDGROUP, _idGrb);
		values.put(DochieSQLiteHelper.COLOMN_ISOPEN, _isOpen);

		long insertId = dbPesan.insert(DochieSQLiteHelper.TABLE_PESAN, null,
				values);
		Cursor cursor = dbPesan.query(DochieSQLiteHelper.TABLE_PESAN,
				allcolomns, DochieSQLiteHelper.COLOMN_IDPESAN + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		PesanDochie newGroub = cursorToMessage(cursor);
		cursor.close();
		return newGroub;
	}

	public PesanDochie createMessage(long _idUsr, String _isiPsn,
			String _filePsn, String _timePsn, int _isSend, int _isMe,
			long _idGrb) {
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_IDUSER, _idUsr);
		values.put(DochieSQLiteHelper.COLOMN_ISIPESAN, _isiPsn);
		values.put(DochieSQLiteHelper.COLOMN_FILEPESAN, _filePsn);
		values.put(DochieSQLiteHelper.COLOMN_TIMEPESAN, _timePsn);
		values.put(DochieSQLiteHelper.COLOMN_ISSEND, _isSend);
		values.put(DochieSQLiteHelper.COLOMN_ISME, _isMe);
		values.put(DochieSQLiteHelper.COLOMN_IDGROUP, _idGrb);
		values.put(DochieSQLiteHelper.COLOMN_ISOPEN,1);

		long insertId = dbPesan.insert(DochieSQLiteHelper.TABLE_PESAN, null,
				values);
		Cursor cursor = dbPesan.query(DochieSQLiteHelper.TABLE_PESAN,
				allcolomns, DochieSQLiteHelper.COLOMN_IDPESAN + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		PesanDochie newGroub = cursorToMessage(cursor);
		cursor.close();
		return newGroub;
	}

	public int updateMessage(long _idPsn, long _idUsr, String _isiPsn,
			String _filePsn, String _timePsn, int _isSend, int _isMe,
			long _idGrb,long _isOpen) {
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_IDUSER, _idUsr);
		values.put(DochieSQLiteHelper.COLOMN_ISIPESAN, _isiPsn);
		values.put(DochieSQLiteHelper.COLOMN_FILEPESAN, _filePsn);
		values.put(DochieSQLiteHelper.COLOMN_TIMEPESAN, _timePsn);
		values.put(DochieSQLiteHelper.COLOMN_ISSEND, _isSend);
		values.put(DochieSQLiteHelper.COLOMN_ISME, _isMe);
		values.put(DochieSQLiteHelper.COLOMN_IDGROUP, _idGrb);
		values.put(DochieSQLiteHelper.COLOMN_IDGROUP, _isOpen);
		int updateID = dbPesan.update(DochieSQLiteHelper.TABLE_PESAN, values,
				DochieSQLiteHelper.COLOMN_IDPESAN + " = " + _idPsn, null);
		return updateID;

	}
	public int updateMessageStatus(long _idPesan,int _isSend){
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_ISSEND, _isSend);
		int updateID = dbPesan.update(DochieSQLiteHelper.TABLE_PESAN, values,
				DochieSQLiteHelper.COLOMN_IDPESAN + " = " + _idPesan, null);
		return updateID;
	}

	public void deleteUsers(PesanDochie mPesans) {
		long id = mPesans.get_idPsn();
		Log.v("delete_data:", "delete id: " + id);
		dbPesan.delete(DochieSQLiteHelper.TABLE_PESAN,
				DochieSQLiteHelper.COLOMN_IDPESAN + " = " + id, null);
	}

	public List<PesanDochie> getAllMessage(int _idUsr) {
		List<PesanDochie> pesans = new ArrayList<PesanDochie>();

		String sql = "select * from pesan where _idGrb=0 and _idUsr = "+_idUsr;
//		Cursor cursor = dbPesan.query(
//				DochieSQLiteHelper.TABLE_PESAN,
//				allcolomns, 
//				DochieSQLiteHelper.COLOMN_IDGROUP + " = " + 0
//						+ " and " + 
//				DochieSQLiteHelper.COLOMN_IDUSER + " = "+ _idUsr, 
//				null, null, null, null);
		Cursor cursor = dbPesan.rawQuery(sql, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PesanDochie pesan = cursorToMessage(cursor);
			pesans.add(pesan);
			cursor.moveToNext();
		}
		
		cursor.close();
		return pesans;
	}
	
	public List<PesanDochie> getAllMessage2(int _idUsr) {
		List<PesanDochie> pesans = new ArrayList<PesanDochie>();

		String sql = "select * from pesan where _idGrb=0 and _idUsr = "+_idUsr;
//		Cursor cursor = dbPesan.query(
//				DochieSQLiteHelper.TABLE_PESAN,
//				allcolomns, 
//				DochieSQLiteHelper.COLOMN_IDGROUP + " = " + 0
//						+ " and " + 
//				DochieSQLiteHelper.COLOMN_IDUSER + " = "+ _idUsr, 
//				null, null, null, null);
		Cursor cursor = dbPesan.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PesanDochie pesan = cursorToMessage(cursor);
			pesans.add(pesan);
			cursor.moveToNext();
		}
		cursor.close();
		return pesans;
	}
	
	public List<PesanDochie> getAllMessagePending(){
		List<PesanDochie> pesans = new ArrayList<PesanDochie>();
		String sql = "select * from pesan where _isSend = "+1;
		Cursor cursor = dbPesan.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PesanDochie pesan = cursorToMessage(cursor);
			pesans.add(pesan);
			cursor.moveToNext();
		}
		cursor.close();
		return pesans;
	}
	
	public List<PesanDochie> getAllMessageGroub(long _idGrb) {
		List<PesanDochie> pesans = new ArrayList<PesanDochie>();

		Cursor cursor = dbPesan.query(DochieSQLiteHelper.TABLE_PESAN,
				allcolomns, DochieSQLiteHelper.COLOMN_IDGROUP + " = " + _idGrb,
				null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PesanDochie pesan = cursorToMessageGroup(cursor);
			pesans.add(pesan);
			cursor.moveToNext();
		}
		cursor.close();
		return pesans;
	}
	private PesanDochie cursorToMessage(Cursor cursor) {
		PesanDochie _message = new PesanDochie();
		_message.set_idPsn(cursor.getLong(0));
		_message.set_idUser(cursor.getLong(1));
		_message.set_isiPsn(cursor.getString(2));
		_message.set_filePsn(cursor.getString(3));
		_message.set_timePsn(cursor.getString(4));
		_message.set_isSend(cursor.getInt(5));
		_message.set_isMe(cursor.getInt(6));
		_message.set_isOpen(cursor.getInt(7));
		return _message;
	}

	private PesanDochie cursorToMessageGroup(Cursor cursor) {
		PesanDochie _message = new PesanDochie();
		_message.set_idPsn(cursor.getLong(0));
		_message.set_idUser(cursor.getLong(1));
		_message.set_isiPsn(cursor.getString(2));
		_message.set_filePsn(cursor.getString(3));
		_message.set_timePsn(cursor.getString(4));
		_message.set_isSend(cursor.getInt(5));
		_message.set_isMe(cursor.getInt(6));
		_message.set_idGrb(cursor.getLong(7));
		_message.set_isOpen(cursor.getInt(8));
		return _message;
	}
}
