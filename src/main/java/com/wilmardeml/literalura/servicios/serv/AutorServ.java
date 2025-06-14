package com.wilmardeml.literalura.servicios.serv;

import com.wilmardeml.literalura.modelos.Autor;

import java.util.List;

public interface AutorServ extends CommonServ<Autor>{
    List<Autor> buscarAutoresVivosEn(int anio, int anio1);

    Autor buscarPorNombre(String nombre);

    void guardar(Autor autor);

    List<Autor> filtrarPorNombre(String nombreAutor);
}
