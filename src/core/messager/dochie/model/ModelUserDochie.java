package core.messager.dochie.model;

import java.util.ArrayList;
import java.util.List;

import core.messager.dochie.bean.UserDochie;
import core.messager.dochie.helper.DochieSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ModelUserDochie {
	private SQLiteDatabase dbUser;
	private DochieSQLiteHelper dbHelper;
	private String allcolomns[] = { DochieSQLiteHelper.COLOMN_IDUSER,
			DochieSQLiteHelper.COLOMN_NAMAUSER,
			DochieSQLiteHelper.COLOMN_EMAILUSER,
			DochieSQLiteHelper.COLOMN_NOHPUSER,
			DochieSQLiteHelper.COLOMN_SINUSER,
			DochieSQLiteHelper.COLOMN_ISDOCHIE };

	public ModelUserDochie(Context context) {
		dbHelper = new DochieSQLiteHelper(context);
	}

	public void open() throws SQLException {
		dbUser = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public String getIdUser(String _nohpUsr) {
		String _idUsr = "";
		String sql = "SELECT * FROM "
				+ DochieSQLiteHelper.TABLE_USER + " WHERE "
				+ DochieSQLiteHelper.COLOMN_NOHPUSER + " LIKE '" + _nohpUsr+"'";
		
		Cursor c = dbUser.rawQuery(sql, null);
		c.moveToFirst();
		
		while (!c.isAfterLast()) {
			Log.i("id user", c.getString(0));
			_idUsr = c.getString(0);
			break;
		}
		
		c.close();
		return _idUsr;
	}
	
	public int getTotIdUser(String _nohpUsr) {
		String sql = "SELECT * FROM "
				+ DochieSQLiteHelper.TABLE_USER + " WHERE "
				+ DochieSQLiteHelper.COLOMN_NOHPUSER + " = '" + _nohpUsr+"'";
		
		Cursor c = dbUser.rawQuery(sql, null);
		c.moveToFirst();
		int _idUsr=c.getCount();
		c.close();
		return _idUsr;
	}
	public UserDochie createMessage(String _namaUsr, String _emailUsr,
			String _nohpUsr, String _sinUsr, int _isdochie) {
		
		ContentValues values = new ContentValues();

		values.put(DochieSQLiteHelper.COLOMN_NAMAUSER, _namaUsr);
		values.put(DochieSQLiteHelper.COLOMN_EMAILUSER, _emailUsr);
		values.put(DochieSQLiteHelper.COLOMN_NOHPUSER, _nohpUsr);
		values.put(DochieSQLiteHelper.COLOMN_SINUSER, _sinUsr);
		values.put(DochieSQLiteHelper.COLOMN_ISDOCHIE, _isdochie);

		long insertId = dbUser.insert(DochieSQLiteHelper.TABLE_USER, null,
				values);
		Cursor cursor = dbUser.query(DochieSQLiteHelper.TABLE_USER, allcolomns,
				DochieSQLiteHelper.COLOMN_IDUSER + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		UserDochie newUser = cursorToMessage(cursor);
		cursor.close();
		return newUser;
	}

	public int updateMessage(String _idUsr, String _namaUsr, String _emailUsr,
			String _nohpUsr, String _sinUsr, int _isdochie) {
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_NAMAUSER, _namaUsr);
		values.put(DochieSQLiteHelper.COLOMN_EMAILUSER, _emailUsr);
		values.put(DochieSQLiteHelper.COLOMN_NOHPUSER, _nohpUsr);
		values.put(DochieSQLiteHelper.COLOMN_SINUSER, _sinUsr);
		values.put(DochieSQLiteHelper.COLOMN_ISDOCHIE, _isdochie);

		int updateID = dbUser.update(DochieSQLiteHelper.TABLE_USER, values,
				DochieSQLiteHelper.COLOMN_IDUSER + " = " + _idUsr, null);
		return updateID;

	}

	public void deleteUsers(UserDochie mUsers) {
		long id = mUsers.get_idUsr();
		Log.v("delete_data:", "delete id: " + id);
		dbUser.delete(DochieSQLiteHelper.TABLE_USER,
				DochieSQLiteHelper.COLOMN_IDUSER + " = " + id, null);
	}

	public List<UserDochie> getAllMessage() {
		List<UserDochie> users = new ArrayList<UserDochie>();

		Cursor cursor = dbUser.query(DochieSQLiteHelper.TABLE_USER, allcolomns,
				DochieSQLiteHelper.COLOMN_ISDOCHIE + " = " + 1, null, null,
				null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserDochie user = cursorToMessage(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}

	public List<UserDochie> getOneUser(String nama) {
		List<UserDochie> users = new ArrayList<UserDochie>();
		String sql = "SELECT * FROM "+DochieSQLiteHelper.TABLE_USER+" WHERE "+DochieSQLiteHelper.COLOMN_NAMAUSER+" = "+nama;
		Cursor cursor = dbUser.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserDochie user = cursorToMessage(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}

	public List<UserDochie> getAllMessage2() {
		List<UserDochie> users = new ArrayList<UserDochie>();

		Cursor cursor = dbUser.query(DochieSQLiteHelper.TABLE_USER, allcolomns,
				null, null, null,
				null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserDochie user = cursorToMessage(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}
	
	public int updateMessageIsDochie(String _emailUsr) {
		String _idUsr = getIdUser(_emailUsr);
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_ISDOCHIE, 1);
		int updateID = dbUser.update(DochieSQLiteHelper.TABLE_USER, values,
				DochieSQLiteHelper.COLOMN_IDUSER + " = " + _idUsr, null);
		return updateID;
	}
	private UserDochie cursorToMessage(Cursor cursor) {
		UserDochie _message = new UserDochie();
		_message.set_idUsr(cursor.getLong(0));
		_message.set_namaUsr(cursor.getString(1));
		_message.set_emailUsr(cursor.getString(2));
		_message.set_nohpUsr(cursor.getString(3));
		_message.set_sinUsr(cursor.getString(4));
		_message.set_isdochie(cursor.getInt(5));
		return _message;
	}
}
