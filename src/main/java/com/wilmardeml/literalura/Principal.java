package com.wilmardeml.literalura;

import com.wilmardeml.literalura.modelos.*;
import com.wilmardeml.literalura.servicios.impl.client.ConsumoAPI;
import com.wilmardeml.literalura.util.ConvierteDatos;
import com.wilmardeml.literalura.servicios.serv.AutorServ;
import com.wilmardeml.literalura.servicios.serv.LibroServ;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final static Scanner TECLADO = new Scanner(System.in);
    private final static String URL_BASE = "https://gutendex.com/books/";

    private final LibroServ servicioLibros;
    private final AutorServ servicioAutores;

    private static String opcion = "-1";

    public Principal(LibroServ servicioLibros, AutorServ servicioAutores) {
        this.servicioLibros = servicioLibros;
        this.servicioAutores = servicioAutores;
    }

    public void init() {
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
                6 - Generar estadísticas de libros registrados
                7 - Generar estadísticas de libros Api (Primeras 20 páginas)
                8 - Top 10 Api (Primeras 20 páginas)
                9 - Top 10 libros registrados
                10 - Buscar Autor por nombre
                
                0 - Salir
                """);
    }

    private void tratarOpcion() {
        switch (opcion) {
            case "1" -> buscarLibroPorTitulo();
            case "2" -> listarLibros();
            case "3" -> listarAutores();
            case "4" -> listarAutoresVivos();
            case "5" -> listarLibrosPorIdioma();
            case "6" -> generarEstadisticasLibrosRegistrados();
            case "7" -> generarEstadisticasLibrosApi();
            case "8" -> listarTop10Api();
            case "9" -> listarTop10LibrosRegistrados();
            case "10" -> buscarAutorPorNombre();
            case "0" -> System.out.printf("%n... Cerrando aplicación ¡Gracias!%n%n");
            default -> System.out.printf("%nOpción no válida intente con otra opción%n");
        }
    }

    private void buscarAutorPorNombre() {
        System.out.print("Ingrese el nombre del autor que desea buscar: ");
        var nombreAutor = TECLADO.nextLine();
        List<Autor> autoresEncontrados = servicioAutores.filtrarPorNombre(nombreAutor);
        if (autoresEncontrados.isEmpty()) {
            System.out.println("No se halló un autor que contenga en el nombre ".concat(nombreAutor));
            return;
        }

        System.out.printf(
                "********** Autores que contienen '%s' en su nombre **********************************%n", nombreAutor);
        autoresEncontrados.forEach(Principal::mostrarAutor);
        System.out.println("\n********** Fin Autores **********************************");
    }

    private void listarTop10LibrosRegistrados() {
        List<Libro> libros = servicioLibros.listarTop10();
        libros.forEach(Principal::mostrarLibro);
    }

    private void listarTop10Api() {
        var datosApi = obtener20PaginasApi();
        datosApi.stream().sorted(Comparator.comparing(DatosLibro::numeroDescargas).reversed())
                .limit(10)
                .forEach(Principal::mostrarLibroApi);
    }

    private void generarEstadisticasLibrosApi() {
        var librosApi = obtener20PaginasApi();
        generarEstadisticaApi(librosApi);
    }

    private List<DatosLibro> obtener20PaginasApi() {
        String bodyResponse = ConsumoAPI.obtenerDatos(URL_BASE);
        ResponseApi result = ConvierteDatos.convertirJsonStringA(bodyResponse, ResponseApi.class);
        List<DatosLibro> librosApi = new ArrayList<>(result.libros());
        while (result.siguientePagina() != null && !result.siguientePagina().contains("page=20")) {
            bodyResponse = ConsumoAPI.obtenerDatos(result.siguientePagina());
            result = ConvierteDatos.convertirJsonStringA(bodyResponse, ResponseApi.class);
            librosApi.addAll(result.libros());
        }
        return librosApi;
    }

    private void generarEstadisticaApi(List<DatosLibro> librosApi) {
        DoubleSummaryStatistics estadistica = librosApi.stream()
                .mapToDouble(DatosLibro::numeroDescargas)
                .summaryStatistics();

        mostrarEstadistica(estadistica, "Libros registrados:");
    }

    private void generarEstadisticasLibrosRegistrados() {
        var libros = servicioLibros.listarTodo();
        generarEstadistica(libros);
    }

    private void generarEstadistica(List<Libro> libros) {
        DoubleSummaryStatistics estadistica = libros.stream()
                .mapToDouble(Libro::getNumeroDescargas)
                .summaryStatistics();

        mostrarEstadistica(estadistica, "Libros registrados:");
    }

    private void listarLibrosPorIdioma() {
        List<String> idiomas = obtenerIdiomas();
        System.out.print("Elija la opción a través de su número: ");
        var opcion2 = TECLADO.nextLine();
        var indice = obtenerEntero(opcion2) - 1;
        if (indice >= 0 && indice < idiomas.size()) {
            List<Libro> libros = servicioLibros.buscarPorIdioma(idiomas.get(indice));
            if (libros.isEmpty()) {
                System.out.printf("No se encontraron libros con el idioma %s", idiomas.get(indice));
                return;
            }
            libros.forEach(Principal::mostrarLibro);
            return;
        }
        if (indice == -1) return;
        System.out.println("¡Opción inválida, intenta de nuevo!");
    }

    private void listarAutoresVivos() {
        System.out.print("Ingrese el año vivo de autor(es) que desea buscar: ");
        var opcion2 = TECLADO.nextLine();
        var anio = obtenerEntero(opcion2);

        if (anio == -1) {
            System.out.println("¡Año no válido, intenta de nuevo!");
            return;
        }

        List<Autor> autores = servicioAutores.buscarAutoresVivosEn(anio, anio);
        System.out.printf("******************* Autores vivos en %d **************************", anio);
        autores.forEach(Principal::mostrarAutor);
        System.out.printf("******************* Fin Autores vivos en %d **************************%n", anio);
    }

    private void listarAutores() {
        List<Autor> autores = servicioAutores.listarTodo();
        System.out.println("******************* Autores Registrados **************************");
        autores.forEach(Principal::mostrarAutor);
        System.out.println("******************* Fin Autores Registrados **************************");
    }

    private void listarLibros() {
        List<Libro> libros = servicioLibros.listarTodo();
        System.out.println("******************* Libros Registrados **************************");
        libros.forEach(Principal::mostrarLibro);
        System.out.println("******************* Fin Libros Registrados **************************");
    }

    private void buscarLibroPorTitulo() {
        ResponseApi result;
        while (true) {
            System.out.print("Ingrese el nombre del libro que desea buscar: ");
            var titulo = TECLADO.nextLine();
            String bodyResponse = ConsumoAPI.obtenerDatos(URL_BASE.concat("?search=").concat(titulo.replace(" ", "+")));
            result = ConvierteDatos.convertirJsonStringA(bodyResponse, ResponseApi.class);

            if (!result.totalLibros().equals(0)) break;

            System.out.println("El libro ".concat(titulo).concat(" no fue encontrado!"));
        }

        if (result.totalLibros() == 1) {
            registrarLibro(result.libros().getFirst());
            return;
        }

        elegirLibroParaRegistrar(result.libros(), result.totalLibros());
    }

    private void elegirLibroParaRegistrar(List<DatosLibro> libros, Integer totalLibros) {
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

    private void registrarLibro(DatosLibro datosLibro) {
        if (libroYaExiste(datosLibro.titulo())) return;

        System.out.println("Se guardará el libro ".concat(datosLibro.titulo()));
        while (true) {
            mostrarSiNo();
            opcion = TECLADO.nextLine();
            if (opcion.equals("2")) {
                System.out.println("¡No se guardó el libro!");
                return;
            }
            if (opcion.equals("1")) break;
            System.out.println("Elija 1 para guardar el registro o 2 para regresar");
        }

        guardarLibroYAutor(datosLibro);
    }

    private boolean libroYaExiste(String titulo) {
        var libroBuscado = servicioLibros.buscarPorTitulo(titulo);
        if (libroBuscado != null) {
            System.out.println("¡El libro ".concat(libroBuscado.getTitulo()).concat(" ya está registrado!"));
            mostrarLibro(libroBuscado);
        }
        return libroBuscado != null;
    }

    private void guardarLibroYAutor(DatosLibro datosLibro) {
        if (datosLibro.autores().isEmpty()) {
            datosLibro.autores().add(new DatosAutor("Sin autor", 0, 0));
        }

        System.out.println("Registrando libro en base de datos...");

        Libro nuevoLibro = new Libro(datosLibro);
        Autor autor = servicioAutores.buscarPorNombre(datosLibro.autores().getFirst().nombre());

        if (autor == null) {
            autor = new Autor(datosLibro.autores().getFirst());
        }

        nuevoLibro.setAutor(autor);
        autor.getLibros().add(nuevoLibro);

        servicioAutores.guardar(autor); // Salva también el libro porque está en Cascade.ALL

        System.out.println("Libro ".concat(nuevoLibro.getTitulo()).concat(" registrado satisfactoriamente!"));
        mostrarLibro(nuevoLibro);
    }

    private void mostrarSiNo() {
        System.out.print("""
                1 - Sí
                2 - No
                
                Elija la opción a través de su número:
                """);
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

    private static void mostrarLibroApi(DatosLibro libro) {
        System.out.printf("""
                
                ************* LIBRO *********************
                Título: %s
                Autor: %s
                Idioma: %s
                Número de descargas: %d
                ************* ***** *********************
                """,
                libro.titulo(), libro.autores().getFirst().nombre(),
                libro.idiomas().getFirst(), libro.numeroDescargas());
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

    private static List<String> obtenerIdiomas() {
        System.out.println("""
                **************************************************
                1 - es -> español
                2 - en -> inglés
                3 - fr -> francés
                4 - pt -> portugués
                5 - it -> italiano
                6 - fi -> finlandés
                
                0 - Regresar al menú anterior
                """);
        return List.of("es", "en", "fr", "pt", "it", "fi");
    }

    private void mostrarEstadistica(DoubleSummaryStatistics estadistica, String librosLabel) {
        System.out.printf("""
                ******************** Estadísticas de %s ******************************
                
                Cantidad de libros: %d
                Suma de descargas: %.0f
                Mínimo número de descargas: %.0f
                Máximo número de descargas: %.0f
                Promedio de descargas: %.2f
                
                ******************** Estadísticas de %s ******************************
                """,
                librosLabel,
                estadistica.getCount(),
                estadistica.getSum(),
                estadistica.getMin(),
                estadistica.getMax(),
                estadistica.getAverage(),
                librosLabel
        );
    }

}
