package core.messager.dochie.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DochieSQLiteHelper extends SQLiteOpenHelper{

	public static final String DATABASE_NAME = "database.db";
	private static final int DATABASE_VERSION = 1;
	//user
	public static final String TABLE_USER = "user";
	public static final String COLOMN_IDUSER = "_idUsr";
	public static final String COLOMN_NAMAUSER = "_namaUsr";
	public static final String COLOMN_EMAILUSER = "_emailUsr";
	public static final String COLOMN_NOHPUSER = "_nohpUsr";
	public static final String COLOMN_SINUSER = "_sinUsr";
	public static final String COLOMN_ISDOCHIE = "_isdochie";
	
	//group
	public static final String TABLE_GROUP = "groups";
	public static final String COLOMN_IDGROUP = "_idGrb";
	public static final String COLOMN_NAMAGROUP = "_namaGrb";
	
	//pesan
	public static final String TABLE_PESAN = "pesan";
	public static final String COLOMN_IDPESAN = "_idPsn";
	public static final String COLOMN_ISIPESAN = "_isiPsn";
	public static final String COLOMN_FILEPESAN = "_filePsn";
	public static final String COLOMN_TIMEPESAN = "_timePsn";
	public static final String COLOMN_ISSEND = "_isSend";
	public static final String COLOMN_ISME = "_isMe";
	public static final String COLOMN_ISOPEN = "_isOpen";
	
	//usergroup
	public static final String TABLE_USERGROUP = "usergroup";
	public static final String COLOMN_IDUSERGROUP = "_idUsrGrb";
	
	//create table user
	private static final String DATABASE_CREATE_USER = "create table "
			+TABLE_USER+" ("
			+COLOMN_IDUSER +" integer primary key autoincrement, " 
			+COLOMN_NAMAUSER + " text, "
			+COLOMN_EMAILUSER + " text, "
			+COLOMN_NOHPUSER + " text,"
			+COLOMN_SINUSER + " text,"
			+COLOMN_ISDOCHIE +" integer);";
	
	//create table pesan	
	private static final String DATABASE_CREATE_PESAN = "create table "
			+TABLE_PESAN+" ("
			+COLOMN_IDPESAN +" integer primary key autoincrement, " 
			+COLOMN_IDUSER + " integer, "
			+COLOMN_ISIPESAN + " text, "
			+COLOMN_FILEPESAN + " text, "
			+COLOMN_TIMEPESAN + " text, "
			+COLOMN_ISSEND +" integer, "
			+COLOMN_ISME +" integer, "
			+COLOMN_ISOPEN +" integer,"
			+COLOMN_IDGROUP +" integer );";
	
	//create table group
	private static final String DATABASE_CREATE_GROUP = "create table " +
			TABLE_GROUP+"(" +
			COLOMN_IDGROUP	+" text primary key, "+
			COLOMN_NAMAGROUP+" text );";
	
	//create table user_group
	private static final String DATABASE_CREATE_USERGROUP = "create table " +
			TABLE_USERGROUP+"(" +
			COLOMN_IDUSERGROUP	+" integer primary key autoincrement, "+
			COLOMN_IDUSER+" integer, " +
			COLOMN_IDGROUP+" text );";
	
	
	public DochieSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_USER);
		db.execSQL(DATABASE_CREATE_GROUP);
		db.execSQL(DATABASE_CREATE_PESAN);
		db.execSQL(DATABASE_CREATE_USERGROUP);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PESAN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERGROUP);
	}

}
