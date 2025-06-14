package com.nomEmpresa.nomProyecto.configuration.security;

import com.nomEmpresa.nomProyecto.configuration.CacheName;
import com.nomEmpresa.nomProyecto.modelos.Administrador;
import com.nomEmpresa.nomProyecto.repositorio.IAdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class Autenticacion implements UserDetailsService {

    @Autowired
    private IAdministradorRepository administradorRepository;


    @Override
    @Cacheable(value = "ADMINISTRADOR")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Busca el registro del administrador por su nombre
        Optional<Administrador> administrador = administradorRepository.findByNombre(username);

        return administrador.orElse(null);

    }
}
