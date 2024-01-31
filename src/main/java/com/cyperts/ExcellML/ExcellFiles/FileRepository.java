package com.cyperts.ExcellML.ExcellFiles;

import java.io.File;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<TemplateFile, Long> {

	TemplateFile getFileDataByUserId(long userId);

	void deleteByUserId(long userId);

	// TemplateFile getFileById(long id);
	public TemplateFile getFileById(long userId);

	TemplateFile findByuserId(long userId);

}
