package core.messager.dochie.bean;

public class KirimEmailDochie {
	String _subject;
	String _body;
	String _from;
	String _to;

	
	public KirimEmailDochie() {
		super();
	}

	public KirimEmailDochie(String _subject, String _body, String _from, String _to) {
		super();
		this._subject = _subject;
		this._body = _body;
		this._from = _from;
		this._to = _to;
	}

	public String get_subject() {
		return _subject;
	}

	public void set_subject(String _subject) {
		this._subject = _subject;
	}

	public String get_body() {
		return _body;
	}

	public void set_body(String _body) {
		this._body = _body;
	}

	public String get_from() {
		return _from;
	}

	public void set_from(String _from) {
		this._from = _from;
	}

	public String get_to() {
		return _to;
	}

	public void set_to(String _to) {
		this._to = _to;
	}

}
