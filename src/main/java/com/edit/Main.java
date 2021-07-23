package com.edit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.rendering.PDFRenderer;

public class Main {
	
	public static void pdfSplitter(String filePath) {
		try {
			PDDocument originalDocument = PDDocument.load(new File(filePath));
			PDDocument copyDocument = new PDDocument();
			
			int pages = originalDocument.getNumberOfPages();
			
			for(int i = 1; i < pages; i++) {
				PDFRenderer renderer = new PDFRenderer(originalDocument);
				BufferedImage image = renderer.renderImage(i, 2);
				
				
				int newWidth = image.getWidth() / 2;
				int newHeight = image.getHeight();
				
				
				BufferedImage imageLeft = image.getSubimage(0, 0, newWidth, newHeight);
				BufferedImage imageRight = image.getSubimage(newWidth, 0, newWidth, newHeight);
				
				
				PDPage newPageLeft = new PDPage(new PDRectangle(imageLeft.getWidth(), imageLeft.getHeight()));
				PDPage newPageRight = new PDPage(new PDRectangle(imageRight.getWidth(), imageRight.getHeight()));
				
				
				PDPageContentStream contents = new PDPageContentStream(copyDocument, newPageLeft);
				contents.drawImage(JPEGFactory.createFromImage(copyDocument, imageLeft, 1), 0, 0);
				contents.close();
				contents = new PDPageContentStream(copyDocument, newPageRight);
				contents.drawImage(JPEGFactory.createFromImage(copyDocument, imageRight, 1), 0, 0);
				contents.close();
				
				
				copyDocument.addPage(newPageLeft);
				copyDocument.addPage(newPageRight);
				
				System.out.println(i + "/" + pages + ":");
			}
			
			copyDocument.save(new File("C:/Users/berke/Desktop/out.pdf"));
			copyDocument.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		pdfSplitter("C:/Users/berke/Desktop/test.pdf");
	}
}
