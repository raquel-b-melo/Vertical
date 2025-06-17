# API Vertical

API para gerenciar pedidos, usuários e produtos.

## Tecnologias Utilizadas

*   **Java (Spring Boot)**: Framework principal para desenvolvimento da API (inferido pelo uso do Maven e práticas comuns).
*   **Maven**: Ferramenta para gerenciamento de dependências e build do projeto.
*   **OpenAPI 3.0 (Swagger)**: Para design, documentação e especificação da API. O arquivo `swagger.yaml` no projeto detalha todos os endpoints.
*   **JWT (JSON Web Tokens)**: Para autenticação e autorização segura dos endpoints.
*   **Docker & Docker Compose**: Para containerização da aplicação e suas dependências, facilitando o setup e a execução em diferentes ambientes.

## Estrutura do Projeto

A API está organizada em torno dos seguintes recursos principais:

*   **Autenticação**:
    *   `POST /auth/register`: Registra um novo usuário.
    *   `POST /auth/login`: Autentica um usuário existente e retorna um token JWT.
*   **Arquivos**:
    *   `POST /files/upload`: Permite o upload de arquivos (requer autenticação).
*   **Pedidos**:
    *   `GET /search/order/{orderId}`: Busca informações do usuário associado a um pedido específico pelo ID do pedido.
    *   `GET /search/orders`: Lista todos os usuários e seus respectivos pedidos.
    *   `GET /search/orders/by-date`: Busca usuários e seus pedidos dentro de um intervalo de datas especificado.

## Como Rodar o Projeto

Siga os passos abaixo para executar a aplicação localmente:

1.  **Pré-requisitos**:
    *   JDK (versão compatível com o projeto, ex: 17 ou superior)
    *   Maven
    *   Docker
    *   Docker Compose

2.  **Abra o terminal na raiz do projeto.**

3.  **Compile e empacote a aplicação com Maven:**