package br.com.magazine.controller;

import java.net.Authenticator.RequestorType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	public static final Logger log = Logger.getLogger(EventoController.class);

	@RequestMapping(value = "/evento", method = RequestMethod.GET, produces =MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Retorna uma lista de eventos.")
	public ResponseEntity<?> listaEvento() {
		log.debug("Listando evento");
		List<Evento> eventos = eventoRepository.findAll();
		return new ResponseEntity<>(eventos, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/evento/sala/{numeroSala}", method = RequestMethod.GET, produces =MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Retorna uma lista de eventos de uma sala.")
	public ResponseEntity<?> listaEventoSala(@PathVariable("numeroSala") String numeroSala) throws JSONException {
		log.debug("Listando evento por sala");
		JSONObject response = new JSONObject();
		List<Evento> eventos = eventoRepository.findByNumeroSala(numeroSala);
		if (!eventos.isEmpty()) {
			return new ResponseEntity<>(eventos, HttpStatus.OK);
		} else {
			response.put(MESSAGE, EVENTO_NAO_ENCONTRADO);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/evento/data/{dataInicio}", method = RequestMethod.GET, produces =MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Retorna uma lista de eventos de uma data.")
	public ResponseEntity<?> listaEventoData(@PathVariable("dataInicio") String dataInicio) throws JSONException {
		log.debug("Listando evento por data");
		JSONObject response = new JSONObject();
		List<Evento> eventos = eventoRepository.findByData(dataInicio);
		if (!eventos.isEmpty()) {
			return new ResponseEntity<>(eventos, HttpStatus.OK);
		} else {
			response.put(MESSAGE, EVENTO_NAO_ENCONTRADO);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/evento/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Retorna um único evento.")
	public ResponseEntity<?> listaEventoUnico(@PathVariable(value = "id") Long id) throws JSONException {
		log.info("Mostrando um único evento!");
		JSONObject response = new JSONObject();
		if (eventoExiste(id)) {
			Evento evento = eventoRepository.getOne(id);
			return new ResponseEntity<>(evento, HttpStatus.OK);
		} else {
			response.put(MESSAGE, EVENTO_NAO_ENCONTRADO);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/evento", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Salva um evento.")
	public ResponseEntity<?> salvarEvento(@RequestBody Evento evento) throws JSONException {
		log.info("Reservando uma sala para o evento " + evento.getTitle());
		log.info("Data Inicio: " + evento.getDataInicio());
		log.info("Data Fim: " + evento.getDataFim());
		JSONObject response = new JSONObject();
		if (!salaExiste(evento.getSalaNumero())) {
			response.put(MESSAGE, SALA_NAO_ENCONTRADA);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		List<Evento> eventos = eventoRepository.findByNumeroSala(evento.getSalaNumero());

		int qtdEventos = eventos.size();

		if (qtdEventos <= 0) {
			eventoRepository.save(evento);
			response.put(MESSAGE, EVENTO_SALVO);
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
		}

		if (qtdEventos > 0) {
			Boolean eventosIguais = null;
			// Search for eventos
			for (Evento evento2 : eventos) {
				eventosIguais = comparaEvento(evento2, evento);
			}
			if (eventosIguais) {
				response.put(MESSAGE, SALA_RESERVADA);
			} else {
				eventoRepository.save(evento);
				response.put(MESSAGE, EVENTO_SALVO);
				return new ResponseEntity<>(response.toString(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/evento/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Deleta um evento.")
	public ResponseEntity<?> deletaEvento(@PathVariable("id") Long id) throws JSONException {
		JSONObject response = new JSONObject();
		try {
			Evento evento = eventoRepository.getOne(id);
			log.info("Deletando o evento " + evento.getTitle());
			if (!eventoExiste(id)) {
				response.put(MESSAGE, EVENTO_NAO_ENCONTRADO);
				return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				Sala sala = salaRepository.findByNumero(evento.getSalaNumero());
				sala.setReservado(false);
				eventoRepository.delete(evento);
				response.put(MESSAGE, EVENTO_REMOVIDO);
				return new ResponseEntity<>(response.toString(), HttpStatus.OK);
			}
		} catch (Exception e) {
			response.put(MESSAGE, EVENTO_NAO_ENCONTRADO);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/evento/{id}")
	@RequestMapping(value = "/evento/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Atualiza um evento.")
	public ResponseEntity<?> atualizaEvento(@PathVariable("id") Long id, @RequestBody Evento evento)
			throws JSONException {

		JSONObject response = new JSONObject();
		if (salaExiste(evento.getSalaNumero()) & !salaReservada(evento.getSalaNumero())) {

			try {
				log.debug("Atualizando registro do evento " + evento.getTitle());
				Evento novoEvento = eventoRepository.getOne(id);
				novoEvento.setTitle(evento.getTitle());
				novoEvento.setDataInicio(evento.getDataInicio());
				novoEvento.setDataFim(evento.getDataFim());
				novoEvento.setSalaNumero(evento.getSalaNumero());
				eventoRepository.save(novoEvento);
				response.put(MESSAGE, EVENTO_ATUALIZADO);
				return new ResponseEntity<>(response.toString(), HttpStatus.OK);

			} catch (Exception e) {
				response.put(MESSAGE, EVENTO_NAO_ENCONTRADO);
				return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response.put(MESSAGE, ERRO_NAO_ATUALIZADO);
			return new ResponseEntity<String>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
