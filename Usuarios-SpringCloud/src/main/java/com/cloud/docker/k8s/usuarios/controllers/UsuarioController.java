package com.cloud.docker.k8s.usuarios.controllers;

import com.cloud.docker.k8s.usuarios.models.entity.Usuario;
import com.cloud.docker.k8s.usuarios.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UsuarioController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService service;

    @Autowired
    private ApplicationContext  context;

    @Autowired
    private Environment env;

    @GetMapping("/crash")
    public void crash(){
        ((ConfigurableApplicationContext) context).close();
    }

    @GetMapping("/")
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(Map.of("users", service.listar(), "podinfo",
                env.getProperty("MY_POD_NAME") + ": " + env.getProperty("MY_POD_IP"),
                "texto", env.getProperty("config.texto")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent())
            return ResponseEntity.ok(usuarioOptional.get());
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result){
        if (result.hasErrors()){
            return validar(result);
        }

        if(service.existePorEmail(usuario.getEmail())){
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un usuario con ese email"));
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar (@PathVariable Long id, @Valid @RequestBody Usuario usuario, BindingResult result){
        if (result.hasErrors()){
            return validar(result);
        }

        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()){
            Usuario usuarioDb = usuarioOptional.get();

            if(!usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) && service.existePorEmail(usuario.getEmail())){
                return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un usuario con ese email"));
            }

            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Usuario> optionalUsuario = service.porId(id);
        if (optionalUsuario.isPresent()){
            service.eliminar(id);
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.listarPorIds(ids));
    }


    @GetMapping("/authorized")
    public Map<String, String> authorized(@RequestParam String code){
        return Map.of("code", code);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginByEmail(@RequestParam String email){
        Optional<Usuario> o = service.porEmail(email);
        if (o.isPresent())
            return ResponseEntity.ok(o.get());
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
