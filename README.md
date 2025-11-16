# RecrutamentoJusto - Plataforma de Recrutamento Justo com IA

**Integrantes do Grupo:**
- Guilherme Doretto Sobreiro - RM: 99674
- Guilherme Fazito Ziolli - RM: 550539
- Raí Gumieri dos Santos - RM: 98287

**Repositório GitHub:** [https://github.com/guidxs/RecrutamentoJusto](https://github.com/guidxs/RecrutamentoJusto)

---

## 📋 Sobre o Projeto

### Contexto e Problema

No mercado de trabalho atual, processos seletivos frequentemente sofrem com **vieses inconscientes** que prejudicam candidatos baseando-se em:
- Nome (indicando origem étnica/social)
- Idade e gênero
- Instituição de ensino
- Endereço/localização
- Aparência física

### Nossa Solução

O **RecrutamentoJusto** é uma plataforma que utiliza IA para promover processos seletivos mais **justos, inclusivos e imparciais**, focando exclusivamente em **competências técnicas e experiências profissionais relevantes**.

### 🚀 Como Funciona

```
1. CANDIDATURA ANÔNIMA
   ↓
   Dados sensíveis são ocultados (nome, idade, gênero, instituição, foto)

2. ANÁLISE POR IA
   ↓
   Sistema calcula score de compatibilidade (0-100)
   • Match de habilidades técnicas (60%)
   • Compatibilidade com área de atuação (40%)

3. DESAFIOS TÉCNICOS
   ↓
   Candidatos pré-selecionados recebem testes práticos personalizados

4. PRÉ-SELEÇÃO BASEADA EM MÉRITO
   ↓
   Apenas após ranking, dados sensíveis são liberados para o RH

5. ENTREVISTAS FOCADAS
   ↓
   RH realiza entrevistas baseadas em competências comprovadas
```

---

## 🛠️ Tecnologias Utilizadas

| Categoria | Tecnologia | Versão | Justificativa |
|-----------|------------|--------|---------------|
| **Linguagem** | Java | 17 | LTS, amplamente adotada no mercado |
| **Framework** | Spring Boot | 3.5.4 | Ecossistema completo para APIs REST |
| **Segurança** | Spring Security | 3.5.4 | Autenticação e autorização robustas |
| **Token** | JWT (Auth0) | 4.4.0 | Autenticação stateless |
| **Banco de Dados** | MySQL | 8.x | SGBD relacional robusto |
| **Migrations** | Flyway | - | Versionamento do banco |
| **Validação** | Jakarta Validation | - | Validação declarativa |
| **Build** | Maven | 3.8+ | Gerenciamento de dependências |

---

## 📂 Estrutura do Projeto

```
RecrutamentoJusto/
├── src/
│   ├── main/
│   │   ├── java/br/com/fiap/recrutamento_justo/
│   │   │   ├── model/              # Entities, VOs, Enums
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Candidato.java
│   │   │   │   ├── Vaga.java
│   │   │   │   ├── Aplicacao.java
│   │   │   │   ├── Desafio.java
│   │   │   │   ├── DadosSensiveisVO.java
│   │   │   │   ├── EnderecoVO.java
│   │   │   │   └── [Enums: UserRole, AreaAtuacao, StatusAplicacao, NivelSenioridade]
│   │   │   ├── dto/                # Data Transfer Objects
│   │   │   ├── controller/         # Endpoints REST
│   │   │   ├── service/            # Regras de negócio + IA
│   │   │   ├── repository/         # Repositórios JPA
│   │   │   ├── security/           # JWT, Filtros, Autenticação
│   │   │   ├── config/             # Configurações (CORS, Web)
│   │   │   └── exception/          # Tratamento global de exceções
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/       # Scripts Flyway
│   └── test/                       # Testes unitários e de integração
└── pom.xml
```

---

## 🏗️ Arquitetura em Camadas

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│  Controllers: AuthController, VagaController, etc.          │
│  Recebe requisições HTTP e retorna ResponseEntity<T>        │
└────────────────────────┬────────────────────────────────────┘
                         │ DTOs
┌────────────────────────▼────────────────────────────────────┐
│                      BUSINESS LAYER                         │
│  Services: VagaService, CandidatoService, AplicacaoService  │
│  • Validações e regras de negócio                           │
│  • Algoritmo de IA (score de compatibilidade)              │
│  • Controle de dados sensíveis                              │
└────────────────────────┬────────────────────────────────────┘
                         │ Entities
┌────────────────────────▼────────────────────────────────────┐
│                   PERSISTENCE LAYER                         │
│  Repositories: Spring Data JPA                              │
└────────────────────────┬────────────────────────────────────┘
                         │
                   ┌─────▼──────┐
                   │  MySQL DB  │
                   └────────────┘
```

---

## 🔐 Segurança e Autenticação

### Fluxo de Autenticação JWT

```
1. POST /auth/registro → Cria usuário com senha criptografada (BCrypt)
2. POST /auth/login → Valida credenciais e gera token JWT
3. Cliente usa token em requisições: Authorization: Bearer {token}
4. SecurityFilter valida token em cada requisição
5. Spring Security autoriza acesso baseado no perfil (role)
```

### Perfis de Usuário e Permissões

| Perfil | Permissões |
|--------|------------|
| **CANDIDATO** | • Criar/editar perfil<br>• Aplicar para vagas<br>• Responder desafios<br>• Ver próprias aplicações |
| **RH** | • Criar/gerenciar vagas<br>• Ver todos candidatos<br>• Liberar dados sensíveis<br>• Enviar desafios<br>• Avaliar candidaturas |
| **ADMIN** | • Todas as permissões<br>• Gerenciar usuários<br>• Acesso total ao sistema |

### Política STATELESS

- ✅ Sem `HttpSession` no servidor
- ✅ Token JWT auto-contido (inclui login, role, expiração)
- ✅ Validação em cada requisição
- ✅ Escalabilidade horizontal

---

## 🤖 Inteligência Artificial - Score de Compatibilidade

### Algoritmo de Matching

```java
private Integer calcularScoreCompatibilidade(Candidato candidato, Vaga vaga) {
    int score = 0;
    
    // 1. Match de Área de Atuação (40 pontos)
    if (candidato.getAreaInteresse() == vaga.getArea()) {
        score += 40;
    }
    
    // 2. Match de Habilidades Técnicas (60 pontos)
    long habilidadesMatch = candidato.getHabilidades().stream()
        .filter(vaga.getHabilidadesRequeridas()::contains)
        .count();
    
    double percentualMatch = (double) habilidadesMatch / vaga.getHabilidadesRequeridas().size();
    score += (int) (percentualMatch * 60);
    
    return Math.min(score, 100);
}
```

### Exemplo Prático

**Vaga:** Desenvolvedor Backend Java  
**Habilidades Requeridas:** `[Java, Spring Boot, MySQL, REST API, Git]`

**Candidato A:**
- Área: DESENVOLVIMENTO_BACKEND → +40 pontos
- Habilidades: `[Java, Spring Boot, MySQL, Docker]` → 3/5 = 60% → +36 pontos
- **Score: 76/100** ⭐⭐⭐⭐

---

## 🔧 Configuração e Instalação

### Pré-requisitos

- Java 17 ou superior
- MySQL 8.x
- Maven 3.8+

### Passo 1: Clonar o Repositório

```bash
git clone https://github.com/guidxs/RecrutamentoJusto.git
cd RecrutamentoJusto
```

### Passo 2: Configurar Banco de Dados

```sql
CREATE DATABASE recrutamento_justo;
```

### Passo 3: Configurar application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/recrutamento_justo
spring.datasource.username=root
spring.datasource.password=sua_senha
```

### Passo 4: Executar a Aplicação

```bash
# Compilar
mvn clean install

# Executar
mvn spring-boot:run
```

Aplicação disponível em: **http://localhost:8080**

---

## 🧪 Testando a API

### Endpoints Públicos

```bash
GET  http://localhost:8080/          # Informações da API
GET  http://localhost:8080/health    # Status do serviço
POST http://localhost:8080/auth/registro  # Registrar usuário
POST http://localhost:8080/auth/login     # Login
```

### Fluxo de Teste Completo

#### 1. Registrar Usuário RH

```http
POST http://localhost:8080/auth/registro
Content-Type: application/json

{
  "login": "rh@empresa.com",
  "senha": "senha123",
  "email": "rh@empresa.com",
  "role": "RH"
}
```

#### 2. Fazer Login

```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "login": "rh@empresa.com",
  "senha": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiration": 3600000
}
```

#### 3. Criar Vaga (com token)

```http
POST http://localhost:8080/vagas
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "titulo": "Desenvolvedor Java Senior",
  "descricao": "Vaga para desenvolvedor backend experiente",
  "area": "DESENVOLVIMENTO_BACKEND",
  "senioridade": "SENIOR",
  "habilidadesRequeridas": ["Java", "Spring Boot", "MySQL", "Docker"],
  "ativa": true
}
```

#### 4. Registrar Candidato

```http
POST http://localhost:8080/auth/registro
Content-Type: application/json

{
  "login": "candidato@email.com",
  "senha": "senha123",
  "email": "candidato@email.com",
  "role": "CANDIDATO"
}
```

#### 5. Criar Perfil de Candidato

```http
POST http://localhost:8080/candidatos
Authorization: Bearer {token_candidato}
Content-Type: application/json

{
  "dadosSensiveis": {
    "nomeCompleto": "João Silva",
    "idade": 28,
    "genero": "Masculino",
    "instituicaoEnsino": "FIAP",
    "telefone": "(11) 99999-9999"
  },
  "endereco": {
    "logradouro": "Rua Exemplo",
    "numero": "123",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  },
  "resumoProfissional": "Desenvolvedor backend com 5 anos de experiência",
  "habilidades": ["Java", "Spring Boot", "MySQL", "Docker", "Git"],
  "areaInteresse": "DESENVOLVIMENTO_BACKEND",
  "experienciasRelevantes": "Desenvolvedor na Empresa X por 3 anos",
  "anosCodificacao": 5
}
```

#### 6. Aplicar para Vaga

```http
POST http://localhost:8080/aplicacoes
Authorization: Bearer {token_candidato}
Content-Type: application/json

{
  "vagaId": 1,
  "cartaApresentacao": "Tenho grande interesse nesta posição..."
}
```

**A IA calculará automaticamente o score de compatibilidade!**

#### 7. Ver Ranking de Candidatos (RH)

```http
GET http://localhost:8080/aplicacoes/vaga/1/ranking
Authorization: Bearer {token_rh}
```

**Resposta:**
```json
[
  {
    "id": 1,
    "candidatoId": 1,
    "score": 88,
    "status": "EM_ANALISE",
    "analiseIA": "Excelente match! Candidato altamente qualificado.",
    "dadosSensiveisLiberados": false
  }
]
```

#### 8. Pré-Selecionar Candidato (libera dados sensíveis)

```http
PATCH http://localhost:8080/aplicacoes/1/status
Authorization: Bearer {token_rh}
Content-Type: application/json

{
  "novoStatus": "PRE_SELECIONADO"
}
```

**Agora o RH pode ver nome, idade, gênero, etc.**

### Usuários de Teste (senha: `123456`)

| Login | Role | Descrição |
|-------|------|-----------|
| admin | ADMIN | Administrador do sistema |
| rh_maria | RH | Recrutadora |
| candidato_joao | CANDIDATO | Candidato Full Stack |
| candidato_ana | CANDIDATO | Candidata Cientista de Dados |

---

## 📊 Modelo de Dados

### Entidades Principais

```
USUARIO (autenticação)
├── CANDIDATO (perfil + habilidades)
│   └── APLICACAO (candidatura + score IA)
│       └── DESAFIO (teste técnico)
└── VAGA (oportunidade de emprego)
```

### Value Objects

- **DadosSensiveisVO**: Nome, idade, gênero, instituição, foto
- **EnderecoVO**: Logradouro, número, cidade, estado, CEP

### Enums

- **UserRole**: CANDIDATO, RH, ADMIN
- **AreaAtuacao**: BACKEND, FRONTEND, FULLSTACK, MOBILE, DATA_SCIENCE, etc.
- **StatusAplicacao**: APLICADO, EM_ANALISE, PRE_SELECIONADO, APROVADO, etc.
- **NivelSenioridade**: JUNIOR, PLENO, SENIOR, ESPECIALISTA

---

### Detalhamento da Implementação

#### 1. Entities, VOs, Enums, Controllers, DTOs

**Entities:** Usuario, Candidato, Vaga, Aplicacao, Desafio  
**VOs:** DadosSensiveisVO, EnderecoVO  
**Enums:** UserRole, AreaAtuacao, StatusAplicacao, NivelSenioridade  
**Controllers:** Auth, Candidato, Vaga, Aplicacao, Desafio, Home  
**DTOs:** LoginDTO, RegistroDTO, TokenDTO, CandidatoDTO, VagaDTO, etc.

#### 2. ResponseEntity

```java
@PostMapping("/vagas")
public ResponseEntity<VagaDTO> criar(@RequestBody VagaDTO dto) {
    VagaDTO criada = vagaService.criar(dto);
    return ResponseEntity.status(201).body(criada);
}
```

#### 3. GlobalExceptionHandler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(new ErrorResponseDTO(...));
    }
    // + 5 outros handlers
}
```

#### 4. Autenticação

- `AuthenticationManager` para validar credenciais
- `TokenService` para gerar/validar JWT
- `BCryptPasswordEncoder` para senhas

#### 5. Autorização por Perfis

```java
// SecurityConfig.java
.requestMatchers(HttpMethod.POST, "/vagas").hasAnyRole("RH", "ADMIN")
.requestMatchers(HttpMethod.POST, "/candidatos").hasRole("CANDIDATO")

