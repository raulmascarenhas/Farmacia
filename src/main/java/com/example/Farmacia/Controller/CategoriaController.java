package com.example.Farmacia.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.Farmacia.Model.Categoria;
import com.example.Farmacia.Repository.CategoriaRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PostMapping
    public ResponseEntity<Categoria> criarCategoria(@Valid @RequestBody Categoria categoria) {
        if (categoriaRepository.existsById(categoria.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria já existe");
        }
        Categoria novaCategoria = categoriaRepository.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable Long id,
            @Valid @RequestBody Categoria categoriaAtualizada) {
        if (categoriaRepository.existsById(id)) {
            categoriaAtualizada.setId(id);
            Categoria categoriaAtualizadaSalva = categoriaRepository.save(categoriaAtualizada);
            return ResponseEntity.status(HttpStatus.OK).body(categoriaAtualizadaSalva);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    @PutMapping
    public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria) {
        if (categoriaRepository.existsById(categoria.getId())) {
            Categoria categoriaAtualizada = categoriaRepository.save(categoria);
            return ResponseEntity.status(HttpStatus.OK).body(categoriaAtualizada);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe");
    }
}