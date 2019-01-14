package br.com.magazine.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.magazine.models.Sala;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(value = "API REST LuizaLabs")
@CrossOrigin(origins = "*")
public class SalaController extends ControllerBase {


	private static final Logger log = Logger.getLogger(SalaController.class);

	@GetMapping("/sala")
	@ApiOperation("Retorna uma lista de salas.")
	public ResponseEntity<Iterable<Sala>> listaEvento() {
		log.debug("Listando sala");
		List<Sala> salas = salaRepository.findAll();
		return new ResponseEntity<>(salas, HttpStatus.OK);
	}

	@RequestMapping(value = "/sala/{id}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Retorna uma única sala.")
	public ResponseEntity<?> listaSalaUnica(@PathVariable(value = "id") Long id) throws JSONException, JsonProcessingException {
		log.info("Mostrando dados de uma sala!");
		String json = "";
		JSONObject response = new JSONObject();
		try {
			Sala sala = new Sala();
			ObjectMapper mapper = new ObjectMapper();
			sala = salaRepository.findByIdSala(id);
			
			if (sala != null) {
				json = mapper.writeValueAsString(sala);
				return new ResponseEntity<>(json, HttpStatus.OK);
			} else {
				response.put(MESSAGE, SALA_NAO_ENCONTRADA);
				return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			response.put(MESSAGE, SALA_NAO_ENCONTRADA);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	@RequestMapping(value = "/sala/numero/{numeroSala}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Retorna dados de uma sala através do numero da sala passado como parâmetro.")
	public ResponseEntity<?> listaSalaUnica(@PathVariable(value = "numeroSala") String numeroSala) throws JSONException, JsonProcessingException {
		log.info("Mostrando dados de uma sala!");
		JSONObject response = new JSONObject();
		String json = "";
		if (salaExiste(numeroSala)) {
			Sala sala = salaRepository.findByNumero(numeroSala);
			ObjectMapper mapper = new ObjectMapper();
			json = mapper.writeValueAsString(sala);
			return new ResponseEntity<>(json, HttpStatus.OK);
		} else {
			response.put(MESSAGE, SALA_NAO_ENCONTRADA);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/sala", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Adiciona uma sala.")
	public ResponseEntity<?> adicionarSala(@RequestBody Sala sala) throws JSONException {

		JSONObject response = new JSONObject();
		/**
		 * Validar dados recebidos
		 */
		if (sala == null) {
			response.put(MESSAGE, SALA_NAO_ENCONTRADA);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Adicionando uma sala " + sala.getNumero());
		if (salaExiste(sala.getNumero())) {
			log.info("Sala já existe, não será adicionada!");
			response.put(MESSAGE, SALA_EXISTE);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			System.out.println("" + sala.getNumero().isEmpty());
			if (!sala.getNumero().isEmpty() & sala.getReservado() != null) {
				log.info("Adicionada sala nº " + sala.getNumero());
				salaRepository.save(sala);
				response.put(MESSAGE, SALA_ADICIONADA);

			} else {
				response.put(MESSAGE, SALA_ERRO_SALVA);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			log.fatal("Sala sem identificação ou com dados inválidos, não será adicionada!");
			response.put(MESSAGE, SALA_ERRO_SALVA);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/sala/{id}", method = RequestMethod.DELETE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("Deleta uma sala através do ID.")
	public ResponseEntity<?> deletaSala(@PathVariable("id") Long id) throws JSONException {
		JSONObject response = new JSONObject();
		try {
			Sala sala = salaRepository.getOne(id);
			log.info("Deletando a sala " + sala.getNumero());
			salaRepository.delete(sala);
			response.put(MESSAGE, SALA_REMOVIDA);
			return new ResponseEntity<>(response.toString(), HttpStatus.OK);
		} catch (Exception e) {
			response.put(MESSAGE, SALA_NAO_ENCONTRADA);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/sala/numero/{numeroSala}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("Deleta uma sala através do numero da sala.")
	public ResponseEntity<?> deletaSalaNumero(@PathVariable("numeroSala") String numeroSala) throws JSONException {
		JSONObject response = new JSONObject();
		try {
			Sala sala = salaRepository.findByNumero(numeroSala);
			log.info("Deletando a sala " + sala.getNumero());
			salaRepository.delete(sala);
			response.put(MESSAGE, SALA_REMOVIDA);
			return new ResponseEntity<>(response.toString(), HttpStatus.OK);
		} catch (Exception e) {
			response.put(MESSAGE, SALA_NAO_ENCONTRADA);
			return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/sala/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation("Atualiza dados de uma sala.")
	public ResponseEntity<?> atualizaSala(@PathVariable("id") Long id, @RequestBody Sala sala)
			throws JSONException {

		JSONObject response = new JSONObject();
		if (salaExiste(sala.getNumero())) {

			try {
				log.debug("Atualizando registro de uma sala " +sala.getNumero());
				Sala newSala = salaRepository.findByNumero(sala.getNumero());
				newSala.setNumero(sala.getNumero());
				newSala.setReservado(sala.getReservado());
				salaRepository.save(newSala);
				response.put(MESSAGE, SALA_ATUALIZADA);
				return new ResponseEntity<>(response.toString(), HttpStatus.OK);

			} catch (Exception e) {
				response.put(MESSAGE, SALA_NAO_ENCONTRADA);
				return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response.put(MESSAGE, ERRO_NAO_ATUALIZADO);
			return new ResponseEntity<String>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
}
