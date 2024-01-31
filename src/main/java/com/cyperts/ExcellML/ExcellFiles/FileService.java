package com.cyperts.ExcellML.ExcellFiles;

import java.util.*;

public interface FileService {

	List<TemplateFile> getAllFiles();

	List<TemplateFile> saveAllFilesList(List<TemplateFile> fileList);
}
