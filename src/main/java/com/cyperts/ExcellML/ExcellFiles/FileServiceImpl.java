package com.cyperts.ExcellML.ExcellFiles;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private FileRepository fileRepository;

	@Override
	public List<TemplateFile> getAllFiles() {
		List<TemplateFile> list = this.fileRepository.findAll();
		return list;
	}

	@Override
	public List<TemplateFile> saveAllFilesList(List<TemplateFile> fileList) {
		for (TemplateFile fileModal : fileList)
			fileRepository.save(fileModal);
		return fileList;

	}

	public void saveFile(TemplateFile fileModal) {
		fileRepository.save(fileModal);

	}

//	public ResponseEntity<?> getUserFileByUserId(long userId) {
//
//		try {
//			if (fileRepository.existsById(userId)) {
//				File userDataFile = fileRepository.getFileDataByUSerId(userId);
//				return ResponseEntity.ok().body(userDataFile);
//			} else {
//				throw new ResourceNotFoundException("File Not found for " + userId, null);
//			}
//		} catch (ResourceNotFoundException e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Error occurred while retrieving user data file.");
//		}
//
//	}

	public File getUserFileByUserId(long userId) {

		try {
				TemplateFile fileDataByUserId = fileRepository.getFileDataByUserId(userId);
				System.out.println("check point 1");
				File file = File.createTempFile("UserFile", ".xlsx");
				byte[] data = fileDataByUserId.getContent();
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data);
				fos.close();

				return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
