# Pass in (develop)

## Sobre o projeto
Pass In é um projeto backend desenvolvido sob orientação da [RocketSeat](https://www.rocketseat.com.br/).

Trata-se do backend de um sistema gerenciados de eventos presenciais. Onde é possível cadastrar eventos e adicionar participantes a cada evento.

## Funcionalidades
- Cadastrar um novo evento;
- Visualizar dados de um evento;
- Visualizar a lista de participantes;
- Listar todos os eventos;
- Inscrever participante em um evento;
- Visualizar crachá de inscrição do participante;
- Realizar check-in do participante no evento;
- Remover participante de um evento;
- Excluir um participante;
- Asssociar participantes a eventos;

## Regras de negócio
- O participante poderá se inscrever em mais de um evento;
- O participante não pode se increver mais de uma vez no mesmo evento;
- O participante só pode se inscrever em eventos com vagas disponíveis;
- O participante só pode realizar check-in em um evento uma única vez;

## Diagrama de banco de dados
![Image](https://github.com/MarcosWinicios/pass-in/blob/develop/docs/database-diagram.png?raw=true)

## Tecnologias utilizadas
- Java
- Lombok
- Spring Boot
- JPA / Hibernate
- Maven
- H2 Database
- Postman

## Como executar o projeto
Pré-requisitos: JDK 17


### Clonar repositório
```bash
git clone https://github.com/MarcosWinicios/pass-in.git
```
### Entrar na pasta do projeto back end
```bash
cd pass-in
```

### Executar o projeto
```bash
./mvnw spring-boot:run
```

### Como testar o projeto
- Baixe a [Collection Postman](https://github.com/MarcosWinicios/pass-in/blob/82b710dac532b25619e501aeb813901e86613d11/docs/pass-in.postman_collection.json) contendo todas as requisições para os endpoints disponíveis atualmente.
- Importe a collection baixada no [Postman](https://www.postman.com/).
- No menu **Enviroments** do Postman, configure uma variável de ambiente com o nome ```host``` e valor ```http://localhost:8080```.
- Com isso será possível realizar realizar requisições nos endpoints em ambiente local.

 
## Autor
Marcos Winicios Pereira Martins
https://www.linkedin.com/in/marcoswp/
