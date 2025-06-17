# API Vertical

API para gerenciar pedidos, usuários e produtos.

## Tecnologias Utilizadas

*   **Java (Spring Boot)**: Framework principal para desenvolvimento da API, utilizando Java 17+.
*   **Maven**: Ferramenta para gerenciamento de dependências e build do projeto.
*   **Spring Data JPA / Hibernate**: Para persistência de dados e interação com o banco de dados.
*   **PostgreSQL**: Banco de dados relacional utilizado.
*   **OpenAPI 3.0 (Swagger)**: Para design, documentação e especificação da API.
*   **Spring Security**: Para autenticação e autorização baseada em JWT.
*   **JWT (JSON Web Tokens)**: Para autenticação e autorização segura dos endpoints.
*   **Docker & Docker Compose**: Para containerização da aplicação.
*   **Lombok**: Para reduzir código boilerplate em classes Java.
*   **JaCoCo**: Ferramenta para medição de cobertura de código por testes automatizados.

## Estrutura do Projeto

A API está organizada em torno dos seguintes recursos principais:

*   **Autenticação**:
    *   `POST /auth/register`: Registra um novo usuário.
    *   `POST /auth/login`: Autentica um usuário existente e retorna um token JWT.
*   **Arquivos**:
    *   `POST /files/upload`: Permite o upload de arquivos (requer autenticação).
*   **Pedidos**:
    *   `GET /search/order/{orderId}`: Busca informações de um pedido específico pelo ID do pedido.
    *   `GET /search/orders`: Lista todos os pedidos.
    *   `GET /search/orders/by-date`: Busca os pedidos dentro de um intervalo de datas especificado.

## Como Rodar o Projeto

Siga os passos abaixo para executar a aplicação localmente:

1.  **Pré-requisitos**:
    *   JDK (versão compatível com o projeto, ex: 17 ou superior)
    *   Maven
    *   Docker
    *   Docker Compose

2.  **Abra o terminal na raiz do projeto.**

3.  **Compile e empacote a aplicação com Maven sem os testes:**
    ```mvn clean package -DskipTests```
4.  **Suba os containers do docker com docker compose:**
    ```docker-compose up --build -d```
5.  **Teste os endpoints e em seguida derrube as imagens do docker para concluir**
    ```docker-compose down -v```