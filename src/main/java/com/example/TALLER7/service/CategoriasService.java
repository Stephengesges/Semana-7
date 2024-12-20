/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.TALLER7.service;

import com.example.TALLER7.model.Categorias;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.TALLER7.repository.CategoriasRepository;


@Service
public class CategoriasService {
    
     @Autowired
    private CategoriasRepository repository;
    
    /**
     * Funcion para listar la tabla categoria
     * @return 
     */
    public List<Categorias> listarTodas() {
        return repository.findAll();
    }
    
    /**
     * Funcion para guardar datos de una categoria
     * @param categoria 
     */
    public void guardar(Categorias categoria) {
        repository.save(categoria);
    }
    
    /**
     * Funcion para buscar una categoria por id
     * @param id
     * @return 
     */
    public Optional<Categorias> buscarPorId(Long id) {
        return repository.findById(id);
    }
    
    /**
     * Funcion para eliminar el registro de un categoria
     * @param id 
     */
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
