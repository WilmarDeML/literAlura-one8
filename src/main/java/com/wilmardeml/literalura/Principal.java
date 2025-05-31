package com.wilmardeml.literalura;

import com.wilmardeml.literalura.modelos.*;
import com.wilmardeml.literalura.repositorios.AutorRepositorio;
import com.wilmardeml.literalura.repositorios.LibroRepositorio;
import com.wilmardeml.literalura.servicios.ConsumoAPI;
import com.wilmardeml.literalura.servicios.ConvierteDatos;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final static Scanner TECLADO = new Scanner(System.in);
    private final static String URL_BASE = "https://gutendex.com/books/?search=";

    private static LibroRepositorio libroRepositorio;
    private static AutorRepositorio autorRepositorio;

    private static String opcion = "-1";

    public static void init(LibroRepositorio lRepositorio, AutorRepositorio aRepositorio) {
        libroRepositorio = lRepositorio;
        autorRepositorio = aRepositorio;
        while (!opcion.equals("0")) {
            mostrarMenu();
            System.out.print("Elija la opción a través de su número: ");
            opcion = TECLADO.nextLine();
            tratarOpcion();
        }
    }

    public static void mostrarMenu() {
        System.out.println("""
                **************************************************
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                
                0 - Salir
                """);
    }

    private static void tratarOpcion() {
        switch (opcion) {
            case "1" -> buscarLibroPorTitulo();
            case "2" -> listarLibros();
            case "3" -> listarAutores();
            case "4" -> listarAutoresVivos();
            case "5" -> listarLibrosPorIdioma();
            case "0" -> System.out.printf("%n... Cerrando aplicación ¡Gracias!%n%n");
            default -> System.out.printf("%nOpción no válida intente con otra opción%n");
        }
    }

    private static void listarLibrosPorIdioma() {
    }

    private static void listarAutoresVivos() {
    }

    private static void listarAutores() {
        List<Autor> autores = autorRepositorio.findAllBy();
        System.out.println("******************* Autores Registrados **************************");
        autores.forEach(Principal::mostrarAutor);
        System.out.println("******************* Fin Autores Registrados **************************");
    }

    private static void listarLibros() {
        List<Libro> libros = libroRepositorio.findAllBy();
        System.out.println("******************* Libros Registrados **************************");
        libros.forEach(Principal::mostrarLibro);
        System.out.println("******************* Fin Libros Registrados **************************");
    }

    private static void buscarLibroPorTitulo() {
        System.out.print("Ingrese el nombre del libro que desea buscar: ");
        var titulo = TECLADO.nextLine();
        String bodyResponse = ConsumoAPI.obtenerDatos(URL_BASE.concat(titulo.replace(" ", "+")));
        ResponseApi result = ConvierteDatos.convertirJsonStringA(bodyResponse, ResponseApi.class);
        if (result.totalLibros().equals(0)) {
            System.out.println("El libro ".concat(titulo).concat(" no fue encontrado!"));
            return;
        }
        if (result.totalLibros() == 1) {
            registrarLibro(result.libros().getFirst());
            return;
        }

        elegirLibroParaRegistrar(result.libros(), result.totalLibros());
    }

    private static void elegirLibroParaRegistrar(List<DatosLibro> libros, Integer totalLibros) {
        System.out.printf("%d / %d libros%n", libros.size(), totalLibros);
        for (int i = 0; i < libros.size(); i++) {
            var libro = libros.get(i);
            System.out.println((i + 1) + " - "
                    .concat(libro.titulo()).concat(" - ")
                    .concat(libro.autores().isEmpty() ? "-" : libro.autores().getFirst().nombre()).concat(" - ")
                    .concat(String.valueOf(libro.numeroDescargas())));
        }
        System.out.print("Elija la opción a través de su número o 0 para regresar al menú anterior: ");
        while (true) {
            var opcion2 = TECLADO.nextLine();
            int indice = obtenerEntero(opcion2) - 1;
            if (indice >= 0 && indice <= libros.size()) {
                registrarLibro(libros.get(indice));
                return;
            }
            if (indice == -1) return;
            System.out.println("Elige una opción válida: ");
        }
    }

    private static int obtenerEntero(String opcion2) {
        try {
            return Integer.parseInt(opcion2);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void registrarLibro(DatosLibro datosLibro) {
        var libroBuscado = libroRepositorio.findByTitulo(datosLibro.titulo());
        if (libroBuscado != null) {
            System.out.println("¡El libro ".concat(libroBuscado.getTitulo()).concat(" ya está registrado!"));
            mostrarLibro(libroBuscado);
            return;
        }
        if (datosLibro.autores().isEmpty()) {
            datosLibro.autores().add(new DatosAutor("Sin autor", 0, 0));
        }

        System.out.println("Registrando libro en base de datos...");

        Libro nuevoLibro = new Libro(datosLibro);
        Autor autor = autorRepositorio.findByNombre(datosLibro.autores().getFirst().nombre());

        if (autor == null) {
            autor = new Autor(datosLibro.autores().getFirst());
        }

        nuevoLibro.setAutor(autor);
        autor.getLibros().add(nuevoLibro);

        autorRepositorio.save(autor); // Salva también el libro porque está en Cascade.ALL

        System.out.println("Libro ".concat(nuevoLibro.getTitulo()).concat(" registrado satisfactoriamente!"));
        mostrarLibro(nuevoLibro);
    }

    private static void mostrarLibro(Libro libro) {
        System.out.printf("""
                
                ************* LIBRO *********************
                Título: %s
                Autor: %s
                Idioma: %s
                Número de descargas: %d
                ************* ***** *********************
                """, libro.getTitulo(), libro.getAutor().getNombre(), libro.getIdioma(), libro.getNumeroDescargas());
    }

    private static void mostrarAutor(Autor autor) {
        System.out.printf("""
                
                ************* Autor *********************
                Autor: %s
                Fecha de nacimiento: %d
                Fecha de fallecimiento: %d
                Libros: [%s]
                ************* ***** *********************
                """, autor.getNombre(), autor.getAnioNacimiento(), autor.getAnioMuerte(),
                autor.getLibros().stream().map(Libro::getTitulo).collect(Collectors.joining(" || ")));
    }

}
