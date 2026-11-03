# EcoEnergia – Gestão de Consumo de Energia (ESG)

API RESTful para monitorar o consumo de energia elétrica de unidades corporativas (sedes, fábricas, centros de distribuição), acompanhar metas mensais, detectar picos de consumo e apoiar a governança ambiental (pilar "E" do ESG).


## Integrantes
| Amanda Sayuri Santos Uechi | RM566892 |
| Aniole Lopes Dos Santos Da Hora | RM567472 |
| Angelo Romano Delfino Dos Santos | RM567274 |
| Mariana Tenório Cavalcante Malvão | RM567541 |
| Grace Camara Dos Santos | RM567589 |

## Problema e solução
Empresas com várias unidades têm dificuldade de saber onde a energia está sendo desperdiçada. A EcoEnergia centraliza as medições de consumo dos dispositivos, compara o acumulado do mês com a meta definida para cada unidade, gera Alertas automáticos quando um dispositivo ultrapassa seu limite e entrega indicadores (percentual da meta atingido, quantidade de alertas) para apoiar a tomada de decisão.

## Tecnologias
- Java 21 · Spring Boot 4.1.1 (Web MVC, Data JPA, Validation, Security)
- Oracle Database (Oracle Free 23ai em contêiner)
- Flyway (migrations versionadas)
- Spring Security com HTTP Basic + BCrypt
- Docker e Docker Compose
- Maven · Lombok

## Como executar (Docker)

Pré-requisitos: Docker Desktop em execução (no Windows, com WSL 2) e as portas `8080` e `1521` livres.

```bash
# 1. Na raiz do projeto, crie o arquivo de variáveis
cp .env.password .env       

# 2. Suba o banco e a API
docker compose up --build
```

- Na primeira execução o Oracle leva de 1 a 3 minutos para ficar saudável. A API só inicia depois disso e o **Flyway** cria as tabelas, sequences, triggers e dados de teste automaticamente.
- A API fica disponível em **http://localhost:8080**.
- Teste rápido: abra `http://localhost:8080/unidades` no navegador.

| Ação | Comando |
|---|---|
| Subir em segundo plano | `docker compose up -d --build` |
| Ver logs da API | `docker compose logs -f api` |
| Ver status dos contêineres | `docker compose ps` |
| Parar (mantém os dados) | `docker compose down` |
| Apagar tudo, inclusive o banco | `docker compose down -v` |

### Variáveis de ambiente (`.env`)
| Variável | Descrição |
|---|---|
| `ORACLE_PASSWORD` | Senha do administrador (SYS/SYSTEM) do Oracle |
| `DB_USERNAME` | Usuário da aplicação (schema onde o Flyway cria as tabelas) |
| `DB_PASSWORD` | Senha do usuário da aplicação |

O compose monta `DB_URL=jdbc:oracle:thin:@//oracle:1521/FREEPDB1` para a API. O arquivo `.env` **não deve ser versionado**; use o `.env.password` como modelo.

### Como o Docker está organizado
- **`Dockerfile`** (multi-stage): o 1º estágio compila com Maven + JDK 21; o 2º roda só com JRE 21 Alpine, com usuário sem privilégios de root.
- **`docker-compose.yml`**: dois serviços, `oracle` (`gvenzl/oracle-free:23-slim-faststart`, com volume e healthcheck) e `api` (espera o banco ficar `healthy` antes de iniciar).
- **`.dockerignore`**: mantém `target/`, `.git/`, `.env` etc. fora da imagem.

### Problemas comuns
| Sintoma | Solução |
|---|---|
| `docker` não é reconhecido | Instale o Docker Desktop, reinicie o PC e reabra o terminal |
| Docker Desktop pede WSL / Virtual Machine Platform | Rode `wsl --install` como administrador e reinicie |
| Porta 8080 ou 1521 em uso | Encerre o programa que usa a porta ou altere o mapeamento no compose |
| API reinicia sem parar | Veja `docker compose logs api`; confira se o Oracle está `healthy` |

## Como executar sem Docker (opcional)
Requer Java 21 e um Oracle acessível. Defina as variáveis e rode:

```powershell
$env:DB_URL="jdbc:oracle:thin:@//localhost:1521/FREEPDB1"
$env:DB_USERNAME="ecoenergia"
$env:DB_PASSWORD="sua_senha"
.\mvnw spring-boot:run
```

## Endpoints

### Unidades
| Método | Rota | Descrição |
|---|---|---|
| POST | `/unidades` | Cadastra uma unidade |
| GET | `/unidades` | Lista as unidades |
| GET | `/unidades/{id}` | Busca uma unidade |
| PUT | `/unidades/{id}` | Atualiza uma unidade |
| DELETE | `/unidades/{id}` | Exclui uma unidade |
| GET | `/unidades/{id}/dispositivos` | Lista os dispositivos da unidade |
| GET | `/unidades/{id}/metas` | Lista as metas da unidade |
| GET | `/unidades/{id}/indicadores?mes=&ano=` | Indicadores do mês (sem parâmetros, usa o mês atual) |

### Dispositivos
| Método | Rota | Descrição |
|---|---|---|
| POST | `/dispositivos` | Cadastra um dispositivo |
| GET | `/dispositivos` | Lista os dispositivos |
| GET | `/dispositivos/{id}` | Busca um dispositivo |
| PUT | `/dispositivos/{id}` | Atualiza um dispositivo |

