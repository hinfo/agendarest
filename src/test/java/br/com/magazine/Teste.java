package br.com.magazine;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Teste {
	
//	@Test
	public static void main(String[] args) {
		testes("01-02-2018");
	}
		private static void testes (String dataString) {
			System.out.println("Enter date for todo item:(like 04/20/2017):");
			LocalDate TodoDate = null;
			String DateString = dataString;
			try{
				TodoDate = LocalDate.parse(DateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			}
			catch(DateTimeParseException e){
				System.out.println("Date Format Error, Go Back Create Item.");
			}

			System.out.println("Data : " + TodoDate);
		}
	}
	
