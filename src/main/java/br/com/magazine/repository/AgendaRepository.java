package br.com.magazine.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.magazine.models.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long>{
	
	public Optional<Agenda> findById(Long id);
}
