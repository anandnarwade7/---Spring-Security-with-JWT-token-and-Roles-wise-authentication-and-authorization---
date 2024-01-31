package com.cyperts.ExcellML.FileOperations;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileOperationRepository extends JpaRepository<FileOperations, Long> {

	List<FileOperations> getAllUserDataByUserId(long userId);

	@Query("SELECT fo FROM FileOperations fo " + "WHERE fo.userId = :userId AND " + "(:searchTerm IS NULL OR "
			+ "(CAST(fo.upc AS string) LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR CAST(fo.dataId AS string) LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR fo.hyperLink LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR CAST(fo.price AS string) LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR CAST(fo.quantity AS string) LIKE CONCAT('%', :searchTerm, '%') OR "
			+ "(DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%Y') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%y') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%d') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%M') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%b') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%M %e') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%e %M') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%b %e') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%e %b') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(fo.createdOn / 1000), '%b %e %Y') = :searchTerm) "
			+ "OR CAST(fo.availability AS string) LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR fo.department LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR CAST(fo.casepack AS string) LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR fo.description LIKE CONCAT('%', :searchTerm, '%') " + "OR fo.type LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR fo.designer LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR fo.productName LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR fo.brand LIKE CONCAT('%', :searchTerm, '%')))")
	List<FileOperations> findByUserIdAndSearchTerm(@Param("userId") long userId,
			@Param("searchTerm") String searchTerm);

	List<FileOperations> findByUserIdOrderByDataIdAsc(long userId);

	List<FileOperations> findByUserIdOrderByDataIdDesc(long userId);

	void deleteByUserId(long userId);

	List<FileOperations> findByUserId(long userId);
}
