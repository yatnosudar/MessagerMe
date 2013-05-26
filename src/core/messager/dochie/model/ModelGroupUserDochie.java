package core.messager.dochie.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import core.messager.dochie.bean.GroupUserDochie;
import core.messager.dochie.helper.DochieSQLiteHelper;

public class ModelGroupUserDochie {
	private SQLiteDatabase dbGroupUsers;
	private DochieSQLiteHelper dbHelper;
	private String allcolomns[] = { DochieSQLiteHelper.COLOMN_IDUSER,
			DochieSQLiteHelper.COLOMN_IDGROUP };

	public ModelGroupUserDochie(Context context) {
		dbHelper = new DochieSQLiteHelper(context);
	}

	public void open() throws SQLException {
		dbGroupUsers = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void creatUserGroup(long _idUsr, String idGrb){
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_IDUSER, _idUsr);
		values.put(DochieSQLiteHelper.COLOMN_IDGROUP, idGrb);
		dbGroupUsers.insert(DochieSQLiteHelper.TABLE_USERGROUP, null,values);
	}
	
	public GroupUserDochie createMessage(long _idUsr, long _idGrb) {
		ContentValues values = new ContentValues();

		values.put(DochieSQLiteHelper.COLOMN_IDUSER, _idUsr);
		values.put(DochieSQLiteHelper.COLOMN_IDGROUP, _idGrb);
		
		long insertId = dbGroupUsers.insert(DochieSQLiteHelper.TABLE_USERGROUP, null,
				values);
		Cursor cursor = dbGroupUsers.query(DochieSQLiteHelper.TABLE_USERGROUP,
				allcolomns, DochieSQLiteHelper.COLOMN_IDUSERGROUP + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		GroupUserDochie newGroub = cursorToMessage(cursor);
		cursor.close();
		Log.i("Data Masuk", "data tersimpan");
		return newGroub;
	}

	public int updateMessage(long _idUsrGrb,long _idUsr, long _idGrb) {
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_IDUSER, _idUsr);
		values.put(DochieSQLiteHelper.COLOMN_IDGROUP, _idGrb);

		int updateID = dbGroupUsers.update(DochieSQLiteHelper.TABLE_USERGROUP, values,
				DochieSQLiteHelper.COLOMN_IDUSERGROUP + " = " + _idUsrGrb, null);
		return updateID;

	}

	public void deleteUsers(GroupUserDochie mGroups) {
		String id = mGroups.get_idGrb();
		Log.v("delete_data:", "delete id: " + id);
		dbGroupUsers.delete(DochieSQLiteHelper.TABLE_USERGROUP,
				DochieSQLiteHelper.COLOMN_IDUSERGROUP + " = '" + id+"'", null);
	}

	public List<GroupUserDochie> getAllMessage() {
		List<GroupUserDochie> groups = new ArrayList<GroupUserDochie>();

		Cursor cursor = dbGroupUsers.query(DochieSQLiteHelper.TABLE_USERGROUP, allcolomns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GroupUserDochie group = cursorToMessage(cursor);
			groups.add(group);
			cursor.moveToNext();
		}
		cursor.close();
		return groups;
	}
	
	public List<GroupUserDochie> getAllUsersGroup(String _idGrb){
		List<GroupUserDochie> groups = new ArrayList<GroupUserDochie>();
		String sql = "SELECT UG._idUsrGrb,UG._idUsr,UG._idGrb,U._namaUsr,U._nohpUsr,G._namaGrb " +
				" FROM groups G, user U, usergroup UG" +
				" WHERE U._idUsr=UG._idUsr AND UG._idGrb=G._idGrb AND G._idGrb= '"+_idGrb+"'";
		Log.v("sql", sql);
		Cursor cursor=dbGroupUsers.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GroupUserDochie group = cursorToUsersGroup(cursor);
			groups.add(group);
			cursor.moveToNext();
		}
		cursor.close();
		return groups;
		
	}

	private GroupUserDochie cursorToMessage(Cursor cursor) {
		GroupUserDochie _message = new GroupUserDochie();
		_message.set_idUsrGrb(cursor.getLong(0));
		_message.set_idUsr(cursor.getLong(1));
		_message.set_idGrb(cursor.getString(2));
		return _message;
	}
	/*long _idUsrGrb, long _idUsr, long _idGrb,
			String _namaUsr, String _nohpUsr, String _namaGrb*/
	
	private GroupUserDochie cursorToUsersGroup(Cursor cursor) {
		GroupUserDochie _message = new GroupUserDochie();
		_message.set_idUsrGrb(cursor.getLong(0));
		_message.set_idUsr(cursor.getLong(1));
		_message.set_idGrb(cursor.getString(2));
		_message.set_namaUsr(cursor.getString(3));
		_message.set_nohpUsr(cursor.getString(4));
		_message.set_namaGrb(cursor.getString(5));
		return _message;
	}
}
