package com.cloud.docker.k8s.usuarios.services;

import com.cloud.docker.k8s.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);

}