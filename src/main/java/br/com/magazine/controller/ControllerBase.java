package br.com.magazine.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.magazine.models.Evento;
import br.com.magazine.models.Sala;
import br.com.magazine.repository.EventoRepository;
import br.com.magazine.repository.SalaRepository;

public class ControllerBase {
	@Autowired
	EventoRepository eventoRepository;

	@Autowired
	SalaRepository salaRepository;
	
	public static final String MESSAGE = "message";
	public static final String EVENTO_SALVO = "Evento salvo com sucesso!";
	public static final String EVENTO_REMOVIDO = "Evento excluído com sucesso";
	public static final String EVENTO_NAO_ENCONTRADO = "Evento não encontrado";
	public static final String EVENTO_ATUALIZADO = "Evento atualizado com sucesso";
	public static final String ERRO_NAO_ATUALIZADO = "Não foi possível atualizar o evento, sala informada não existe!";
	public static final String SALA_ADICIONADA = "Sala adicionada com sucesso.";
	public static final String SALA_REMOVIDA = "Sala deletada com sucesso.";
	public static final String SALA_ERRO_SALVA = "Sala não adicionada.";
	public static final String SALA_EXISTE = "Sala já existe.";
	public static final String SALA_RESERVADA = "Sala reservada.";
	public static final String SALA_NAO_ENCONTRADA = "Sala não encontrada.";
	public static final String SALA_ATUALIZADA = "Sala atualizada com sucesso.";
	
	
	
	/**
	 * Compara as datas de dois eventos, dois eventos não podem estar na mesma sala
	 * no mesmo dia(Periodo). Um evento não pode começar enquanto outro estiver
	 * terminando
	 * 
	 * @param evt1
	 * @param evt2
	 * @return true or false
	 */
	public Boolean comparaEvento(Evento evt1, Evento evt2) {
		Boolean mesmoPeriodo = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String inicioEvt1 = evt1.getDataInicio();
		String fimEvt1 = evt1.getDataFim();
		String inicioEvt2 = evt2.getDataInicio();
		String fimEvt2 = evt2.getDataFim();

		LocalDate dataIncioEvt1 = LocalDate.parse(inicioEvt1, formatter);
		LocalDate dataFimEvt1 = LocalDate.parse(fimEvt1, formatter);
		LocalDate dataIncioEvt2 = LocalDate.parse(inicioEvt2, formatter);
		LocalDate dataFimEvt2 = LocalDate.parse(fimEvt2, formatter);

		if (dataIncioEvt1.isEqual(dataIncioEvt2)) {
			mesmoPeriodo = true;
		}
		if (dataFimEvt1.isEqual(dataFimEvt2) || dataIncioEvt2.isEqual(dataFimEvt1)) {
			mesmoPeriodo = true;
		}
		if (dataIncioEvt2.isAfter(dataIncioEvt1) & dataFimEvt2.isBefore(dataFimEvt1)
				|| dataFimEvt1.isEqual(dataIncioEvt2) || dataIncioEvt2.isBefore(dataFimEvt1)) {
			mesmoPeriodo = true;
		}
		return mesmoPeriodo;
	}
	
	/**
	 * Verifica se a sala existe
	 * 
	 * @param numeroSala
	 * @return true or false
	 */
	public Boolean salaExiste(String numeroSala) {
		Boolean existe = false;
		Sala sala = salaRepository.findByNumero(numeroSala);
		if (sala != null) {
			existe = true;
		}
		return existe;
	}
	
	/**
	 * Verifica se um evento existe
	 * @param id
	 * @return
	 */
	public Boolean eventoExiste(Long id) {
		Boolean existe = false;
		Optional<Evento> evt = eventoRepository.findById(id);
		if (evt.isPresent()) {
			existe = true;
		}
		return existe;
	}
	
	/**
	 * Verifica se uma sala está reservada
	 * 
	 * @param numeroSala
	 * @return true or false
	 */
	public Boolean salaReservada(String numeroSala) {
		Boolean reservado = true;
		if (salaExiste(numeroSala)) {
			Sala sala = salaRepository.findByNumero(numeroSala);
			reservado = sala.getReservado();
		}
		return reservado;
	}
	
}
