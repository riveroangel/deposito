package com.deposito.gamasonic.service;

import com.deposito.gamasonic.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo ;

    public CustomUserDetailsService(UsuarioRepository usuarioRepo){
        this.usuarioRepo= usuarioRepo;

    }
    @Override
    public UserDetails loadUserByUsername (String username)
        throws UsernameNotFoundException{

        return usuarioRepo.findByUsername (username)
                .orElseThrow(()-> new UsernameNotFoundException("USUARIO NO ENCONTRADO"));

    }


}
