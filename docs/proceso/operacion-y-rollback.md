# Verificación operativa y rollback (T-08-04)

## Cómo verificar que la app funciona
- `GET /healthz` → `200 OK` si la app alcanza su MySQL; `503 Unhealthy: …` si no. Detalle en [`HEALTHCHECK.md`](../HEALTHCHECK.md). *Sin variables `MYSQL_*` (por ejemplo, demo local) la app arranca pero `/healthz` responderá 503: es esperado.*
- Prueba funcional mínima: crear un enlace (`POST /link`), abrirlo (`GET /{id}` → 302) y consultar `GET /link/{id}/stats`.
- Logs: la app escribe por stdout; con `LOG_LEVEL` se ajusta el detalle ([`LOGGING.md`](../LOGGING.md)). Los logs no incluyen la URL de destino.
- Despliegue blue/green: la VM nueva pasa pruebas funcionales (k6) antes de recibir tráfico; si fallan, se destruye sin afectar producción ([`DEPLOYMENT.md`](../DEPLOYMENT.md)).

## Cómo volver a la versión funcional
Procedimiento manual y automatizado en [`ROLLBACK_STRATEGY.md`](../ROLLBACK_STRATEGY.md): volver al último JAR/tag publicado.

## Ensayo
**No se registra ensayo** de rollback en este documento: solo debe anotarse si el equipo lo ejecuta (fecha, quién, resultado).
