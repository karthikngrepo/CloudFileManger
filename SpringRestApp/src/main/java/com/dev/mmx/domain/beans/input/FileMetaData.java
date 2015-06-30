package com.dev.mmx.domain.beans.input;


public class FileMetaData {
	
	private String _filename;

	private String _fileSize;
	
	private String _owner;
	
	private String _lastModified;
	
	private String _created;
	
	private String _accessed;
	
	private String _fileType;
	
	private String _contentType;
	
	public String get_filename() {
		return _filename;
	}
	
	public void set_filename(String _filename) {
		this._filename = _filename;
	}
	
	public String get_fileSize() {
		return _fileSize;
	}
	
	public void set_fileSize(String _fileSize) {
		this._fileSize = _fileSize;
	}
	
	public String get_owner() {
		return _owner;
	}
	
	public void set_owner(String _owner) {
		this._owner = _owner;
	}
	
	public String get_accessed() {
		return _accessed;
	}
	
	public void set_accessed(String _accessed) {
		this._accessed = _accessed;
	}
	
	public String get_created() {
		return _created;
	}
	
	public void set_created(String _created) {
		this._created = _created;
	}
	
	public String get_lastModified() {
		return _lastModified;
	}
	
	public void set_lastModified(String _lastModified) {
		this._lastModified = _lastModified;
	}
	
	public String get_fileType() {
		return _fileType;
	}
	
	public void set_fileType(String _fileType) {
		this._fileType = _fileType;
	}
	
	public String get_contentType() {
		return _contentType;
	}
	
	public void set_contentType(String _contentType) {
		this._contentType = _contentType;
	}
}
