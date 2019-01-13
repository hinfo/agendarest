package br.com.magazine.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import br.com.magazine.models.Evento;
import br.com.magazine.models.Sala;
import br.com.magazine.repository.EventoRepository;
import br.com.magazine.repository.SalaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(value = "API REST LuizaLabs")
@CrossOrigin(origins = "*")
public class EventoController extends ControllerBase {
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
		return new ResponseEntity<>(eventos, HttpStatus.OK);
	}

	@GetMapping("/evento/{id}")
	@ApiOperation("Retorna um único evento.")
	public ResponseEntity<String> listaEventoUnico(@PathVariable(value = "id") Long id) {
		log.info("Mostrando um único evento!");
		if (eventoExiste(id)) {
			Evento evento = eventoRepository.getOne(id);
			return new ResponseEntity<>("" + evento, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Evento não existe", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/evento")
	@ApiOperation("Salva um evento.")
	public ResponseEntity<String> salvarEvento(@RequestBody Evento evento) {
		log.info("Reservando uma sala para o evento " + evento.getTitle());
		log.info("Data Inicio: " + evento.getDataInicio());
		log.info("Data Fim: " + evento.getDataFim());

		String response = "";
		if (!salaExiste(evento.getSalaNumero())) {
			return new ResponseEntity<>("Sala não encontrada!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		List<Evento> eventos = eventos = eventoRepository.findByNumeroSala(evento.getSalaNumero());

		int qtdEventos = eventos.size();

		if (qtdEventos <= 0) {
			System.out.println("Sala disponível.");
			eventoRepository.save(evento);
			System.out.println("Salvando evento na sala " + evento.getSalaNumero());
			return new ResponseEntity<String>("Evento salvo!", HttpStatus.OK);
		}

		if (qtdEventos > 0) {
			System.out.println("Eventos nesta sala");
			Boolean eventosIguais = null;
			// Search for eventos
			for (Evento evento2 : eventos) {
				eventosIguais = comparaEvento(evento2, evento);
			}
			if (eventosIguais) {
				response = "A sala está reservada neste periodo!";
			} else {
				System.out.println("Disponível nesta data! Evento pode ser salvo!");
				eventoRepository.save(evento);
				return new ResponseEntity<String>("Evento salvo!", HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@DeleteMapping("/evento/{id}")
	@ApiOperation("Deleta um evento.")
	public ResponseEntity<String> deletaEvento(@PathVariable("id") Long id) {
		try {
			Evento evento = eventoRepository.getOne(id);
			log.info("Deletando o evento " + evento.getTitle());
			if (!eventoExiste(id)) {
				return new ResponseEntity<String>("Evento não encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				Sala sala = salaRepository.findByNumero(evento.getSalaNumero());
				sala.setReservado(false);
				eventoRepository.delete(evento);
				return new ResponseEntity<String>("Evento excluído", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Evento não encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/evento/{id}")
	@ApiOperation("Atualiza um evento.")
	public ResponseEntity<String> atualizaEvento(@PathVariable("id") Long id, @RequestBody Evento evento) {

		if (salaExiste(evento.getSalaNumero()) & !salaReservada(evento.getSalaNumero())) {

			try {
				log.debug("Atualizando registro do evento " + evento.getTitle());
				Evento novoEvento = eventoRepository.getOne(id);
				novoEvento.setTitle(evento.getTitle());
				novoEvento.setDataInicio(evento.getDataInicio());
				novoEvento.setDataFim(evento.getDataFim());
				novoEvento.setSalaNumero(evento.getSalaNumero());
				eventoRepository.save(novoEvento);
				return new ResponseEntity<String>("Evento atualizado!", HttpStatus.OK);

			} catch (Exception e) {
				return new ResponseEntity<String>("Evento não encontrado!", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<String>("Não foi possível atualizar o evento pois a sala informada não existe!",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

//	/**
//	 * Verifica se uma sala está reservada
//	 * 
//	 * @param numeroSala
//	 * @return true or false
//	 */
//	private Boolean salaReservada(String numeroSala) {
//		Boolean reservado = true;
//		if (salaExiste(numeroSala)) {
//			Sala sala = salaRepository.findByNumero(numeroSala);
//			reservado = sala.getReservado();
//			System.out.println("Sala id: " + sala.getId());
//		}
//		System.out.println("Reservada! " + reservado);
//		return reservado;
//	}
//
//	/**
//	 * Verifica se a sala existe
//	 * 
//	 * @param numeroSala
//	 * @return true or false
//	 */
//	private Boolean salaExiste(String numeroSala) {
//		Boolean existe = false;
//		Sala sala = salaRepository.findByNumero(numeroSala);
//		if (sala != null) {
//			existe = true;
//		}
//		return existe;
//	}
//
//	private Boolean eventoExiste(Long id) {
//		Boolean existe = false;
//		Optional<Evento> evt = eventoRepository.findById(id);
//		if (evt.isPresent()) {
//			existe = true;
//		}
//		return existe;
//	}
//
//	/**
//	 * Compara as datas de dois eventos, dois eventos não podem estar na mesma sala
//	 * no mesmo dia(Periodo). Um evento não pode começar enquanto outro estiver
//	 * terminando
//	 * 
//	 * @param evt1
//	 * @param evt2
//	 * @return true or false
//	 */
//	private Boolean comparaEvento(Evento evt1, Evento evt2) {
//		Boolean mesmoPeriodo = false;
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		String inicioEvt1 = evt1.getDataInicio();
//		String fimEvt1 = evt1.getDataFim();
//		String inicioEvt2 = evt2.getDataInicio();
//		String fimEvt2 = evt2.getDataFim();
//
//		LocalDate dataIncioEvt1 = LocalDate.parse(inicioEvt1, formatter);
//		LocalDate dataFimEvt1 = LocalDate.parse(fimEvt1, formatter);
//		LocalDate dataIncioEvt2 = LocalDate.parse(inicioEvt2, formatter);
//		LocalDate dataFimEvt2 = LocalDate.parse(fimEvt2, formatter);
//
//		if (dataIncioEvt1.isEqual(dataIncioEvt2)) {
//			mesmoPeriodo = true;
//		}
//		if (dataFimEvt1.isEqual(dataFimEvt2) || dataIncioEvt2.isEqual(dataFimEvt1)) {
//			mesmoPeriodo = true;
//		}
//		if (dataIncioEvt2.isAfter(dataIncioEvt1) & dataFimEvt2.isBefore(dataFimEvt1)
//				|| dataFimEvt1.isEqual(dataIncioEvt2) || dataIncioEvt2.isBefore(dataFimEvt1)) {
//			mesmoPeriodo = true;
//		}
//		return mesmoPeriodo;
//	}
}