// Controller
@PreAuthorize("hasAnyRole('RH', 'ADMIN')")
public ResponseEntity<VagaDTO> criar(...)
```

#### 6. JWT STATELESS

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
}
```

#### 7. Serviços com Regras de Negócio

- **CandidatoService**: Controla visibilidade de dados sensíveis
- **VagaService**: Valida e gerencia vagas
- **AplicacaoService**: Calcula score IA, ranking, liberação de dados
- **DesafioService**: Envia e avalia testes técnicos

#### 8. Organização Modular

- **Separação clara de responsabilidades** (Controller → Service → Repository)
- **Injeção de dependências** via construtor
- **DTOs** para transferência de dados
- **VOs** para encapsular dados imutáveis

---

## 🎯 Diferenciais do Projeto

✨ **Inovação Social:** Combate vieses no recrutamento  
✨ **IA Aplicada:** Score automático de compatibilidade  
✨ **Segurança Robusta:** JWT + BCrypt + Autorização granular  
✨ **Código Limpo:** SOLID, Clean Architecture  
✨ **Dados Sensíveis Protegidos:** Liberados apenas após pré-seleção  
✨ **Migrations Flyway:** Versionamento de banco de dados  
✨ **Testes Automatizados:** JUnit + Mockito + H2  

---

## 🧪 Testes Automatizados

