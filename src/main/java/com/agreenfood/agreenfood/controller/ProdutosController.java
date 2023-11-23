package com.agreenfood.agreenfood.controller;

import com.agreenfood.agreenfood.model.Produtos;
import com.agreenfood.agreenfood.repository.CategoriasRepository;
import com.agreenfood.agreenfood.repository.ProdutosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class ProdutosController {

    @Autowired
    private ProdutosRepository produtosRepository;

    @Autowired
    private CategoriasRepository categoriasRepository;

    @GetMapping
    public ResponseEntity<List<Produtos>> getAll() {

        return ResponseEntity.ok(produtosRepository.findAll());
    }

    @GetMapping("/{id}")
        public ResponseEntity<Produtos> getById(@PathVariable Long id) {
            return produtosRepository.findById(id)
                    .map(resposta -> ResponseEntity.ok(resposta))
                    .orElse(ResponseEntity.notFound().build());
        }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Produtos>> getByNome(@PathVariable String nome) {
        return ResponseEntity.ok(produtosRepository.findAllByNomeContainingIgnoreCase(nome));
    }

    @PostMapping
    public ResponseEntity<Produtos> post(@Valid @RequestBody Produtos produtos){
        if (categoriasRepository.existsById(produtos.getCategorias().getId()))
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(produtosRepository.save(produtos));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!", null);
    }


    @PutMapping
    public ResponseEntity<Produtos> put(@Valid @RequestBody Produtos produtos){
        if (produtosRepository.existsById(produtos.getId())) {
            if (categoriasRepository.existsById(produtos.getCategorias().getId()))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(produtosRepository.save(produtos));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!", null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ResponseStatus (HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Produtos> produtos = produtosRepository.findById(id);
        if (produtos.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        produtosRepository.deleteById(id);
    }

}
