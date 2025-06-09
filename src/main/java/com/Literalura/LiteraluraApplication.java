package com.Literalura;

import com.Literalura.principal.Principal;
import com.Literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Autowired
	private LibroRepository repository;

	@Override
	public void run(String... args) {
		Principal principal = new Principal(repository);
		principal.mostrarMenu();
	}
}
