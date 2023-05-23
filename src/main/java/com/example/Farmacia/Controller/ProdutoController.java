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

import com.example.Farmacia.Model.Produto;
import com.example.Farmacia.Repository.ProdutoRepository;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@Valid @RequestBody Produto produto) {
        if (produtoRepository.existsById(produto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto já existe");
        }
        Produto novoProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @GetMapping("/nomes/{nome}")
    public ResponseEntity<List<Produto>> getProdutosPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> obterProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id,
            @Valid @RequestBody Produto produtoAtualizado) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isPresent()) {
            produtoAtualizado.setId(id);
            Produto produtoAtualizadoSalvo = produtoRepository.save(produtoAtualizado);
            return ResponseEntity.ok(produtoAtualizadoSalvo);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        produtoRepository.deleteById(id);
    }

    @PutMapping
    public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
        if (produtoRepository.existsById(produto.getId())) {
            return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Produto não existe");
    }
}