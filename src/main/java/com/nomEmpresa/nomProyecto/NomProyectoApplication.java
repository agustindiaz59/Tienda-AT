package com.nomEmpresa.nomProyecto;

import com.nomEmpresa.nomProyecto.dto.AdministradorDTO;
import com.nomEmpresa.nomProyecto.modelos.Administrador;
import com.nomEmpresa.nomProyecto.modelos.DatosAuxiliares;
import com.nomEmpresa.nomProyecto.repositorio.IDatosAuxiliares;
import com.nomEmpresa.nomProyecto.servicio.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.PrintStream;
import java.util.Optional;

@SpringBootApplication
public class NomProyectoApplication implements CommandLineRunner {

	@Autowired
	private AdministradorService administradorService;

	@Autowired
	private IDatosAuxiliares datosAuxiliaresRepository;

	@Value("${usuarioPorDefecto}")
	private String usuarioDefecto;

	@Value("${clavePorDefecto}")
	private String claveDefecto;

	public static void main(String[] args) {
		SpringApplication.run(NomProyectoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//Inicio el archivo de log
		//System.setOut(new PrintStream("D:/log.txt"));
		//System.setErr(new PrintStream("D:/logErr.txt"));



		// Crea un usuario por defecto el para acceder a las funciones
		AdministradorDTO defecto = new Administrador(usuarioDefecto, claveDefecto).getDto();

		administradorService.crearAdmin(defecto);


		//Crea una instancia de datos para el negocio
		Optional<DatosAuxiliares> datos = datosAuxiliaresRepository.findById(1L);

		if(datos.isEmpty()){
			datosAuxiliaresRepository.save(new DatosAuxiliares());
		}

	}
}
