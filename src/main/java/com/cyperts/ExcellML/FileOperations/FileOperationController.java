
package com.cyperts.ExcellML.FileOperations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.cyperts.ExcellML.ExcellFiles.FileRepository;
import com.cyperts.ExcellML.ExcellFiles.FileServiceImpl;
import com.cyperts.ExcellML.ExcellFiles.TemplateFile;
import com.cyperts.ExcellML.UserAndRole.Role;
import com.cyperts.ExcellML.UserAndRole.User;
import com.cyperts.ExcellML.UserAndRole.UserRepository;
import com.cyperts.ExcellML.jwt.JwtUtil;

@Controller
@RequestMapping("/files")
public class FileOperationController {

	@Autowired
	private FileServiceImpl fileServiceImpl;
	@Autowired
	private FileOperationService fileOperationService;
	@Autowired
	private FileOperationRepository fileOperationRepository;
	@Autowired
	private FileHeaderRepository fileHeaderRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	FileRepository fileRepo;
	@Autowired
	private JwtUtil jwtUtil;
	

//	to read the excell file data
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/fileOperation")
	public ResponseEntity<List<FileOperations>> uploadFileData(@RequestParam long userId) {
		try {

			File file = fileServiceImpl.getUserFileByUserId(userId);

//			File convFile = new File(file.getOriginalFilename());
//			FileOutputStream fos = new FileOutputStream(convFile);
//			fos.write(file.getBytes());
//			fos.close();
//			String fileContentType = file.getContentType();
//			byte[] sourceFileContent = file.getBytes();
//			String fileName = file.getOriginalFilename();
//			TemplateFile fileModal = new TemplateFile(fileName, sourceFileContent, fileContentType, userId);
			List<FileOperations> uploadFileOperation = fileOperationService.uploadFileOperation(file, userId);

//			fileServiceImpl.saveFile(fileModal);
			return new ResponseEntity<List<FileOperations>>(uploadFileOperation, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/fileOperation/header")
	public ResponseEntity<List<FileHeaders>> uploadHeaderData(@RequestBody MultipartFile file,
			@RequestParam long userId) {
		try {
			File convFile = new File(file.getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();

			List<FileHeaders> saveHeaders = fileOperationService.saveHeaders(convFile, userId);
			return new ResponseEntity<List<FileHeaders>>(saveHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	to update header mapping
	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping("/fileOperation/headerMapping")
	public ResponseEntity<FileHeaders> UploadHeaderMappings(@RequestBody FileHeaders fileHeaders) {
		try {
			FileHeaders updateHeaderMapping = fileOperationService.updateHeaderMapping(fileHeaders);

			return new ResponseEntity<FileHeaders>(updateHeaderMapping, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

//	to get List of file headers
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/fileHeaders/{userId}")
	public ResponseEntity<List<FileHeaders>> getFileHeaderDataByUserId(@PathVariable long userId,@RequestHeader("Authorization") String token) {
		try {
			String jwtToken = jwtUtil.resolveToken(
					((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
			
			User user = jwtUtil.getUserFromToken(jwtToken);
			Role roles = user.getRoles();
			String name = roles.getName();
			System.out.println("Role of User :::: "+name);
			if (name.equals("ROLE_ADMIN")) {
				List<FileHeaders> fileHeaderList = fileOperationService.getFileHeaderDataByUserId(userId);
				return new ResponseEntity<List<FileHeaders>>(fileHeaderList, HttpStatus.OK);
			}else {
       		 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

//	to get File data 
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getData/{userId}/{pageNumber}")
	public ResponseEntity<List<FileOperations>> getFileDataByUserId(@PathVariable int pageNumber,
			@PathVariable long userId) {
		try {

			List<FileOperations> fileOperations = fileOperationService.getFileOperationDataByUserId(userId);
			int startIndex = (pageNumber - 1) * 20;
			int endIndex = Math.min(startIndex + 20, fileOperations.size());
			System.out.println("/" + startIndex + "/" + endIndex + "/" + fileOperations.size());
			fileOperations = new ArrayList<>(fileOperations.subList(startIndex, endIndex));

			return ResponseEntity.ok().body(fileOperations);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

// to download File
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/downloadExcel/{userId}")
	public ResponseEntity<byte[]> downloadExcelData(@PathVariable long userId) {
		try {
			List<FileOperations> fileOperations = fileOperationService.getFileOperationDataByUserId(userId);
			// Create Excel Workbook and populate data
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			fileOperationService.generateExcelFile(fileOperations, outputStream, userId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "Excell-File.xlsx");

			return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	// to download searched data
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/file-operations/searchAndDownload/{userId}")
	public ResponseEntity<byte[]> downLoadSearchedData(@PathVariable long userId, @RequestParam String searchTerm) {
		try {
			List<FileOperations> searchResults = fileOperationRepository.findByUserIdAndSearchTerm(userId, searchTerm);
			// String fileName = fileOperationService.findFileNameByUserId(userId);
			TemplateFile fileData = fileRepo.getFileDataByUserId(userId);
			String fileName1 = fileData.getFileName();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			fileOperationService.generateExcelFile(searchResults, outputStream, userId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", fileName1);

			return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}

	// for searching data only
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/file-operations/search/{userId}/{pageNumber}")
	public ResponseEntity<List<FileOperations>> searchByUserId(@PathVariable int pageNumber, @PathVariable long userId,
			@RequestParam String searchTerm) {
		List<FileOperations> fileOperationsList = fileOperationRepository.findByUserIdAndSearchTerm(userId, searchTerm);
		int startIndex = (pageNumber - 1) * 20;
		int endIndex = Math.min(startIndex + 20, fileOperationsList.size());
		System.out.println("/" + startIndex + "/" + endIndex + "/" + fileOperationsList.size());
		fileOperationsList = new ArrayList<>(fileOperationsList.subList(startIndex, endIndex));

		if (fileOperationsList != null && !fileOperationsList.isEmpty()) {
			return new ResponseEntity<>(fileOperationsList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// for sorting data by dataId
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/file-operations/sort/{order}/{userId}/{pageNumber}")
	public ResponseEntity<List<FileOperations>> getAllSortedBydataId(@PathVariable int pageNumber,
			@PathVariable String order, @PathVariable long userId) {
		try {
			List<FileOperations> sortedFileOperations;
			if ("asc".equalsIgnoreCase(order)) {
				sortedFileOperations = fileOperationRepository.findByUserIdOrderByDataIdAsc(userId);
			} else if ("desc".equalsIgnoreCase(order)) {
				sortedFileOperations = fileOperationRepository.findByUserIdOrderByDataIdDesc(userId);
			} else {
				return ResponseEntity.badRequest().build();
			}
			int startIndex = (pageNumber - 1) * 20;
			int endIndex = Math.min(startIndex + 20, sortedFileOperations.size());
			System.out.println("/" + startIndex + "/" + endIndex + "/" + sortedFileOperations.size());
			sortedFileOperations = new ArrayList<>(sortedFileOperations.subList(startIndex, endIndex));

			if (sortedFileOperations != null && !sortedFileOperations.isEmpty()) {
				return ResponseEntity.ok().body(sortedFileOperations);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	// to delete headers by Id and userId
	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/fileHeaders/{userId}/{id}")
	public ResponseEntity<Long> deleteFileHeaderDataByUserId(@PathVariable long userId, @PathVariable long id) {
		try {
			long deletedHeaderId = fileOperationService.deleteFileHeaderDataByUserIdAndId(userId, id);
			return new ResponseEntity<Long>(deletedHeaderId, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

//	@CrossOrigin(origins = "http://localhost:3000")
//	@GetMapping("/getHeaders/{userId}/{id}")
//	public ResponseEntity<FileHeaders> getFileHeaderByUserIdAndId(@PathVariable long userId, @PathVariable long id) {
//		try {
//
//			FileHeaders fileHeader = fileOperationService.getFileHeaderByUserIdAndId(userId, id);
//			return new ResponseEntity<FileHeaders>(fileHeader, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//
//	}

	// to count whole rows for pagination
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/file-operations/count/{userId}")
	public ResponseEntity<Integer> getTotalNumberOfRows(@PathVariable long userId) {
		try {
			List<FileOperations> sortedFileOperations = fileOperationRepository.getAllUserDataByUserId(userId);
			int rowCount = sortedFileOperations.size();
			return ResponseEntity.ok(rowCount);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(0);
		}
	}
}