### Metas
| Método | Rota | Descrição |
|---|---|---|
| POST | `/metas` | Cadastra a meta mensal de uma unidade |
| PUT | `/metas/{id}` | Atualiza o valor máximo da meta |

### Consumos
| Método | Rota | Descrição |
|---|---|---|
| POST | `/consumos` | Registra uma medição (o custo estimado é calculado pelo sistema) |
| GET | `/consumos` | Lista as medições |
| GET | `/consumos/{id}` | Busca uma medição |
| PUT | `/consumos/{id}` | Atualiza uma medição |
| DELETE | `/consumos/{id}` | Exclui uma medição |

### Alertas
| Método | Rota | Descrição |
|---|---|---|
| GET | `/alertas` | Lista os alertas gerados |
| GET | `/alertas/{id}` | Busca um alerta |
| PATCH | `/alertas/{id}/resolver` | Marca o alerta como resolvido |

### Exemplos de corpo (JSON)

`POST /unidades`
```json
{
  "nmUnidade": "Filial Recife",
  "sgEstado": "PE",
  "nmCidade": "Recife",
  "dsSegmento": "Escritório Corporativo"
}
```

`POST /dispositivos`
```json
{
  "cdUnidade": 1,
  "nmDispositivo": "Ar-condicionado Sala 2",
  "dsTipoDispositivo": "Sistema Ar Central",
  "vlLimiteKwhHora": 60.00,
  "stDispositivo": "O",
  "dtInstalacao": "2024-05-10"
}
```

`POST /metas`
```json
{ "cdUnidade": 1, "nrMes": 5, "nrAno": 2026, "vlMetaMaxKwh": 5000.00 }
```

`POST /consumos`
```json
{ "cdDispositivo": 101, "dataMedicao": "2026-05-10", "kwhConsumido": 95.50 }
```

Uma coleção pronta para importar no Insomnia está em `insomnia/ecoenergia-collection-insomnia.yaml`.

## Regras de negócio e automações no banco
As regras abaixo são executadas por **triggers Oracle** (migration V3):

1. **Detecção de picos:** ao inserir um consumo acima do limite kWh/hora do dispositivo, um alerta é criado automaticamente.
2. **Atualização das metas:** cada consumo registrado soma no acumulado da meta mensal da unidade.
3. **Validação de sensores:** impede consumo negativo e dispositivo inexistente.
4. **Governança e auditoria:** impede alterar a meta de um mês já encerrado.

O custo estimado de cada consumo é `kWh × tarifa`, com a tarifa configurada em `ecoenergia.tarifa-kwh` (`application.properties`, padrão R$ 0,85/kWh).

### Tratamento de erros
Um `GlobalExceptionHandler` padroniza as respostas:

| Status | Quando |
|---|---|
| 400 | Dados inválidos (Bean Validation), JSON malformado, regra violada |
| 401 | Não autenticado em rota protegida |
| 404 | Recurso não encontrado |
| 409 | Violação de integridade no banco |
| 422 | Alteração de meta de mês encerrado |
| 500 | Erro inesperado |

## Segurança
- Autenticação **HTTP Basic** com Spring Security, sessão stateless e senhas com **BCrypt**.
- Rotas `GET` são públicas; `POST`, `PUT`, `PATCH` e `DELETE` exigem login.
- Usuário de desenvolvimento: `admin` / `admin123`.

No Insomnia: aba **Auth → Basic** e informe usuário e senha. Sem credenciais, as rotas protegidas retornam **401**.

## Banco de dados
Migrations em `src/main/resources/db/migration`, aplicadas pelo Flyway na inicialização:

| Versão | Conteúdo |
|---|---|
| V1 | Tabelas: unidade, dispositivo, meta de energia, consumo diário, alerta/incidente |
| V2 | Sequences de chaves primárias |
| V3 | Triggers de automação |
| V4 | Dados de teste (6 unidades, dispositivos, consumos e metas) |
| V5 | Tabela de usuários (`tb_esg_usuario`) |

> Nunca edite uma migration já aplicada; crie uma nova versão (V6, V7…).

Para acessar o banco em um cliente SQL (DBeaver, SQL Developer): host `localhost`, porta `1521`, service name `FREEPDB1`, usuário e senha do `.env`.

## Testes
```bash
./mvnw test
```
Os testes de repositório só executam quando `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` estão definidas (precisam do Oracle no ar). O build do Docker ignora os testes (`-DskipTests`).

## Estrutura do projeto
```
ecoenergia/
├── Dockerfile
├── docker-compose.yml
├── .env.password
├── insomnia/                       coleção de requisições
└── src/main/
    ├── java/br/com/fiap/ecoenergia/
    │   ├── config/                 SecurityConfig
    │   ├── controller/             endpoints REST
    │   ├── dto/                    records de entrada e saída
    │   ├── entity/                 entidades JPA
    │   ├── exception/              exceções e handler global
    │   ├── repository/             Spring Data JPA
    │   └── service/                regras de negócio
    └── resources/
        ├── application.properties
        └── db/migration/           scripts Flyway
```