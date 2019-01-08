package br.com.magazine.exceptions;

public class SalaReservadaException extends Exception{
	
	private String numeroSala;
	
	public SalaReservadaException(String numeroSala) {
		super("Sala já está reservada ou não existe!");
		this.numeroSala = numeroSala;
	}

	public String getNumeroSala() {
		return numeroSala;
	}
	
	
}
