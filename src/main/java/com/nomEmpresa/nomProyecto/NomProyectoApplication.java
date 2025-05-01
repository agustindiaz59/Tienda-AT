package com.nomEmpresa.nomProyecto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class NomProyectoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(NomProyectoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//TODO Sincronizar BBDD con archivos y carpetas en Wasabi

	}
}
