package com.cyperts.ExcellML.FileOperations;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileHeaderRepository extends JpaRepository<FileHeaders, Long> {

	FileHeaders getHeadersByIdAndUserId(long id, long userId);

	List<FileHeaders> getListOfFileHeaderByUserId(long userId);

	// FileHeaders findByUserId(long userId);
	@Query("SELECT f.fileName FROM FileHeaders f WHERE f.userId = :userId")
	List<String> findFileNameByUserId(long userId);

	void deleteFileHeaderByUserIdAndId(long userId, long id);

	FileHeaders getListOfFileHeaderByUserIdAndId(long userId, long id);

	void deleteFileHeaderByUserId(long userId);

}
