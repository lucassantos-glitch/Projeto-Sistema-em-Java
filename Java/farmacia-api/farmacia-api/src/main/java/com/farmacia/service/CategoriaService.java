package com.farmacia.service;

import com.farmacia.model.Categoria;
import com.farmacia.repository.CategoriaRepository;
import com.farmacia.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public Categoria criar(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome da categoria é obrigatório");
        }
        if (repository.existsByNome(categoria.getNome())) {
            throw new RuntimeException("Categoria já existe");
        }
        return repository.save(categoria);
    }

    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo");
        }
        if (medicamentoRepository.existsByCategoriaId(id)) {
            throw new RuntimeException("Não é possível excluir categoria vinculada a medicamentos");
        }
        repository.deleteById(id);
    }

    public List<Categoria> listar() {
        return repository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo");
        }
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public Categoria atualizar(Long id, Categoria categoria) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo");
        }
        Categoria existente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        
        // Valida nome se foi enviado
        if (categoria.getNome() != null && !categoria.getNome().trim().isEmpty()) {
            // Verifica se o nome já existe em outra categoria
            if (!categoria.getNome().equalsIgnoreCase(existente.getNome()) && repository.existsByNome(categoria.getNome())) {
                throw new RuntimeException("Nome da categoria já existe");
            }
            existente.setNome(categoria.getNome().trim());
        }
        
        if (existente == null) {
            throw new RuntimeException("Categoria não pode ser nula");
        }
        return repository.save(existente);
    }
}
