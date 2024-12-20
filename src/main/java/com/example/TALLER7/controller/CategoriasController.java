/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.TALLER7.controller;

import com.example.TALLER7.model.Categorias;
import com.example.TALLER7.service.CategoriasService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletResponse;


// importaciones pdf
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.IOException;
import java.util.List;
import com.itextpdf.layout.Document;


// importaciones excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author FLOMAR
 */
@Controller
@RequestMapping("/categorias")
public class CategoriasController {
    
     private final CategoriasService service;

    // Constructor para inyectar el servicio
    public CategoriasController(CategoriasService service) {
        this.service = service;
    }

    // Mostrar todos las categorias
    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", service.listarTodas());
        return "categorias"; // Vista que muestra todos los proveedores
    }

    // Mostrar el formulario para crear una nueva categorias
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("categoria", new Categorias());
        return "formulario"; // Vista para el formulario de una nueva categorias
    }

    // Guardar una nueva categoria
    @PostMapping
    public String guardarCategoria(@ModelAttribute Categorias categoria) {
        service.guardar(categoria);
        return "redirect:/categorias"; // Redirigir a la lista de categoria después de guardar
    }

    // Mostrar el formulario para editar una categorias existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", service.buscarPorId(id).orElseThrow(() ->
                new IllegalArgumentException("ID inválido: " + id)));
        return "formulario"; // Vista para el formulario de edición de cliente
    }

    // Eliminar una categorias por su ID
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/categorias"; // Redirigir a la lista de categorias después de eliminar
    }
    
    @GetMapping("/reporte/pdf")
    public void generarPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=categorias_reporte.pdf");
        
        PdfWriter write = new PdfWriter(response.getOutputStream());
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(write));
        
        document.add(new Paragraph("Reporte de categoria").setBold().setFontSize(18));
        
        Table table = new Table(3);
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Descripcion");
        
        
        List<Categorias> categorias = this.service.listarTodas();
        for (Categorias categoria : categorias) {
            table.addCell(categoria.getId().toString());
            table.addCell(categoria.getNombre());
            table.addCell(categoria.getDescripcion());            
           
        }
        
        document.add(table);
        document.close();
    }
    
    @GetMapping("/reporte/excel")
    public void generarExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=categorias_reporte.xlsx");
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Categorias");
        
        Row headerRow = sheet.createRow(0);
        String[] columnHeaders = {"ID","Nombre", "Descripcion"};
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }
        
        List<Categorias> categorias = this.service.listarTodas();
        int rowIndex = 1;
        for (Categorias categoria : categorias) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(categoria.getId());
              row.createCell(1).setCellValue(categoria.getNombre());
            row.createCell(2).setCellValue(categoria.getDescripcion());
            
        }
        
        /*for (int i = 0; columnHeaders.length; i++) {
            sheet.autoSizeColumn(i);
        }*/
        
        workbook.write(response.getOutputStream());
        workbook.close();
        
    }
    
    
    
}
