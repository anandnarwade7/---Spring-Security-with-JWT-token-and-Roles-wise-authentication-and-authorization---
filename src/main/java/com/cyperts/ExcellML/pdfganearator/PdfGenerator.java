//package com.cyperts.ExcellML.pdfganearator;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.poi.util.TempFile;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
//import com.cyperts.ExcellML.ExcellFiles.FileRepository;
//import com.cyperts.ExcellML.ExcellFiles.TemplateFile;
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//
//public class PdfGenerator {
//
//	@Value("${pdfDir}")
//	private String pdfDir;
//
//	@Value("${reportFileName}")
//	private String reportFileName;
//
//	@Value("${reportFileNameDateFormat}")
//	private String reportFileNameDateFormat;
//
//	@Value("${localDateFormat}")
//	private String localDateFormat;
//
//	@Value("${logoImgPath}")
//	private String logoImgPath;
//
//	@Value("${logoImgScale}")
//	private Float[] logoImgScale;
//
//	@Value("${currencySymbol:}")
//	private String currencySymbol;
//
//	@Value("${table_noOfColumns}")
//	private int noOfColumns;
//
//	@Value("${table.columnNames}")
//	private List<String> columnNames;
//
//	@Autowired
//	FileRepository fileRepo;
//
//	private static Font COURIER = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);
//	private static Font COURIER_SMALL = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
//	private static Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);
//
//	public void generatePdfReport() throws DocumentException {
//
//		Document document = new Document();
//
//		try {
//			PdfWriter.getInstance(document, new FileOutputStream("D:/PdfReportRepo/generatedPdf.pdf"));
//			document.open();
//			addLogo(document);
//			addDocTitle(document);
//			createTable(document, 4);
//			addFooter(document);
//			document.close();
//			System.out.println("------------------Your PDF Report is ready!-------------------------");
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	private void addLogo(Document document) throws DocumentException {
//		try {
//			Image img = Image.getInstance("D:\\java-logo-1.png");
//			img.scalePercent(50, 50);
//			img.setAlignment(Element.ALIGN_RIGHT);
//			document.add(img);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private void addDocTitle(Document document) throws DocumentException {
//		long currentTimeMillis = System.currentTimeMillis();
//		Date currentDate = new Date(currentTimeMillis);
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String localDateString = dateFormat.format(currentDate);
//		Paragraph p1 = new Paragraph();
//		leaveEmptyLine(p1, 1);
//		p1.add(new Paragraph("Excell_ML", COURIER));
//		p1.setAlignment(Element.ALIGN_CENTER);
//		leaveEmptyLine(p1, 1);
//		p1.add(new Paragraph("Report generated on " + localDateString, COURIER_SMALL));
//
//		document.add(p1);
//
//	}
//
//	private void createTable(Document document, int noOfColumns) throws DocumentException {
//		Paragraph paragraph = new Paragraph();
//		leaveEmptyLine(paragraph, 3);
//		document.add(paragraph);
//
//		List<String> nameOfColumns = new ArrayList<>();
//		nameOfColumns.add("File name");
//		nameOfColumns.add("Data");
//		nameOfColumns.add("file Data");
//		nameOfColumns.add("User Id");
//		PdfPTable table = new PdfPTable(noOfColumns);
//
//		for (int i = 0; i < noOfColumns; i++) {
//			PdfPCell cell = new PdfPCell(new Phrase(nameOfColumns.get(i)));
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			cell.setBackgroundColor(BaseColor.CYAN);
//			table.addCell(cell);
//		}
//
//		table.setHeaderRows(1);
//		getDbData(table);
//		document.add(table);
//	}
//
//	private void getDbData(PdfPTable table) {
//
//		ArrayList<TemplateFile> list = new ArrayList<>();
//		list.add(new TemplateFile("shubham", null, "excell", 100));
//		list.add(new TemplateFile("shubham", null, "excell", 100));
//		list.add(new TemplateFile("shubham", null, "excell", 100));
//		list.add(new TemplateFile("shubham", null, "excell", 100));
//		list.add(new TemplateFile("shubham", null, "excell", 100));
//		list.add(new TemplateFile("shubham", null, "excell", 100));
//		list.add(new TemplateFile("shubham", null, "excell", 100));
//		list.add(new TemplateFile("shubham", null, "excell", 100));
//		list.add(new TemplateFile("shubham", null, "excell", 100));
//
//		for (TemplateFile file : list) {
//
//			table.setWidthPercentage(100);
//			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//			table.addCell(file.getFileName());
//			table.addCell(file.getFileType());
//			table.addCell(file.getUserId());
//			table.addCell(currencySymbol + file.getId());
//
//		}
//
//	}
//
//	private void addFooter(Document document) throws DocumentException {
//		Paragraph p2 = new Paragraph();
//		leaveEmptyLine(p2, 3);
//		p2.setAlignment(Element.ALIGN_MIDDLE);
//		p2.add(new Paragraph("------------------------End Of " + reportFileName + "------------------------",
//				COURIER_SMALL_FOOTER));
//
//		document.add(p2);
//	}
//
//	private static void leaveEmptyLine(Paragraph paragraph, int number) {
//		for (int i = 0; i < number; i++) {
//			paragraph.add(new Paragraph(" "));
//		}
//	}
//
//	private String getPdfNameWithDate() {
////		String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(reportFileNameDateFormat));
//		return "D:/PdfReportRepo/" + "generatedPdf" + "-" + "121212" + ".pdf";
//	}
//}
