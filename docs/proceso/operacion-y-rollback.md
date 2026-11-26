# Verificación operativa y rollback (T-08-04)

## Cómo verificar que la app funciona
- `GET /healthz` → `200 OK` si la app alcanza su MySQL; `503 Unhealthy: …` si no. Detalle en [`HEALTHCHECK.md`](../HEALTHCHECK.md). *Sin variables `MYSQL_*` (por ejemplo, demo local) la app arranca pero `/healthz` responderá 503: es esperado.*
- Prueba funcional mínima: crear un enlace (`POST /link`), abrirlo (`GET /{id}` → 302) y consultar `GET /link/{id}/stats`.
- Logs: la app escribe por stdout; con `LOG_LEVEL` se ajusta el detalle ([`LOGGING.md`](../LOGGING.md)). Los logs no incluyen la URL de destino.
- Despliegue blue/green en Oracle: la VM nueva pasa pruebas funcionales (k6) antes de recibir tráfico; si fallan, se destruye sin afectar producción ([`DEPLOYMENT.md`](../DEPLOYMENT.md)). Ese pipeline ya no corre solo: es manual y requiere secretos de OCI que este fork no tiene.
- Despliegue blue/green en AWS (el que usa este fork): [`DEPLOYMENT_AWS.md`](../DEPLOYMENT_AWS.md). Un balanceador (ALB) con dos grupos de destino; la VM nueva pasa k6 por un puerto de prueba, recibe 10 % y luego 100 % del tráfico, y la anterior se termina. El ALB comprueba `/` (no `/healthz`, para no depender de MySQL).
- Evidencia: un despliegue completo terminó en verde el 26 nov 2026 (14:20–14:26 Bogotá): [run 45882908020](https://github.com/Anderfg13/linker1/actions/runs/45882908020/job/140126393111).

## Cómo volver a la versión funcional
- **Oracle:** procedimiento manual y automatizado en [`ROLLBACK_STRATEGY.md`](../ROLLBACK_STRATEGY.md): volver al último JAR/tag publicado.
- **AWS, durante el despliegue:** si falla cualquier paso, el workflow ejecuta `scripts/aws/bluegreen.sh rollback`: devuelve el 100 % del tráfico a la versión actual y destruye la VM nueva.
- **AWS, después de promover:** la VM anterior ya se terminó, así que no hay marcha atrás instantánea. Se vuelve a la versión funcional ejecutando de nuevo **Blue/Green Deployment (AWS)** con el commit o tag anterior en el campo `ref`.
- Los enlaces están en RDS (si se configuró `MYSQL_PWD`), por lo que sobreviven al rollback. Con SQLite local, no.

## Ensayo
**No se ha ensayado ningún rollback** (ni el automático ni el de volver a un commit anterior). El camino de fallo del script no se ha probado contra AWS real. Debe anotarse aquí solo si el equipo lo ejecuta (fecha, quién, resultado).
