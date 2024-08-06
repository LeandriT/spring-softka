# Customer Service

El `account-service` es un microservicio responsable de gestionar los usuarios. Permite la creación, actualización, eliminación y consulta de
clientes.

## Endpoints

### 1. Obtener todos los clientes (paginado)

- **Endpoint:** `/customers`
- **Método:** `GET`
- **Descripción:** Obtiene una lista paginada de todos los clientes registrados.
- **Respuesta:**
    - `200 OK`: Devuelve una lista paginada de objetos `CustomerDto`.

### 2. Obtener detalles de un cliente específico

- **Endpoint:** `/customers/{uuid}`
- **Método:** `GET`
- **Descripción:** Obtiene los detalles de un cliente específico mediante su UUID.
- **Respuesta:**
    - `200 OK`: Devuelve un objeto `CustomerDto` con los detalles del cliente.

### 3. Crear un nuevo cliente

- **Endpoint:** `/customers`
- **Método:** `POST`
- **Descripción:** Crea un nuevo cliente.
- **Request Body:** `CustomerRequest` (ver detalles en la sección de Modelos de Datos).
- **Respuesta:**
    - `201 CREATED`: Devuelve un objeto `CustomerDto` con los detalles del cliente creado.

### 4. Actualizar un cliente

- **Endpoint:** `/customers/{uuid}`
- **Método:** `PATCH`
- **Descripción:** Actualiza los detalles de un cliente específico.
- **Request Body:** `CustomerRequest` (ver detalles en la sección de Modelos de Datos).
- **Respuesta:**
    - `200 OK`: Devuelve un objeto `CustomerDto` con los detalles del cliente actualizado.

### 5. Eliminar un cliente

- **Endpoint:** `/customers/{uuid}`
- **Método:** `DELETE`
- **Descripción:** Elimina un cliente específico.
- **Respuesta:**
    - `204 NO CONTENT`: Elimina el cliente sin devolver contenido.

## Modelos de Datos

### CustomerRequest

- **Descripción:** Modelo que representa la información de un cliente.
- **Campos:**
    - `clientId`: `String` - ID del cliente.
    - `password`: `String` - Contraseña del cliente.
    - `status`: `Boolean` - Estado del cliente (activo/inactivo).
    - `name`: `String` - Nombre del cliente. No puede ser nulo.
        - **Validación:** `@NotNull(message = "Name cannot be null", groups = {OnCreate.class})`
    - `gender`: `String` - Género del cliente. No puede ser nulo.
        - **Validación:** `@NotNull(message = "Gender cannot be null", groups = {OnCreate.class})`
    - `age`: `Integer` - Edad del cliente. Debe ser mayor a 0.
        - **Validación:** `@Min(value = 1, message = "Age must be greater than 0", groups = {OnCreate.class})`
    - `identification`: `String` - Identificación del cliente. No puede ser nulo.
        - **Validación:** `@NotNull(message = "Identification cannot be null", groups = {OnCreate.class})`
    - `address`: `String` - Dirección del cliente. No puede ser nulo.
        - **Validación:** `@NotNull(message = "Address cannot be null", groups = {OnCreate.class})`
    - `phone`: `String` - Teléfono del cliente. No puede ser nulo.
        - **Validación:** `@NotNull(message = "Phone cannot be null", groups = {OnCreate.class})`

### CustomerDto

- **Descripción:** Modelo que representa la información detallada de un cliente.
- **Campos:**
    - `uuid`: `UUID` - Identificador único del cliente.
    - `clientId`: `String` - ID del cliente.
    - `name`: `String` - Nombre del cliente.
    - `gender`: `String` - Género del cliente.
    - `age`: `Integer` - Edad del cliente.
    - `identification`: `String` - Identificación del cliente.
    - `address`: `String` - Dirección del cliente.
    - `phone`: `String` - Teléfono del cliente.
    - `status`: `Boolean` - Estado del cliente.

## Validaciones

- La creación y actualización de clientes utiliza validaciones específicas definidas en el grupo `OnCreate` para garantizar que los campos requeridos
  estén presentes y correctos.

## Ejemplos de Uso

### Crear un cliente

```bash
POST /customers
{
    "clientId": "cb88b7cf-f2f3-40d5-b4b6-02b7935abf15",
    "password": "secretpassword",
    "status": true,
    "name": "Juan Maldonado",
    "gender": "MASCULINO",
    "age": 30,
    "identification": "1234567890",
    "address": "Av. Siempre Viva 123",
    "phone": "0991234567"
}