### Executar Testes

```bash
mvn test
```

### Relatório de Cobertura (JaCoCo)

```bash
mvn verify
```

Relatório em: `target/site/jacoco/index.html`

### Testes Implementados

- ✅ Testes Unitários (Services com Mockito)
- ✅ Testes de Integração (Controllers)
- ✅ Testes de Segurança (JWT, Autorização)

---

## 📝 Endpoints Principais

### Autenticação (Público)
- `POST /auth/registro` - Registrar usuário
- `POST /auth/login` - Login e token JWT

### Candidatos
- `POST /candidatos` - Criar perfil (CANDIDATO)
- `GET /candidatos/meu-perfil` - Ver perfil completo (CANDIDATO)
- `GET /candidatos/{id}` - Buscar candidato (dados sensíveis controlados)
- `POST /candidatos/{id}/liberar-dados` - Liberar dados (RH/ADMIN)

### Vagas
- `POST /vagas` - Criar vaga (RH/ADMIN)
- `GET /vagas` - Listar vagas ativas (público)
- `PATCH /vagas/{id}/desativar` - Desativar (RH/ADMIN)

### Aplicações
- `POST /aplicacoes` - Aplicar para vaga (CANDIDATO) - IA calcula score
- `GET /aplicacoes/vaga/{id}/ranking` - Ranking por score (RH/ADMIN)
- `PATCH /aplicacoes/{id}/status` - Atualizar status (RH/ADMIN)

### Desafios
- `POST /desafios` - Criar desafio (RH/ADMIN)
- `PATCH /desafios/{id}/responder` - Responder (CANDIDATO)
- `PATCH /desafios/{id}/avaliar` - Avaliar (RH/ADMIN)

---

**GitHub:** [github.com/guidxs/RecrutamentoJusto](https://github.com/guidxs/RecrutamentoJusto)

---

## 📄 Licença

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina SOA & WebServices da FIAP.

**Desenvolvido por:** Guilherme Doretto, Guilherme Fazito e Raí Gumieri - FIAP 2025
