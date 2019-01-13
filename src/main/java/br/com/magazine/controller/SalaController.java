package br.com.magazine.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.magazine.models.Sala;
import br.com.magazine.repository.SalaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(value = "API REST LuizaLabs ")
@CrossOrigin(origins = "*")
public class SalaController extends ControllerBase {

	@Autowired
	SalaRepository salaRepository;

	private static final Logger log = Logger.getLogger(SalaController.class);

	@GetMapping("/sala")
	@ApiOperation("Retorna uma lista de salas.")
	public ResponseEntity<Iterable<Sala>> listaEvento() {
		log.debug("Listando sala");
		List<Sala> salas = salaRepository.findAll();
		return new ResponseEntity<>(salas, HttpStatus.OK);
	}

	@GetMapping("/sala/{id}")
	@ApiOperation("Retorna uma única sala.")
	public ResponseEntity<String> listaSalaUnica(@PathVariable(value = "id") Long id) {
		log.info("Mostrando dados de uma sala!");
		Sala sala = new Sala();
		String response = "";
		try {
			sala = salaRepository.getOne(id);
			response = "" + sala;
			
		} catch (Exception e) {
			return new ResponseEntity<>("Sala não encontrada", HttpStatus.OK);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/sala/numero/{numeroSala}")
	@ApiOperation("Retorna dados de uma sala através do numero da sala passado como parâmetro.")
	public ResponseEntity<String> listaSalaUnica(@PathVariable(value = "numeroSala") String numeroSala) {
		log.info("Mostrando dados de uma sala!");
		if (salaExiste(numeroSala)) {
			Sala sala = salaRepository.findByNumero(numeroSala);
			return new ResponseEntity<>("" + sala, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Sala não existe", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/sala")
	@ApiOperation("Adiciona uma sala.")
	public ResponseEntity<String> adicionarSala(@RequestBody Sala sala) {

		String response = "";
		/**
		 * Validar dados recebidos
		 */
		if (sala == null) {
			return new ResponseEntity<String>("Sala sem identificação, Não será adicionada",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Adicionando uma sala " + sala.getNumero());
		if (salaExiste(sala.getNumero())) {
			log.info("Sala já existe, não será adicionada!");
			return new ResponseEntity<>("Sala já existe!", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			System.out.println("" + sala.getNumero().isEmpty());
//			System.out.println("" + sala.getReservado().equals(Boolean.class));
			if (!sala.getNumero().isEmpty() & sala.getReservado() != null) {
//				salaRepository.save(sala);
				log.info("Adicionada sala nº " + sala.getNumero());
				response = "Sala adicionada";
				
			} else {
				response = "Erro ao adicionar a sala";
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			log.fatal("Sala sem identificação ou com dados inválidos, não será adicionada!");
			return new ResponseEntity<String>("Sala com dados inválidos, Não será adicionada",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@DeleteMapping("/sala/{id}")
	@ApiOperation("Deleta uma sala.")
	public ResponseEntity<String> deletaSala(@PathVariable("id") Long id) {
		try {
			Sala sala = salaRepository.getOne(id);
			log.info("Deletando a sala " + sala.getNumero());
			salaRepository.delete(sala);
			return new ResponseEntity<String>("Sala excluída", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Erro: Sala não encontrada", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
