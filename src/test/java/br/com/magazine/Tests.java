package br.com.magazine;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests {

	
	String baseURI = "http://localhost:8080/api";

	@Test
	public void A_addSala() {
		System.out.println(baseURI + "/sala");
		String queryJson = "{\"numero\":\"15\",\"reservado\":false}";
		given().contentType("application/json").body(queryJson).when().post(baseURI + "/sala").then().statusCode(200)
				.body("message", containsString("Sala adicionada com sucesso."));
	}
	@Test
	public void B_listaSala() {
		given()
		.when().get(baseURI + "/sala/numero/15")
		.then()
		.statusCode(200)
		.body("numero", equalTo("15"))
		.body("reservado", equalTo(false));
	}

	@Test
	public void C_deletaSala() {
		given().when().delete(baseURI + "/sala/numero/15").then().statusCode(200).body("message",
				containsString("Sala excluída com sucesso."));
	};


}
