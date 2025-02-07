# API de Gestión de Eventos

Esta es una API para la gestión de eventos, que permite realizar varias operaciones como la creación, actualización, venta y canje de boletos. La API está diseñada para ser sencilla de integrar con aplicaciones que necesiten administrar eventos y su correspondiente sistema de boletos.

## Funcionalidades

La API permite realizar las siguientes operaciones:

- **Crear eventos**: Permite crear nuevos eventos con detalles como nombre, fechas y número de boletos disponibles.
- **Editar eventos**: Permite actualizar la información de un evento.
- **Vender boletos**: Permite vender boletos de los eventos registrados, actualizando la cantidad de boletos disponibles y vendidos.
- **Canjear boletos**: Permite marcar los boletos vendidos como canjeados, asegurando que se haga dentro de los límites de tiempo establecidos para el evento.
- **Eliminar eventos**: Permite eliminar eventos solo si no tienen boletos vendidos o si la fecha de fin del evento ha pasado.

## Endpoints

### 1. Crear Evento

**POST** `/api/eventos/crear`

Crea un nuevo evento.

**Request body**:

```json
{
    "nombre": "Concierto Shakira 3",
    "fechaInicio": "2025-02-07",
    "fechaFin": "2025-02-08",
    "boletosDisponibles": 100
}
Response:

json
Copiar
{
    "id": 1,
    "nombre": "Concierto Shakira 3",
    "fechaInicio": "2025-02-07",
    "fechaFin": "2025-02-08",
    "boletosDisponibles": 100
}


2. Vender Boleto
POST /api/eventos/vender/{id}

Vende un boleto del evento especificado por id.

Response:

json
Copiar
{
    "boletosDisponibles": 99,
    "boletosVendidos": 1
}



3. Canjear Boleto
POST /api/eventos/canjear/{id}

Permite canjear un boleto del evento especificado por id, solo si el evento está dentro de las fechas de inicio y fin.



4. Obtener Eventos
GET /api/eventos/listar

Lista todos los eventos registrados en la base de datos.

Response:

json
Copiar
[
    {
        "id": 1,
        "nombre": "Concierto Shakira 3",
        "fechaInicio": "2025-02-07",
        "fechaFin": "2025-02-08",
        "boletosDisponibles": 100,
        "boletosVendidos": 0,
        "boletosCanjeados": 0
    },
    ...
]


Requerimientos
Java 11 o superior
Maven 3.6 o superior
Base de datos MySQL o compatible
Configuración


Clona este repositorio en tu máquina local:

bash
Copiar
git clone https://github.com/Erick3210/Prueba-T-cnica---Backend-Developer-nivel-Jr.git
Accede al directorio del proyecto:

bash
Copiar
cd gestorDeEventos
Configura tu base de datos y credenciales en el archivo application.properties.

Para ejecutar la aplicación, usa el siguiente comando:

bash
Copiar
mvn spring-boot:run
Consultas SQL y GraphQL
Las consultas SQL y GraphQL necesarias para la operación de la API están disponibles en los archivos:

Querys MySQL.txt
Querys GraphQL.txt
Asegúrate de revisarlas y adaptarlas a tu configuración.

Contribuciones
Si deseas contribuir a este proyecto, por favor abre un "pull request" con tus sugerencias o cambios.

