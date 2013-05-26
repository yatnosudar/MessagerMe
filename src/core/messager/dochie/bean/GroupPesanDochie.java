package core.messager.dochie.bean;

public class GroupPesanDochie {
	private long _idPsn;
	private long _idUsr;
	private String _isiPsn;
	private String _filePsn;
	private String _timePsn;
	private int _isSend;
	private int _isMe;
	private long _idGrb;
	private String _namaUsr;
	private String _emailUsr;
	private String _nohpUsr;
	private String _namaGrb;
	
	
	public GroupPesanDochie() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GroupPesanDochie(long _idUsr, String _isiPsn, String _filePsn,
			String _timePsn, int _isSend, int _isMe, long _idGrb,
			String _namaUsr, String _emailUsr, String _nohpUsr, String _namaGrb) {
		super();
		this._idUsr = _idUsr;
		this._isiPsn = _isiPsn;
		this._filePsn = _filePsn;
		this._timePsn = _timePsn;
		this._isSend = _isSend;
		this._isMe = _isMe;
		this._idGrb = _idGrb;
		this._namaUsr = _namaUsr;
		this._emailUsr = _emailUsr;
		this._nohpUsr = _nohpUsr;
		this._namaGrb = _namaGrb;
	}
	public GroupPesanDochie(long _idPsn, long _idUsr, String _isiPsn,
			String _filePsn, String _timePsn, int _isSend, int _isMe,
			long _idGrb, String _namaUsr, String _emailUsr, String _nohpUsr,
			String _namaGrb) {
		super();
		this._idPsn = _idPsn;
		this._idUsr = _idUsr;
		this._isiPsn = _isiPsn;
		this._filePsn = _filePsn;
		this._timePsn = _timePsn;
		this._isSend = _isSend;
		this._isMe = _isMe;
		this._idGrb = _idGrb;
		this._namaUsr = _namaUsr;
		this._emailUsr = _emailUsr;
		this._nohpUsr = _nohpUsr;
		this._namaGrb = _namaGrb;
	}
	public long get_idPsn() {
		return _idPsn;
	}
	public void set_idPsn(long _idPsn) {
		this._idPsn = _idPsn;
	}
	public long get_idUsr() {
		return _idUsr;
	}
	public void set_idUsr(long _idUsr) {
		this._idUsr = _idUsr;
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
	public String get_namaUsr() {
		return _namaUsr;
	}
	public void set_namaUsr(String _namaUsr) {
		this._namaUsr = _namaUsr;
	}
	public String get_emailUsr() {
		return _emailUsr;
	}
	public void set_emailUsr(String _emailUsr) {
		this._emailUsr = _emailUsr;
	}
	public String get_nohpUsr() {
		return _nohpUsr;
	}
	public void set_nohpUsr(String _nohpUsr) {
		this._nohpUsr = _nohpUsr;
	}
	public String get_namaGrb() {
		return _namaGrb;
	}
	public void set_namaGrb(String _namaGrb) {
		this._namaGrb = _namaGrb;
	}
	
	
}
