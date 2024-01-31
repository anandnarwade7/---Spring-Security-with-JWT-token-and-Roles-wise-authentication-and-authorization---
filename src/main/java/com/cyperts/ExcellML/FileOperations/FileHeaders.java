package com.cyperts.ExcellML.FileOperations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class FileHeaders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String fileHeader;
	
	private String headerName;
	
	private long userId;
	
	private String fileName;
	
	@Column(columnDefinition = "bigint default 0")
	private long createdOn;
	@Column(columnDefinition = "bigint default 0")
	private long editedOn;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileHeader() {
		return fileHeader;
	}

	public void setFileHeader(String fileHeader) {
		this.fileHeader = fileHeader;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@PrePersist
	protected void prePersistFunction() {
		this.createdOn = System.currentTimeMillis();
		this.editedOn = System.currentTimeMillis();
	}

	@PreUpdate
	protected void preUpdateFunction() {
		this.editedOn = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "FileHeaders [id=" + id + ", fileHeader=" + fileHeader + ", headerName=" + headerName + ", userId="
				+ userId + ", fileName=" + fileName + ", createdOn=" + createdOn + ", editedOn=" + editedOn + "]";
	}

	public FileHeaders(long id, String fileHeader, String headerName, long userId, String fileName, long createdOn,
			long editedOn) {
		super();
		this.id = id;
		this.fileHeader = fileHeader;
		this.headerName = headerName;
		this.userId = userId;
		this.fileName = fileName;
		this.createdOn = createdOn;
		this.editedOn = editedOn;
	}

	public FileHeaders() {
		super();
		// TODO Auto-generated constructor stub
	}

}
