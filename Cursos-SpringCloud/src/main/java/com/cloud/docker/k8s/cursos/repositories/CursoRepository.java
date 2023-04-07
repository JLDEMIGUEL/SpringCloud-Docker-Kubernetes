package com.cloud.docker.k8s.cursos.repositories;

import com.cloud.docker.k8s.cursos.models.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {
}
