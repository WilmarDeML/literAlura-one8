package com.wilmardeml.literalura;

import com.wilmardeml.literalura.servicios.serv.AutorServ;
import com.wilmardeml.literalura.servicios.serv.LibroServ;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	private final AutorServ servicioAutores;
	private final LibroServ servicioLibros;

	LiteraluraApplication(AutorServ servicioAutores, LibroServ servicioLibros) {
        this.servicioAutores = servicioAutores;
        this.servicioLibros = servicioLibros;
    }

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Principal programa = new Principal(servicioLibros, servicioAutores);
		programa.init();
	}
}
