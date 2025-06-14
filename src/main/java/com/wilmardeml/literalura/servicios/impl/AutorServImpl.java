package com.wilmardeml.literalura.servicios.impl;

import com.wilmardeml.literalura.modelos.Autor;
import com.wilmardeml.literalura.repositorios.AutorRepositorio;
import com.wilmardeml.literalura.servicios.serv.AutorServ;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorServImpl implements AutorServ {
    private final AutorRepositorio autorRepositorio;

    public AutorServImpl(AutorRepositorio autorRepositorio) {
        this.autorRepositorio = autorRepositorio;
    }

    @Override
    public List<Autor> buscarAutoresVivosEn(int anio, int anio1) {
        return autorRepositorio.findByAnioMuerteGreaterThanAndAnioNacimientoLessThan(anio, anio);
    }

    @Override
    public Autor buscarPorNombre(String nombre) {
        return autorRepositorio.findByNombre(nombre);
    }

    @Override
    public void guardar(Autor autor) {
        autorRepositorio.save(autor);
    }

    @Override
    public List<Autor> filtrarPorNombre(String nombreAutor) {
        return autorRepositorio.buscarPorNombre(nombreAutor);
    }

    @Override
    public List<Autor> listarTodo() {
        return autorRepositorio.findAllByOrderByNombre();
    }
}
