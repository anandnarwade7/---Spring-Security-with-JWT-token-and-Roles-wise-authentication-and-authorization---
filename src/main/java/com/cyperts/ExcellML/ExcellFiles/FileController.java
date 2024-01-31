package com.cyperts.ExcellML.ExcellFiles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class FileController {

	@Autowired
	private FileServiceImpl fileServiceImpl;

//	@GetMapping("/")
//	public ResponseEntity<String> getData() throws DocumentException {
//		PdfGenerator pDFGenerator = new PdfGenerator();
//
//		pDFGenerator.generatePdfReport();
//		return new ResponseEntity<String>("success",HttpStatus.OK);
//	}

	@PostMapping("/upload")
	public ResponseEntity<List<TemplateFile>> uploadMultipartFile(@RequestParam("files") MultipartFile[] files) {
		try {
			// Declare empty list for collect the files data
			// which will come from UI
			List<TemplateFile> fileList = new ArrayList<TemplateFile>();
			for (MultipartFile file : files) {
				String fileContentType = file.getContentType();
				byte[] sourceFileContent = file.getBytes();
				String fileName = file.getOriginalFilename();
				TemplateFile fileModal = new TemplateFile(fileName, sourceFileContent, fileContentType, 100);

				// Adding file into fileList
				fileList.add(fileModal);
			}

			// Saving all the list item into database
			List<TemplateFile> saveAllFilesList = fileServiceImpl.saveAllFilesList(fileList);
			return new ResponseEntity<List<TemplateFile>>(saveAllFilesList, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
