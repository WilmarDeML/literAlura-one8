package com.wilmardeml.literalura;

import com.wilmardeml.literalura.repositorios.AutorRepositorio;
import com.wilmardeml.literalura.repositorios.LibroRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	private final LibroRepositorio libroRepositorio;
	private final AutorRepositorio autorRepositorio;

	LiteraluraApplication(LibroRepositorio libroRepositorio, AutorRepositorio autorRepositorio) {
		this.libroRepositorio = libroRepositorio;
		this.autorRepositorio = autorRepositorio;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Principal.init(libroRepositorio, autorRepositorio);
	}
}
