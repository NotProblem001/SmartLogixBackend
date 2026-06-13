# Microservicio Backend/Componentes Frontend: SmartLogix

## Descripción General
**SmartLogix** es una plataforma tecnológica e infraestructura de software diseñada como una solución logística integral para pequeñas y medianas empresas (PYMEs). El propósito de este ecosistema es centralizar, asegurar y optimizar las operaciones de la cadena de suministro, incluyendo la gestión de almacenes e inventarios, el procesamiento transaccional de pedidos, el emparejamiento geodésico de cargas y transportistas en tiempo real (Haversine Proximity Engine), cobros electrónicos bancarios EFT y la sincronización resiliente de entregas mediante flujos offline-first.

---

## Arquitectura y Patrones Implementados
El sistema se rige bajo una arquitectura desacoplada y distribuida basada en microservicios, asegurando alta disponibilidad, escalabilidad horizontal y tolerancia a fallos:

- **Topología de Microservicios & BFF**: El sistema consta de tres microservicios de negocio (`ms-inventario`, `ms-pedidos` y `ms-envios`), un servidor de bases de datos PostgreSQL aislado y un **BFF / API Gateway** (`bff-gateway`) que actúa como único punto de entrada unificado y enrutador para el cliente.
- **API Gateway & Filtros de Seguridad**: La gateway enruta dinámicamente las solicitudes a los respectivos microservicios y valida de forma centralizada la cabecera `Authorization` utilizando un filtro de seguridad JWT personalizado (`JwtValidationFilter`).
- **Tolerancia a Fallos e Idempotencia**:
  - **Circuit Breaker (Resilience4j)**: Aplicado en las pasarelas del gateway para aislar fallas en el flujo de envíos e impedir la propagación de excepciones en cascada.
  - **Idempotencia Transaccional persistente**: Integración de base de datos JPA relacional en el servicio de pagos EFT (`EFTPaymentService`) para interceptar peticiones duplicadas y prevenir dobles cobros financieros bancarios.
- **Factory Method y Patrones Creacionales**: Empleado en el dominio y en la abstracción de clientes externos (como interfaces de pasarelas bancarias y simulación de transacciones).
- **Persistencia Aislada (Database per Service)**: Cada microservicio cuenta con su propio esquema de base de datos en PostgreSQL (`smartlogix_inventory`, `smartlogix_orders`, `smartlogix_shipping`), asegurando el desacoplamiento total del modelo de datos y el cumplimiento de principios de microservicios.

---

## Estructura de Directorios Resumida
A continuación se detalla la distribución estructural del código del ecosistema general:

```text
SmartLogix/
├── SmartLogixBackend/                   # Proyecto Backend (Spring Boot / Maven Multi-módulo)
│   ├── bff-gateway/                     # Gateway centralizador y BFF (Port 8080 -> 8085 en host)
│   │   └── src/main/resources/          # Configuraciones de enrutamiento y filtros JWT
│   ├── ms-inventario/                   # Microservicio de Inventario (Port 8081 -> 8086 en host)
│   ├── ms-pedidos/                      # Microservicio de Pedidos (Port 8082)
│   ├── ms-envios/                       # Microservicio de Envíos (Port 8083)
│   │   └── src/main/java/.../envios/
│   │       ├── client/                  # Clientes bancarios y mocks
│   │       ├── controller/              # Controladores REST de envíos y emparejamiento (JPA)
│   │       ├── entity/                  # Modelos JPA (Driver, ProcessedInvoice, etc.)
│   │       ├── repository/              # Interfaces JPA repositories
│   │       └── service/                 # Servicios (MatchingEngineService, EFTPaymentService)
│   ├── docker-compose.yml               # Orquestación de contenedores del backend y base de datos
│   └── pom.xml                          # Archivo de configuración Maven Padre (Reactor)
│
└── SmartLogixFrontend/                  # Proyecto Frontend (React / Vite / pnpm Workspaces)
    ├── src/
    │   ├── components/                  # Componentes reutilizables (OfflineDeliveryPanel, GlassPanel)
    │   ├── pages/                       # Vistas principales (ShippingPage, LoginPage)
    │   ├── services/                    # Servicios de API de comunicación (ShippingService, api config)
    │   └── context/                     # Contexto de autenticación y generación de JWT
    ├── package.json                     # Scripts y dependencias npm
    └── pnpm-workspace.yaml              # Configuración de workspace monorepo de pnpm
```

