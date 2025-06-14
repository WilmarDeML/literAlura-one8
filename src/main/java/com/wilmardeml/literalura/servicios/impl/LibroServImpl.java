package com.wilmardeml.literalura.servicios.impl;

import com.wilmardeml.literalura.modelos.Libro;
import com.wilmardeml.literalura.repositorios.LibroRepositorio;
import com.wilmardeml.literalura.servicios.serv.LibroServ;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServImpl implements LibroServ {
    private final LibroRepositorio libroRepositorio;

    public LibroServImpl(LibroRepositorio libroRepositorio) {
        this.libroRepositorio = libroRepositorio;
    }

    @Override
    public List<Libro> buscarPorIdioma(String idioma) {
        return libroRepositorio.findAllByIdioma(idioma);
    }

    @Override
    public Libro buscarPorTitulo(String titulo) {
        return libroRepositorio.findByTitulo(titulo);
    }

    @Override
    public List<Libro> listarTop10() {
        return libroRepositorio.findFirst10ByOrderByNumeroDescargasDesc();
    }

    @Override
    public List<Libro> listarTodo() {
        return libroRepositorio.findAllByOrderByTitulo();
    }
}
