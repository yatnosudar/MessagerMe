package core.messager.dochie.bean;

public class GroupDochie {
	private String _idGrb;
	private String _namaGrb;

	public GroupDochie() {
		super();
	}

	public GroupDochie(String _idGrb) {
		super();
		this._idGrb = _idGrb;
	}

	// public group_dochie(String _namaGrb) {
	// super();
	// this._namaGrb = _namaGrb;
	// }

	public GroupDochie(String _idGrb, String _namaGrb) {
		super();
		this._idGrb = _idGrb;
		this._namaGrb = _namaGrb;
	}

	public String get_idGrb() {
		return _idGrb;
	}

	public void set_idGrb(String _idGrb) {
		this._idGrb = _idGrb;
	}

	public String get_namaGrb() {
		return _namaGrb;
	}

	public void set_namaGrb(String _namaGrb) {
		this._namaGrb = _namaGrb;
	}

}
