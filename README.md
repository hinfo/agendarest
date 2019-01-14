<hr>
<h1>Projeto Luiza Labs</h1>
<hr>


<h2>Desenvolvimento API REST para realizar agendamentos</h2>

Requerimentos de Ambiente do Sistema:
    - Java 8 ou superior
    - SpringBoot 2.1 ou superior
    - Banco de Dados PostgreSQL
        - Descrição do banco de dados no arquivo src/main/resources/application.properties.
    - Maven 3.5 ou superior
    - Swagger (http://localhost:8080/swagger-ui.html)
    - log4J
        - Diretivas de logger no arquivo src/main/resources/log4j.properties.
    - Demais libs descritas no arquivo pom.xml do projeto.

Execução:
    - Acessar o diretório raiz do projeto e executar o comando:
        $ mvn spring-boot:run

Metodos API:
    URL:/api/evento
        metodo: GET
            - Retorna uma lista dos eventos cadastrados.
        metodo: POST
            - Adiciona um evento.
                - Request formato json de um evento.
                - Exemplo:
                <pre>{
                    "title": "teste",
                    "salaNumero": "01",
                    "dataInicio": "04-01-2019",
                    "dataFim": "04-01-2019"
                } </pre>
        
        
        URL:/api/evento/{id}
        metodo: GET
            - Retorna evento do ID informado.
            
        URL:/api/evento/sala/{numeroSala}
        metodo: GET
            - Retorna uma lista dos eventos da sala informada.
        
        URL:/api/evento/data/{dataIncio}
        metodo: GET
            - Retorna uma lista dos eventos na data informada.
        
        URL:/api/evento/{id}
        metodo: DELETE
            - Remove um evento.
        
        URL:/api/evento/{id}
        metodo: PUT
            - Atualiza um evento.
        
        URL:/api/sala
        metodo: GET
            - Retorna uma lista das salas cadastradas.
        
        URL:/api/sala/{id} (Pesquisa por ID)
        metodo: GET
            - Retorna dados de uma sala.
            
        URL:/api/sala/numero/{numeroSala} (Pesquisa por Númmero da sala)
        metodo: GET
            - Retorna dados de uma sala.
            
        URL:/api/sala
        metodo: POST
            - Adiciona uma sala.
        
        URL:/api/sala/{id} (Remove pelo ID)
        metodo: DELETE
            - Remove uma sala.
            
        URL:/api/sala/numero/{numeroSala} (Remove pelo numero da sala)
        metodo: DELETE
            - Remove uma sala.
        
        URL:/api/sala/{id} 
        metodo: PUT
            - Atualiza dados de uma sala.
