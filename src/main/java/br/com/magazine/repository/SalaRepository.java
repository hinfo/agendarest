package br.com.magazine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.magazine.models.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	public List<Sala> findAll();
	
	@Query("select s from sala s where numero = :numero")
	public Sala findByNumero(@Param("numero")String numero);
}
