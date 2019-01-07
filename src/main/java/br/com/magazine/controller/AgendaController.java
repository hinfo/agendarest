package br.com.magazine.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.magazine.models.Agenda;
import br.com.magazine.repository.AgendaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(value = "API REST Magazine Luza")
@CrossOrigin(origins="*")
public class AgendaController {
	@Autowired
	AgendaRepository agendaRepository;
	
	@GetMapping("/agenda")
	@ApiOperation("Retorna uma lista de eventos agendados.")
	public List<Agenda> listaAgenda(){
		return agendaRepository.findAll();
	}
	
	@GetMapping("/agenda/{id}")
	@ApiOperation("Retorna um único evento agendado.")
	public Optional<Agenda> listaAgendamentoUnico(@PathVariable(value="id") Long id) {
		return agendaRepository.findById(id);
	}
	@PostMapping("/agenda")
	@ApiOperation("Salva um evento na agenda.")
	public Agenda salvarAgenda(@RequestBody Agenda agenda) {
		return agendaRepository.save(agenda);
	}
	@DeleteMapping("/agenda")
	@ApiOperation("Deleta um evento da agenda")
	public void deletaProduto(@RequestBody Agenda agenda) {
		agendaRepository.delete(agenda);
	}
	
	@PutMapping("/agenda")
	@ApiOperation("Atualiza um evento na agenda")
	public Agenda atualizaAgenda(@RequestBody Agenda agenda) {
		return agendaRepository.save(agenda);
	}
			
}
