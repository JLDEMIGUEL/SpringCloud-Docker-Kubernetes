package com.cloud.docker.k8s.cursos.services;

import com.cloud.docker.k8s.cursos.models.Usuario;
import com.cloud.docker.k8s.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Optional<Curso> porIdConUsuarios(Long id, String token);
    Curso guardar(Curso usuario);
    void eliminar(Long id);
    void eliminarCursoUsuarioPorId(Long id);

    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId, String token);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId, String token);
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId, String token);
}
