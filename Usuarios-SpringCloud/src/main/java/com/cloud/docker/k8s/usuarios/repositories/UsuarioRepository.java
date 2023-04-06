package com.cloud.docker.k8s.usuarios.repositories;

import com.cloud.docker.k8s.usuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
}
