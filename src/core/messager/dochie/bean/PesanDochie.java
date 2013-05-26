package core.messager.dochie.bean;

public class PesanDochie {

	private long _idPsn;
	private long _idUsr;
	private String _isiPsn;
	private String _filePsn;
	private String _timePsn;
	private int _isSend;
	private int _isMe;
	private long _idGrb;
	private long _isOpen;
	
	public PesanDochie() {
		super();
	}

	public PesanDochie(long _idUsr, String _isiPsn, String _filePsn,
			String _timePsn, int _isSend, int _isMe) {
		super();
		this._idUsr = _idUsr;
		this._isiPsn = _isiPsn;
		this._filePsn = _filePsn;
		this._timePsn = _timePsn;
		this._isSend = _isSend;
		this._isMe = _isMe;
	}

	public PesanDochie(long _idPsn, long _idUsr, String _isiPsn,
			String _filePsn, String _timePsn, int _isSend, int _isMe) {
		super();
		this._idPsn = _idPsn;
		this._idUsr = _idUsr;
		this._isiPsn = _isiPsn;
		this._filePsn = _filePsn;
		this._timePsn = _timePsn;
		this._isSend = _isSend;
		this._isMe = _isMe;
	}

	public PesanDochie(long _idUsr, String _isiPsn, String _filePsn,
			String _timePsn, int _isSend, int _isMe, long _idGrb) {
		super();
		this._idUsr = _idUsr;
		this._isiPsn = _isiPsn;
		this._filePsn = _filePsn;
		this._timePsn = _timePsn;
		this._isSend = _isSend;
		this._isMe = _isMe;
		this._idGrb = _idGrb;
	}

	public PesanDochie(long _idPsn, long _idUsr, String _isiPsn,
			String _filePsn, String _timePsn, int _isSend, int _isMe,
			long _idGrb) {
		super();
		this._idPsn = _idPsn;
		this._idUsr = _idUsr;
		this._isiPsn = _isiPsn;
		this._filePsn = _filePsn;
		this._timePsn = _timePsn;
		this._isSend = _isSend;
		this._isMe = _isMe;
		this._idGrb = _idGrb;
	}
	
	public long get_isOpen() {
		return _isOpen;
	}

	public void set_isOpen(long _isOpen) {
		this._isOpen = _isOpen;
	}

	public long get_idPsn() {
		return _idPsn;
	}

	public void set_idPsn(long _idPsn) {
		this._idPsn = _idPsn;
	}

	public long get_idUser() {
		return _idUsr;
	}

	public void set_idUser(long _idUser) {
		this._idUsr = _idUser;
	}

	public String get_isiPsn() {
		return _isiPsn;
	}

	public void set_isiPsn(String _isiPsn) {
		this._isiPsn = _isiPsn;
	}

	public String get_filePsn() {
		return _filePsn;
	}

	public void set_filePsn(String _filePsn) {
		this._filePsn = _filePsn;
	}

	public String get_timePsn() {
		return _timePsn;
	}

	public void set_timePsn(String _timePsn) {
		this._timePsn = _timePsn;
	}

	public int get_isSend() {
		return _isSend;
	}

	public void set_isSend(int _isSend) {
		this._isSend = _isSend;
	}

	public int get_isMe() {
		return _isMe;
	}

	public void set_isMe(int _isMe) {
		this._isMe = _isMe;
	}
	public long get_idGrb() {
		return _idGrb;
	}

	public void set_idGrb(long _idGrb) {
		this._idGrb = _idGrb;
	}
}
