# Account Service

El `account-service` es un microservicio responsable de gestionar las cuentas de los usuarios, así como las transacciones asociadas. Permite la
creación, actualización, eliminación y consulta de cuentas y transacciones, además de la generación de reportes.

## Endpoints

### 1. Gestión de Cuentas

#### Obtener todas las cuentas (paginado)

- **Endpoint:** `/accounts`
- **Método:** `GET`
- **Descripción:** Obtiene una lista paginada de todas las cuentas registradas.

#### Obtener detalles de una cuenta específica

- **Endpoint:** `/accounts/{uuid}`
- **Método:** `GET`
- **Descripción:** Obtiene los detalles de una cuenta específica mediante su UUID.

#### Actualizar una cuenta

- **Endpoint:** `/accounts/{uuid}`
- **Método:** `PATCH`
- **Descripción:** Actualiza los detalles de una cuenta específica.

#### Crear una nueva cuenta

- **Endpoint:** `/accounts`
- **Método:** `POST`
- **Descripción:** Crea una nueva cuenta.

#### Eliminar una cuenta

- **Endpoint:** `/accounts/{uuid}`
- **Método:** `DELETE`
- **Descripción:** Elimina una cuenta específica.

#### Generar reporte de estado de cuenta

- **Endpoint:** `/accounts/reports`
- **Método:** `GET`
- **Descripción:** Genera un reporte de estado de cuenta en un rango de fechas para un cliente específico.

### 2. Gestión de Transacciones

#### Obtener todas las transacciones (paginado)

- **Endpoint:** `/transactions`
- **Método:** `GET`
- **Descripción:** Obtiene una lista paginada de todas las transacciones registradas.

#### Obtener detalles de una transacción específica

- **Endpoint:** `/transactions/{uuid}`
- **Método:** `GET`
- **Descripción:** Obtiene los detalles de una transacción específica mediante su UUID.

#### Crear una nueva transacción

- **Endpoint:** `/transactions`
- **Método:** `POST`
- **Descripción:** Crea una nueva transacción.

#### Actualizar una transacción

- **Endpoint:** `/transactions/{uuid}`
- **Método:** `PATCH`
- **Descripción:** Actualiza los detalles de una transacción específica.

#### Eliminar una transacción

- **Endpoint:** `/transactions/{uuid}`
- **Método:** `DELETE`
- **Descripción:** Elimina una transacción específica.

## Modelos de Datos

### AccountRequest

- **Descripción:** Modelo que representa la información de una cuenta.
- **Campos:**
    - `number`: `String` - Número de cuenta. No puede ser nulo.
        - **Validación:** `@NotNull(message = "number cannot be null", groups = {OnCreate.class})`
    - `type`: `String` - Tipo de cuenta. No puede ser nulo.
        - **Validación:** `@NotNull(message = "type cannot be null", groups = {OnCreate.class})`
    - `initialBalance`: `BigDecimal` - Saldo inicial de la cuenta. Debe ser mayor a 0.
        - **Validación:** `@Min(value = 1, message = "initial balance must be greater than 0", groups = {OnCreate.class})`
    - `status`: `Boolean` - Estado de la cuenta. No puede ser nulo.
        - **Validación:** `@NotNull(message = "status cannot be null", groups = {OnCreate.class})`
    - `customerUuid`: `UUID` - UUID del cliente asociado. No puede ser nulo.
        - **Validación:** `@NotNull(message = "customer_uuid cannot be null", groups = {OnCreate.class})`

### TransactionRequest

- **Descripción:** Modelo que representa la información de una transacción.
- **Campos:**
    - `date`: `LocalDateTime` - Fecha de la transacción. No puede ser nulo.
        - **Validación:** `@NotNull(message = "date cannot be null", groups = {OnCreate.class})`
    - `transactionType`: `String` - Tipo de transacción (Depósito, Retiro). No puede ser nulo.
        - **Validación:** `@NotNull(message = "transactionType cannot be null", groups = {OnCreate.class})`
    - `amount`: `BigDecimal` - Monto de la transacción. No puede ser nulo.
        - **Validación:** `@NotNull(message = "amount cannot be null", groups = {OnCreate.class})`
    - `accountUuid`: `UUID` - UUID de la cuenta asociada. No puede ser nulo.
        - **Validación:** `@NotNull(message = "account uuid cannot be null", groups = {OnCreate.class, OnUpdate.class})`

### AccountStatementReport

- **Descripción:** Modelo que representa el reporte de estado de cuenta.
- **Campos:**
    - `content`: Lista de reportes de clientes con sus cuentas y transacciones.
        - `customer`: Información del cliente.
            - `name`: Nombre del cliente.
            - `accounts`: Lista de cuentas asociadas al cliente.
                - `number`: Número de cuenta.
                - `type`: Tipo de cuenta (Ahorros, Corriente).
                - `initial_balance`: Saldo inicial de la cuenta.
                - `actual_balance`: Saldo actual de la cuenta.
                - `transactions`: Lista de transacciones asociadas a la cuenta.
                    - `balance`: Balance después de la transacción.
                    - `date`: Fecha de la transacción.
    - `pageable`: Información de paginación.
    - `last`: Indica si es la última página de resultados.
    - `total_elements`: Número total de elementos.
    - `total_pages`: Número total de páginas.
    - `size`: Tamaño de la página.
    - `number`: Número de la página actual.
    - `sort`: Información sobre el orden de los resultados.
    - `first`: Indica si es la primera página de resultados.
    - `number_of_elements`: Número de elementos en la página actual.
    - `empty`: Indica si la página está vacía.

## Validaciones

- La creación y actualización de cuentas y transacciones utiliza validaciones específicas definidas en los grupos `OnCreate` y `OnUpdate` para
  garantizar que los campos requeridos estén presentes y correctos.

## Ejemplos de Uso

### Crear una cuenta

```bash
POST /accounts
{
    "number": "123456789",
    "type": "AHORROS",
    "initialBalance": 1000.00,
    "status": true,
    "customerUuid": "abcd1234-ef56-7890-gh12-ijklmnopqrst"
}