---

## Inicio Rápido (Entorno Local)

### Requisitos Previos
Asegúrese de contar con las siguientes herramientas instaladas en su sistema:
- **Java Development Kit (JDK) 17 o 21**
- **Node.js (Versión LTS)**
- **pnpm** (Gestor de paquetes de Node.js)
- **Docker & Docker Compose**
- **Maven 3.9+** (opcional, wrapper `./mvnw` incluido)

### Instrucciones Paso a Paso

#### 1. Clonar y Levantar los Contenedores con Docker
Navegue al directorio del backend y construya/levante todos los servicios (Base de datos PostgreSQL, Gateway, y los 3 microservicios) en segundo plano:
```bash
cd SmartLogixBackend
docker compose up --build -d
```

#### 2. Compilación y Ejecución de Pruebas en el Backend (Local)
Para compilar y testear los microservicios sin docker utilizando el wrapper de Maven:
```bash
# Definir la variable JAVA_HOME en la terminal
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.5" # Windows PowerShell
# Compilar y ejecutar pruebas de todo el proyecto
./mvnw clean test
```

#### 3. Levantar el Servidor de Desarrollo del Frontend
Navegue al directorio del frontend, instale las dependencias e inicie el servidor de desarrollo Vite:
```bash
cd ../SmartLogixFrontend
pnpm install
pnpm run dev
```
> La aplicación web estará disponible localmente en `http://localhost:5173`.

---

## Variables de Entorno y Configuración
Las siguientes variables configuran la conectividad de red, base de datos y llaves de cifrado en el ecosistema:

| Variable | Tipo / Origen | Puerto/Valor por Defecto | Descripción |
| :--- | :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | Backend (`ms-envios`) | `jdbc:postgresql://postgres:5432/smartlogix_shipping` | URL de conexión de la base de datos de envíos. |
| `SPRING_DATASOURCE_USERNAME`| Backend | `postgres` | Usuario administrador de la base de datos PostgreSQL. |
| `SPRING_DATASOURCE_PASSWORD`| Backend | `postgres_password` | Contraseña de la base de datos. |
| `JWT_SECRET` | Backend / Gateway | `defaultsecretkeyplaceholderformicros32chars` | Clave de firma simétrica HMAC-256 para validación de tokens. |
| `MS_ENVIOS_URI` | Gateway / BFF | `http://ms-envios:8083` | URI interna del microservicio de envíos dentro del clúster. |
| `VITE_API_URL` | Frontend | `http://localhost:8085/api/v1` | URL base que apunta al BFF/Gateway para el consumo de servicios. |

---

## Estrategia de Pruebas
Garantizamos la estabilidad y resiliencia del software mediante un pipeline de pruebas automatizadas con el objetivo de mantener un **mínimo de 60% de cobertura de código**:

- **Pruebas del Backend (JUnit 5 & Mockito)**:
  - Pruebas unitarias de algoritmos lógicos (Fórmula Haversine, ordenamiento por recencia y límites de capacidad) en `MatchingEngineServiceTest`.
  - Simulación y mocking de pasarelas de pago EFT externas y comportamiento ante Timeouts de comunicación (3 reintentos) en `EFTPaymentServiceTest`.
  - Cobertura de tests e informes generados mediante el plugin **JaCoCo** y vinculados con **SonarQube** para análisis estático de código.
- **Pruebas del Frontend (Vitest & React Testing Library)**:
  - Ejecución de pruebas asíncronas con mocks de peticiones HTTP en `ShippingService.test.jsx` y `api.test.jsx`.
  - Sincronización resiliente automática en bloque al detectar la restauración de la conexión de red.
