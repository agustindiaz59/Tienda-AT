package com.nomEmpresa.nomProyecto;

import com.nomEmpresa.nomProyecto.dto.AdministradorDTO;
import com.nomEmpresa.nomProyecto.modelos.Administrador;
import com.nomEmpresa.nomProyecto.servicio.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class NomProyectoApplication implements CommandLineRunner {

	@Autowired
	private AdministradorService administradorService;

	public static void main(String[] args) {
		SpringApplication.run(NomProyectoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//TODO Sincronizar BBDD con archivos y carpetas en Wasabi

		// Crea un usuario por defecto el para acceder a las funciones
		AdministradorDTO defecto = new Administrador("admin", "as#gt15*p").getDto();

		administradorService.crearAdmin(defecto);

	}
}
