# 👤 Restaurant Microservice (Hexagonal Architecture)

Este microservicio permite la creación de restaurantes con rol **PROPIETARIO**, siguiendo una arquitectura **hexagonal**,
integrando seguridad, validaciones, y documentación OpenAPI/Swagger.

---

## 📋 Descripción

Este servicio expone un endpoint para registrar nuevos restaurantes. Solo los usuarios con rol `ADMIN` pueden crear Restaurantes y relacionarle el id del usuario si su rol es `PROPIETARIO`.  
El microservicio realiza validaciones como:

- Los nombres pueden contener números pero no ser solo númericos
- Validación de NIT permite solo números
- Longitud y formato del número telefónico
- Restricción de relación del id del usuario su rol debe ser solo "PROPIETARIO"

---

## 🚀 Tecnologías

- ☕ **Java 17**
- 🧱 **Spring Boot 3.5.5**
- 🔐 **Spring Security**
- 🧪 **Spring Validation**
- 🔄 **MapStruct**
- 📦 **MySQL**
- 📚 **Swagger/OpenAPI 3 (springdoc-openapi)**
- 🧪 **JUnit 5 + Jacoco (para cobertura)**
- ✅ **Arquitectura Hexagonal**

---

## 🛠️ Endpoints

| Método | Ruta       | Autenticación | Descripción         |
|--------|------------|---------------|---------------------|
| POST   | `/api/v1/` | `ADMIN`       | Crea un restaurante |

---
## 📖 Documentación de la API (Swagger)
http://localhost:8080/restaurant/swagger-ui/index.html

## 📄 Ejemplo de postman

#### Crear Restaurantes
```http
curl --location 'http://localhost:8080/restaurant/api/v1/' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AcGxhem9sZXRhLmNvbTpBZG1pbjEyMyE=' \
--header 'Cookie: JSESSIONID=C6B9E1BCEF2D968B43F002304CBF9726' \
--data '{
  "name": "Come bueno",
  "nit": "123454",
  "address": "Avenida siempre viva 123",
  "phoneNumber": "+571234567890",
  "urlLogo": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&psig=AOvVaw2g9G0Sld15LB9VVzwp1f5-&ust=1758314236215000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCICW7YyV448DFQAAAAAdAAAAABAE",
  "ownerId": 2
}'
```

## 📄 Ejemplo de solicitud

```http
POST /restaurant/api/v1/
Content-Type: application/json
Authorization: Basic base64(admin:password)

{
  "name": "Come bueno",
  "nit": "123454",
  "address": "Avenida siempre viva 123",
  "phoneNumber": "+571234567890",
  "urlLogo": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&psig=AOvVaw2g9G0Sld15LB9VVzwp1f5-&ust=1758314236215000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCICW7YyV448DFQAAAAAdAAAAABAE",
  "ownerId": 2
}
```

