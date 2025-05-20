package com.example.examen3o;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class Principal {

	public static void main(String[] args) {

		ArrayList<Evento> listaEventos = new ArrayList<>();

		try (BufferedReader bufferReader = new BufferedReader(new FileReader("src/main/resources/eventos.txt"))) {
			String linea;
			while ((linea = bufferReader.readLine()) != null) {
				String[] partes = linea.split(",", 4);
				if (partes.length == 4) {
					String nombre = partes[0];
					LocalDateTime fecha = LocalDateTime.parse(partes[1]);
					String ubicacion = partes[2];
					String descripcion = partes[3];
					listaEventos.add(new Evento(nombre, fecha, ubicacion, descripcion));
				}
			}

		} catch (IOException e) {
			System.out.println("Error leyendo el archivo" + e.getMessage());
		}

		Evento evento1 = new Evento();
		evento1.setNombre("Primer festival");
		evento1.setFecha(LocalDateTime.of(2025, 4, 21, 21, 00));
		evento1.setUbicacion("Segovia");
		evento1.setDescripcion("Primer festival");

		Evento evento2 = new Evento();
		evento2.setNombre("Segundo festival");
		evento2.setFecha(LocalDateTime.of(2025, 6, 14, 21, 00));
		evento2.setUbicacion("Valladolid");
		evento2.setDescripcion("Segundo festival");

		Evento evento3 = new Evento();
		evento3.setNombre("Tercer festival");
		evento3.setFecha(LocalDateTime.of(2025, 12, 29, 20, 30));
		evento3.setUbicacion("Barcelona");
		evento3.setDescripcion("Tercer festival");

		Evento evento4 = new Evento();
		evento4.setNombre("Cuarto festival");
		evento4.setFecha(LocalDateTime.of(2025, 12, 29, 20, 30));
		evento4.setUbicacion("Madrid");
		evento4.setDescripcion("Cuarto festival");

		listaEventos.add(evento1);
		listaEventos.add(evento2);
		listaEventos.add(evento3);
		listaEventos.add(evento4);

		try (BufferedWriter bufferWriter = new BufferedWriter(
				new FileWriter("src/main/resources/salida_eventos.txt"))) {
			for (Evento e : listaEventos) {
				bufferWriter
						.write(e.getNombre() + "," + e.getFecha() + "," + e.getUbicacion() + "," + e.getDescripcion());
				bufferWriter.newLine();
			}
			System.out.println("Archivo 'salida_eventos.txt' generado correctamente.");

		} catch (IOException e) {
			System.out.println("Error escribiendo el archivo: " + e.getMessage());
		}

		XSSFWorkbook libro = new XSSFWorkbook();

		XSSFSheet hoja = libro.createSheet("eventos");

		XSSFRow fila0 = hoja.createRow(0);
		fila0.createCell(0).setCellValue("Nombre");
		fila0.createCell(1).setCellValue("Fecha");
		fila0.createCell(2).setCellValue("Ubicación");
		fila0.createCell(3).setCellValue("Descripción");

		int numFilas = 1;
		for (Evento e : listaEventos) {
			XSSFRow fila = hoja.createRow(numFilas++);
			fila.createCell(0).setCellValue(e.getNombre());
			fila.createCell(1).setCellValue(e.getFecha().toString());
			fila.createCell(2).setCellValue(e.getUbicacion());
			fila.createCell(3).setCellValue(e.getDescripcion());
		}

		try (FileOutputStream out = new FileOutputStream("src/main/resources/eventos.xlsx")) {
			libro.write(out);
			libro.close();
			System.out.println("Excel 'eventos.xlsx' generado correctamente.");

		} catch (IOException e) {
			System.out.println("Error escribiendo el Excel: " + e.getMessage());
		}

		try {
			String ruta = "src/main/resources/resumen_eventos.pdf";
			PdfWriter PDFwriter = new PdfWriter(ruta);
			PdfDocument pdf = new PdfDocument(PDFwriter);
			Document document = new Document(pdf);

			Paragraph titulo = new Paragraph("Resumen de Eventos");
			document.add(titulo);

			for (Evento e : listaEventos) {
				String eventoTexto = "Nombre: " + e.getNombre() + "\n" + "Fecha: " + e.getFecha().toString() + "\n"
						+ "Ubicación: " + e.getUbicacion() + "\n" + "Descripción: " + e.getDescripcion() + "\n\n";

				Paragraph parrafoEvento = new Paragraph(eventoTexto);
				document.add(parrafoEvento);
			}

			document.close();
			System.out.println("PDF 'resumen_eventos.pdf' generado correctamente.");

		} catch (IOException e) {
			System.out.println("Error generando el PDF: " + e.getMessage());
		}

	}

}
