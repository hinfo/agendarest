package br.com.magazine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.magazine.models.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	public List<Sala> findAll();
	
	@Query("select s from Sala s where id = :id")
	public Sala findByIdSala(@Param("id") Long id);
	
	@Query("select s from Sala s where numero = :numero")
	public Sala findByNumero(@Param("numero")String numero);
	
	
}
