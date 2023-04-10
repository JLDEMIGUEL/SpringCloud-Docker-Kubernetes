package com.cloud.docker.k8s.cursos.controllers;

import com.cloud.docker.k8s.cursos.models.Usuario;
import com.cloud.docker.k8s.cursos.models.entity.Curso;
import com.cloud.docker.k8s.cursos.services.CursoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController("/")
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping("/")
    public List<Curso> listar(){
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Curso> cursoOptional = service.porIdConUsuarios(id);
        if(cursoOptional.isPresent())
            return ResponseEntity.ok(cursoOptional.get());
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result){
        if (result.hasErrors()){
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar (@PathVariable Long id, @Valid @RequestBody Curso curso, BindingResult result){
        if (result.hasErrors()){
            return validar(result);
        }

        Optional<Curso> cursoOptional = service.porId(id);
        if (cursoOptional.isPresent()){
            Curso cursoDb = cursoOptional.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> curso = service.porId(id);
        if (curso.isPresent()){
            service.eliminar(id);
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try{
            o = service.asignarUsuario(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe usuario por el id o error en la conexion: "+ e.getMessage()));
        }
        if (o.isPresent()){
            return  ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try{
            o = service.crearUsuario(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se pudo crear el usuario o error en la conexion: "+ e.getMessage()));
        }
        if (o.isPresent()){
            return  ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try{
            o = service.eliminarUsuario(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se pudo eliminar el usuario por id o error en la conexion: "+ e.getMessage()));
        }
        if (o.isPresent()){
            return  ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id){
            service.eliminarCursoUsuarioPorId(id);
            return ResponseEntity.noContent().build();
    }


    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
