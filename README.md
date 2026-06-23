**Nombre:** Samuel Gil  
**Materia:** Arquitecturas de Software (ARSW)  
**Institución:** Escuela Colombiana de Ingeniería

---

# Lab Redis — Arquitectura Orientada por Eventos

Implementación de un productor de eventos en Java usando **Redis Streams** como broker de mensajería. El sistema simula el dominio bancario definido en clase: cada vez que se registra una transferencia, se genera un evento `TransferenciaCreada` y se publica en el stream `banco.transferencias`, donde tres grupos de consumidores independientes (`fraude-group`, `notif-group`, `auditoria-group`) pueden procesarlo sin acoplarse entre sí.

---

## Requisitos

- Java 17
- Maven
- Docker

---

## Ejecución

**1. Levantar Redis**

```bash
docker-compose up -d
```

**2. Compilar el proyecto**

```bash
mvn compile
```

**3. Ejecutar el productor**

```bash
mvn exec:java
```

El productor solicita por consola la cuenta origen, cuenta destino y monto de cada transferencia. Cada evento publicado queda persistido en el stream de Redis con un ID único asignado por el broker.

**4. Verificar en Redis**

```bash
docker exec -it redis-eda redis-cli
```

```
XRANGE banco.transferencias - +
XLEN banco.transferencias
XINFO GROUPS banco.transferencias
```

---

## Evidencia

![Transferencias publicadas](images/pruebatransferencia.png)

Ejecución del productor por consola. Se ingresan dos transferencias manualmente indicando cuenta origen, cuenta destino y monto. El sistema genera el evento `TransferenciaCreada`, lo publica en el stream `banco.transferencias` y Redis devuelve el ID único asignado a cada mensaje.

---

![Eventos en Redis](images/eventos.png)

Verificación directa en Redis usando `redis-cli`. Se ejecutaron los siguientes comandos:

- `XRANGE banco.transferencias - +` — lista todos los eventos guardados en el stream, mostrando el ID de cada uno junto con sus campos (eventId, from, to, amount, currency, createdAt).
- `XLEN banco.transferencias` — devuelve el número total de eventos almacenados en el stream.
- `XINFO GROUPS banco.transferencias` — muestra los grupos de consumidores registrados (`fraude-group`, `notif-group`, `auditoria-group`), cuántos mensajes han leído y cuántos tienen pendientes de confirmar.

---

## Conclusión

El laboratorio permitió aplicar los conceptos de arquitectura orientada por eventos usando Redis Streams como broker liviano. Se demostró que un productor puede publicar eventos de negocio sin conocer quién los va a consumir, lo que reduce el acoplamiento entre servicios. El uso de grupos de consumidores garantiza que cada servicio (antifraude, notificaciones, auditoría) procese los eventos de forma independiente y con confirmación de lectura mediante `XACK`. Redis Streams resulta ser una alternativa viable a soluciones como Kafka para flujos de eventos con persistencia y retención.
