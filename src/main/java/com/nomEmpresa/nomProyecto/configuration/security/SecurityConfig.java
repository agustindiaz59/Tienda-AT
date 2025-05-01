package com.nomEmpresa.nomProyecto.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private Autenticacion autenticacionService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity filters) throws Exception {
        return filters
                .authorizeHttpRequests( (request) -> request
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/admin/nuevo").permitAll()
                        .anyRequest().permitAll()
                )
                .userDetailsService(autenticacionService)
                //.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .httpBasic(Customizer.withDefaults())
                .build();
    }


    /**
     * Este bean se encarga de validar las credenciales del usuario (userDetails),
     * las solicita al userDetailsService y compara las contraseñas con el passwordEncoder
     *
     * @param userDetailsService servicio encargado de la busqueda en la base de datos del usuario
     * @param passwordEncoder objeto encargado de verificar si la contraseña encriptada coincide con la ingresada en el formulario de login
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
