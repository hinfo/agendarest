package br.com.magazine.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.magazine.exceptions.SalaReservadaException;
import br.com.magazine.models.Evento;
import br.com.magazine.models.Sala;
import br.com.magazine.repository.EventoRepository;
import br.com.magazine.repository.SalaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.bytecode.Descriptor.Iterator;

@RestController
@RequestMapping("/api")
@Api(value = "API REST Magazine Luza")
@CrossOrigin(origins = "*")
public class EventoController {
	@Autowired
	EventoRepository eventoRepository;

	@Autowired
	SalaRepository salaRepository;

	private static final Logger log = Logger.getLogger(EventoController.class);

	@GetMapping("/evento")
	@ApiOperation("Retorna uma lista de eventos.")
	public ResponseEntity<Iterable<Evento>> listaEvento() {
		log.debug("Listando evento");
		List<Evento> eventos = eventoRepository.findAll();
		return new ResponseEntity<>(eventos,HttpStatus.OK);
	}

	@GetMapping("/evento/{id}")
	@ApiOperation("Retorna um único evento.")
	public Optional<Evento> listaEventoUnico(@PathVariable(value = "id") Long id) {
		log.info("Mostrando um único evento!");
		return eventoRepository.findById(id);
	}

	@PostMapping("/evento")
	@ApiOperation("Salva um evento.")
	public ResponseEntity<String> salvarEvento(@RequestBody Evento evento) {
		log.info("Reservando uma sala para o evento " + evento.getTitle());
		Sala sala = salaRepository.findByNumero(evento.getSalaNumero());
		List<Evento> eventos = eventoRepository.findByNumeroSala(evento.getSalaNumero());
		LocalDate dataIncio = LocalDate.parse(evento.getDataIncio());
		LocalDate dataFim = LocalDate.parse(evento.getDataFim());
		String response = "";
		try {
			for (Evento evt : eventos) {
				if (LocalDate.parse(evt.getDataIncio()) != dataIncio & LocalDate.parse(evt.getDataFim()) != dataFim) {
					eventoRepository.save(evento);
					sala.setIsReservado(true);
					response = "Evento criado!";
				}
			}
			throw new SalaReservadaException(sala.getNumero());

		} catch (SalaReservadaException e) {
			response = e.toString();
		}
		return new ResponseEntity<String>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/evento")
	@ApiOperation("Deleta um evento.")
	public ResponseEntity<String> deletaEvento(@RequestBody Evento evento) {
		log.info("Deletando o evento " + evento.getTitle());
		eventoRepository.delete(evento);
		return new ResponseEntity<String>("Evento excluído", HttpStatus.OK);
	}

	@PutMapping("/evento")
	@ApiOperation("Atualiza um evento.")
	public ResponseEntity<String> atualizaEvento(@RequestBody Evento evento) {
		log.debug("Atualizando registro do evento " + evento.getTitle());
		eventoRepository.save(evento);
		return new ResponseEntity<String>("Evento atualizado!", HttpStatus.OK);
	}

}
