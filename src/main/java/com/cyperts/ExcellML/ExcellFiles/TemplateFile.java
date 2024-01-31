package com.cyperts.ExcellML.ExcellFiles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity

public class TemplateFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fileId")
	long id;
	@Column(name = "name")
	String fileName;
	@Lob
	@Column(name = "content", columnDefinition = "LONGBLOB")
	byte[] content;
	@Column(name = "filetype")
	private String fileType;

	@Column(name = "userId")
	private long userId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "TemplateFile [id=" + id + ", fileName=" + fileName + ", content=" + content + ", fileType=" + fileType
				+ ", userId=" + userId + "]";
	}

	public TemplateFile(String fileName, byte[] content, String fileType, long userId) {
		super();
		this.fileName = fileName;
		this.content = content;
		this.fileType = fileType;
		this.userId = userId;
	}

	public TemplateFile() {
		super();
		// TODO Auto-generated constructor stub
	}

}
