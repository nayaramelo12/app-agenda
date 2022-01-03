package io.github.udemy.agendaapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.udemy.agendaapi.model.entity.Contato;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {

}
