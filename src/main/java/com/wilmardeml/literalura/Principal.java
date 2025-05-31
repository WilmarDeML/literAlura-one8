package com.wilmardeml.literalura;

import java.util.Scanner;

public class Principal {
    private final static Scanner TECLADO = new Scanner(System.in);
    private static String opcion = "-1";

    public static void init() {
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
    }

    private static void listarLibros() {
    }

    private static void buscarLibroPorTitulo() {
    }
}
