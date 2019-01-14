package br.com.magazine.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.magazine.models.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{
	
	public Optional<Evento> findById(Long id);
	
	@Query("select e from Evento e where sala_numero = :numeroSala")
	public List<Evento> findByNumeroSala(@Param("numeroSala") String numeroSala);
	
	@Query("select e from Evento e where data_inicio = :dataInicio")
	public List<Evento> findByData(@Param("dataInicio") String dataInicio);
}
