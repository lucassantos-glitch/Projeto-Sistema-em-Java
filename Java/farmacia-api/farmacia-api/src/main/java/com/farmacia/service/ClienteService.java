package com.farmacia.service;

import com.farmacia.model.Cliente;
import com.farmacia.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	public Cliente criar(Cliente cliente) {
		if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
			throw new RuntimeException("CPF é obrigatório");
		}
		if (!cpfValido(cliente.getCpf())) {
			throw new RuntimeException("CPF inválido. Formato esperado: 000.000.000-00");
		}
		// Manter CPF formatado como 000.000.000-00
		String cpfFormatado = cliente.getCpf();
		cliente.setCpf(cpfFormatado);
		
		if (repository.existsByCpf(cpfFormatado)) {
			throw new RuntimeException("CPF já cadastrado");
		}
		if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
			throw new RuntimeException("E-mail é obrigatório");
		}
		if (repository.existsByEmail(cliente.getEmail())) {
			throw new RuntimeException("E-mail já cadastrado");
		}
		if (cliente.getDataNascimento() == null) {
			throw new RuntimeException("Data de nascimento é obrigatória");
		}
		java.time.LocalDate hoje = java.time.LocalDate.now();
		if (cliente.getDataNascimento().isAfter(hoje)) {
			throw new RuntimeException("Data de nascimento não pode ser no futuro");
		}
		int idade = java.time.Period.between(cliente.getDataNascimento(), hoje).getYears();
		if (idade < 18) {
			throw new RuntimeException("Cliente deve ter mais de 18 anos para realizar compras");
		}
		return repository.save(cliente);
	}

	// Validação de CPF (aceita apenas formato 000.000.000-00)
	private boolean cpfValido(String cpf) {
		// Validar formato: deve estar no padrão 000.000.000-00
		if (cpf == null || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
			return false;
		}
		// Remover formatação para validação do algoritmo
		String cpfLimpo = cpf.replaceAll("\\D", "");
		
		if (cpfLimpo.length() != 11 || cpfLimpo.chars().distinct().count() == 1) {
			return false;
		}
		// Validação dos dígitos verificadores
		try {
			int d1 = 0, d2 = 0;
			for (int i = 0; i < 9; i++) {
				int digito = Character.getNumericValue(cpfLimpo.charAt(i));
				d1 += digito * (10 - i);
				d2 += digito * (11 - i);
			}
			d1 = 11 - (d1 % 11);
			if (d1 >= 10) d1 = 0;
			d2 += d1 * 2;
			d2 = 11 - (d2 % 11);
			if (d2 >= 10) d2 = 0;
			return d1 == Character.getNumericValue(cpfLimpo.charAt(9)) && d2 == Character.getNumericValue(cpfLimpo.charAt(10));
		} catch (Exception e) {
			return false;
		}
	}

	public List<Cliente> listar() {
		return repository.findAll();
	}

	public Cliente buscarPorId(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("O id não pode ser nulo");
		}
		return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
	}

	@SuppressWarnings("null")
	public Cliente atualizar(Long id, Cliente cliente) {
		if (id == null) {
			throw new IllegalArgumentException("O id não pode ser nulo");
		}
		Cliente existente = repository.findById(id)
			.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
		
		if (cliente.getNome() != null && !cliente.getNome().trim().isEmpty()) {
			existente.setNome(cliente.getNome());
		}
		if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
			if (repository.existsByEmail(cliente.getEmail()) && !existente.getEmail().equals(cliente.getEmail())) {
				throw new RuntimeException("E-mail já cadastrado");
			}
			existente.setEmail(cliente.getEmail());
		}
		if (cliente.getDataNascimento() != null) {
			java.time.LocalDate hoje = java.time.LocalDate.now();
			if (cliente.getDataNascimento().isAfter(hoje)) {
				throw new RuntimeException("Data de nascimento não pode ser no futuro");
			}
			int idade = java.time.Period.between(cliente.getDataNascimento(), hoje).getYears();
			if (idade < 18) {
				throw new RuntimeException("Cliente deve ter mais de 18 anos");
			}
			existente.setDataNascimento(cliente.getDataNascimento());
		}
		
		return repository.save(existente);
	}

	public void deletar(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("O id não pode ser nulo");
		}
		repository.findById(id)
			.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
		repository.deleteById(id);
	}
}
    