package core.messager.dochie.model;

import java.util.ArrayList;
import java.util.List;

import core.messager.dochie.bean.GroupDochie;
import core.messager.dochie.helper.DochieSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ModelGroupDochie {
	private SQLiteDatabase dbGroup;
	private DochieSQLiteHelper dbHelper;
	private String allcolomns[] = { DochieSQLiteHelper.COLOMN_IDGROUP,
			DochieSQLiteHelper.COLOMN_NAMAGROUP };

	public ModelGroupDochie(Context context) {
		dbHelper = new DochieSQLiteHelper(context);
	}

	public void open() throws SQLException {
		dbGroup = dbHelper.getWritableDatabase();

	}

	public void close() {
		dbHelper.close();
	}

	public GroupDochie createMessage(String _idGrb, String _namaGrb) {
		ContentValues values = new ContentValues();

		values.put(DochieSQLiteHelper.COLOMN_IDGROUP, _idGrb);
		values.put(DochieSQLiteHelper.COLOMN_NAMAGROUP, _namaGrb);

		dbGroup.insert(DochieSQLiteHelper.TABLE_GROUP, null, values);
		Cursor cursor = dbGroup.query(DochieSQLiteHelper.TABLE_GROUP,
				allcolomns, DochieSQLiteHelper.COLOMN_IDGROUP + " = '"
						+ _idGrb+"'", null, null, null, null);

		cursor.moveToFirst();
		GroupDochie newGroub = cursorToMessage(cursor);
		cursor.close();
		return newGroub;
	}

	public GroupDochie updateGroup(String _idGrb, String _namaGrb) {
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_NAMAGROUP, _namaGrb);

		dbGroup.update(DochieSQLiteHelper.TABLE_GROUP, values,
				DochieSQLiteHelper.COLOMN_IDGROUP + " = '" + _idGrb+"'", null);

		Cursor cursor = dbGroup.query(DochieSQLiteHelper.TABLE_GROUP,
				allcolomns, DochieSQLiteHelper.COLOMN_IDGROUP + " = '"
						+ _idGrb+"'", null, null, null, null);

		cursor.moveToFirst();
		GroupDochie newGroub = cursorToMessage(cursor);
		cursor.close();
		return newGroub;
	}

	public int updateMessage(String _idGrb, String _namaGrb) {
		ContentValues values = new ContentValues();
		values.put(DochieSQLiteHelper.COLOMN_NAMAGROUP, _namaGrb);

		int updateID = dbGroup.update(DochieSQLiteHelper.TABLE_GROUP, values,
				DochieSQLiteHelper.COLOMN_IDGROUP + " = " + _idGrb, null);
		return updateID;

	}

	public void deleteUsers(GroupDochie mGroups) {
		String id = mGroups.get_idGrb();
		Log.v("delete_data:", "delete id: " + id);
		dbGroup.delete(DochieSQLiteHelper.TABLE_GROUP,
				DochieSQLiteHelper.COLOMN_IDGROUP + " = '" + id+"'", null);
	}

	public List<GroupDochie> getAllMessage() {
		List<GroupDochie> groups = new ArrayList<GroupDochie>();

		Cursor cursor = dbGroup.query(DochieSQLiteHelper.TABLE_GROUP,
				allcolomns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GroupDochie group = cursorToMessage(cursor);
			groups.add(group);
			cursor.moveToNext();
		}
		cursor.close();
		return groups;
	}

	private GroupDochie cursorToMessage(Cursor cursor) {
		GroupDochie _message = new GroupDochie();
		_message.set_idGrb(cursor.getString(0));
		_message.set_namaGrb(cursor.getString(1));
		return _message;
	}
}
