package br.com.magazine.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="TB_AGENDAMENTO")
@Data
public class Agenda implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String title;
	String sala;
	@Column(name = "data_inicio")
	String dataIncio;
	@Column(name="data_fim")
	String dataFim;
			

}
