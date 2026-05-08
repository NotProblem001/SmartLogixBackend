---

## 🚀 Inicio Rápido (Entorno Local)

### Requisitos Previos
*   **Java 21** o superior.
*   **Maven 3.9+** (o usar el wrapper `./mvnw`).
*   **Docker & Docker Compose** para servicios de infraestructura.

### Pasos para la Ejecución
1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/empresa/microservicio-backend.git](https://github.com/empresa/microservicio-backend.git)
    cd microservicio-backend
    ```

2.  **Levantar infraestructura de respaldo:**
    Inicia PostgreSQL y RabbitMQ mediante el archivo de orquestación:
    ```bash
    docker-compose up -d
    ```

3.  **Ejecutar la aplicación:**
    ```bash
    ./mvnw spring-boot:run
    ```
    > El servicio estará disponible en: `http://localhost:8080`

---

## 🧪 Estrategia de Pruebas
Dividimos las pruebas para optimizar el pipeline de CI/CD:

*   **Pruebas de Controlador:** Ejecución rápida mediante `@WebMvcTest`.
*   **Pruebas de Integración:** Uso de **Testcontainers** para levantar bases de datos reales efímeras durante los tests.
*   **Comando:** 
    ```bash
    ./mvnw test
    ```

---

## 🔒 Variables de Entorno y Configuración
Las siguientes variables rigen el comportamiento del servicio (configurables en `application.yml`):

| Variable | Descripción | Valor por Defecto |
| :--- | :--- | :--- |
| `DB_HOST` | URL del host de base de datos | `localhost:5432` |
| `DB_USER` | Usuario de PostgreSQL | `admin` |
| `JWT_SECRET` | Clave para tokens de autorización | *Requerido* |
| `RABBITMQ_URI` | URI de conexión al broker | `amqp://localhost` |

---

## 🤝 Lineamientos de Contribución
Para mantener la calidad del código, antes de enviar un **Pull Request**:
1.  Asegúrate de que el `README.md` esté actualizado si realizaste cambios arquitectónicos.
2.  Documenta nuevas APIs mediante **Swagger/OpenAPI**.
3.  Verifica que el código cumpla con las reglas de estilo y que el build sea¡Excelente base! He ajustado el texto para que tenga ese estilo profesional de GitHub, aplicando el formato **Markdown** adecuado, mejorando la jerarquía visual y añadiendo algunos toques técnicos que suelen apreciarse en repositorios de microservicios con Spring Boot.

Aquí tienes el contenido para tu `README.md`:

---

# 🚀 Microservicio Backend: [Nombre del Proyecto]

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-%23FF6600.svg?logo=rabbitmq&logoColor=white)

## 📖 Descripción General
Este repositorio contiene el microservicio de backend responsable de **[describir brevemente el propósito comercial del servicio, ej: la gestión del inventario y procesamiento de órdenes]**. 

El servicio opera dentro de un ecosistema distribuido, exponiendo una **API RESTful** robusta, empleando comunicación asincrónica a través de **RabbitMQ** y persistencia de datos en **PostgreSQL**.

---

## 🏗️ Arquitectura y Patrones Implementados
El proyecto se adhiere a patrones de desarrollo empresarial para asegurar escalabilidad y mantenibilidad:

*   **Arquitectura en Capas:** Separación clara entre Controladores, Servicios y Repositorios.
*   **Inyección de Dependencias:** Gestión de ciclo de vida de beans basada en constructores.
*   **DTOs y MapStruct:** Aislamiento total entre modelos de base de datos (`@Entity`) y contratos de respuesta JSON.
*   **Configuración Tipada:** Uso de `@ConfigurationProperties` para una gestión de propiedades segura y centralizada.
*   **Manejo Global de Excepciones:** Respuestas estandarizadas mediante `@ControllerAdvice`.

---

## 📂 Estructura de Directorios
El código sigue la convención de paquetes estándar de la industria:
```text
src/main/java/com/empresa/microservicio
├── config/         # Seguridad, Swagger y beans de configuración
├── controller/     # Enrutamiento semántico HTTP (@RestController)
├── dto/            # Data Transfer Objects (Requests/Responses)
├── exception/      # Lógica de @ExceptionHandler y excepciones personalizadas
├── model/          # Entidades JPA y clases de dominio
├── repository/     # Interfaces de Spring Data JPA
└── service/        # Lógica de negocio e implementaciones
