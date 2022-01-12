package io.github.udemy.agendaapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.udemy.agendaapi.model.Contato;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {
	
	List<Contato> findByNomeContainingIgnoreCase(String nome);

}
