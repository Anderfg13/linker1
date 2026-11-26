# Lean: desperdicios, métricas y mejora aplicada (HU-07)

> Todo lo de este documento sale de datos reales del repositorio (GitHub) del 25–26 nov 2026. Actualizado el 26 nov a las 14:30 (Bogotá) con los PR #9–#18.
> Las observaciones las redactó un asistente de IA a partir de esos datos: **el equipo debe confirmarlas, corregirlas o descartarlas** antes de la sustentación.

## T-07-01 — Desperdicios observados (con evidencia)

| # | Desperdicio | Dónde apareció | Efecto | Evidencia |
|---|---|---|---|---|
| 1 | **Esperas y defectos tardíos por CI que no corría**: `ci.yml` disparaba con la rama `DEV` (mayúsculas) pero la rama real es `develop`. | PR #2 y PR #5 se integraron a `develop` sin build, tests ni smoke test; solo corrió SonarCloud. | Riesgo de integrar cambios sin validar. | [PR #2](https://github.com/Anderfg13/linker1/pull/2), [PR #5](https://github.com/Anderfg13/linker1/pull/5) (solo el check de SonarCloud) |
| 2 | **Retrabajo por pruebas desactualizadas**: el smoke test y las verificaciones k6/Postman seguían esperando HTTP 301 cuando HU-03 cambió la redirección a 302. | PR #6 falló el *Smoke Test* y hubo que corregir `ci.yml` y `tests/k6/switchover-checks.js`; Postman se corrigió en otro PR. | Dos PR extra y espera. | [PR #6](https://github.com/Anderfg13/linker1/pull/6), [PR #7](https://github.com/Anderfg13/linker1/pull/7) |
| 3 | **Configuración inconsistente de nombres de rama**: la protección de rama se creó para `dev` y la rama de trabajo es `develop`. | `develop` quedó sin protección hasta que se detectó. | Posible push directo a la rama de integración. | Historial de este proceso; regla actual en Settings → Branches |

| 4 | **Pipelines heredados fallando en cada push**: `bluegreen.yml` (Oracle) se ejecutaba en cada push a `main` sin los secretos de OCI, y el job *API Tests (Live)* apuntaba al dominio del repositorio original (`1.n-la-c.app`), cuyo certificado venció. | Cada integración a `main` mostraba checks en rojo (*Provision green VM*, *Verify OCI_INSTANCE_OCID*, *API Tests (Live)*) que no tenían relación con el código. | Ruido que oculta fallos reales y pérdida de confianza en el CI. | Checks del commit [`f516f9c`](https://github.com/Anderfg13/linker1/commit/f516f9c3387154fe967c4285c7bcf2c8f027baab) en `main`; corrección en [PR #15](https://github.com/Anderfg13/linker1/pull/15) |
| 5 | **Defecto detectado tarde en la dependencia nueva**: al subir `sqlite-jdbc`/`mysql-connector-j`, el empaquetado en un solo jar dejó el driver de SQLite sin registrar (`No suitable driver found for jdbc:sqlite`). | Lo detectó el *Smoke Test* del [PR #17](https://github.com/Anderfg13/linker1/pull/17) antes de integrar. | Una iteración extra, pero sin llegar a `develop`. | PR #17 (commits de corrección) |

Son **hechos verificables en GitHub**; la interpretación del efecto es una hipótesis del equipo a confirmar.

## T-07-02 — Línea base de dos métricas

**Métrica 1 — Tiempo de PR abierto a integrado** (propuesta permitida por la guía). Datos de los PR del 25–26 nov (hora de Bogotá). No se cuentan los PR de promoción `develop` → `main` (#12, #14, #16, #18), que son automáticos de un clic.

| PR | Autor | Abierto → integrado | Duración |
|---|---|---|---|
| #1 chore: add Dependabot | Anderfg13 | 19:04 → 19:12 | 7 min 48 s |
| #2 secure link creation | Natalia-Espitia | 19:21 → 19:36 | 15 min 25 s |
| #3 ci: run CI on develop | Anderfg13 | 19:57 → 19:59 | 2 min 24 s |
| #4 ci: run CI on develop (main) | Anderfg13 | 19:57 → 19:59 | 2 min 8 s |
| #5 test reserved alias | Natalia-Espitia | 20:26 → 20:47 | 21 min 7 s |
| #6 open counts | Waldron63 | 00:17 → 10:22 (26 nov) | 10 h 4 min 45 s |
| #7 postman 302 | Anderfg13 | 10:26 → 10:28 | 1 min 58 s |
| #8 LaunchDarkly fallback | Anderfg13 | 10:34 → 10:43 | 9 min 12 s |
| #9 docs: evidencias del proceso | Anderfg13 | 11:24 → abierto | — |
| #10 AWS blue/green (infra + script + workflow) | Anderfg13 | 11:32 → 12:08 | 36 min 11 s |
| #11 RDS MySQL para persistir enlaces | Anderfg13 | 11:51 → 12:08 | 17 min 17 s |
| #13 IDs fijos de políticas CloudFront | Anderfg13 | 12:48 → 13:23 | 34 min 28 s |
| #15 CI esencial + Dependency-Check (SCA) | Anderfg13 | 13:29 → 13:35 | 6 min 28 s |
| #17 Corregir hallazgos de Dependency-Check | Anderfg13 | 13:48 → 14:10 | 22 min 33 s |

Mediana de los 13 PR integrados: **15 min 25 s** (con los 8 primeros era 8 min 30 s). Limitación: un solo día de datos; el PR #6 incluye la noche entre el trabajo y la revisión, y el aumento de la mediana se debe sobre todo a PR de infraestructura, más grandes y con más iteraciones del CI. No es una comparación antes/después de una mejora.

**Métrica 2 — Ítems terminados por sprint.** Al 26 nov 2026 en Azure Boards (Sprint 1, 05/11–26/11) las tareas T-01-01…T-02-04 están *In Progress* y ninguna figura *Done*. **Línea base: 0 terminadas hasta ahora.** Actualizar este número cuando el equipo mueva las tareas a Done.

## T-07-03 — Mejora aplicada y resultado

- **Situación inicial:** desperdicio 1 (CI sin correr en `develop`).
- **Acción:** [PR #3](https://github.com/Anderfg13/linker1/pull/3) y [PR #4](https://github.com/Anderfg13/linker1/pull/4) cambiaron los disparadores de `DEV` a `develop`.
- **Resultado observado:** el [PR #6](https://github.com/Anderfg13/linker1/pull/6) ya ejecutó build, tests, empaquetado y smoke test, y el smoke test **detectó** la expectativa 301 obsoleta antes de integrar.
- **Qué falta medir:** repetir la métrica 1 en el siguiente sprint y registrar cuántos defectos detecta el CI antes de integrar. Nota: la mejora se aplicó dentro del mismo Sprint 1, no en el siguiente.

### Segunda mejora aplicada (desperdicio 4)
- **Acción:** [PR #15](https://github.com/Anderfg13/linker1/pull/15) dejó el pipeline de Oracle solo en ejecución manual y condicionó *API Tests (Live)* a la variable `LIVE_BASE_URL`.
- **Resultado observado:** en el commit [`f516f9c`](https://github.com/Anderfg13/linker1/commit/f516f9c3387154fe967c4285c7bcf2c8f027baab) de `main` (antes) fallaban 3 checks ajenos al código; en el commit [`23c6b76`](https://github.com/Anderfg13/linker1/commit/23c6b76) (después) todos los checks quedaron en verde o *skipped* y ninguno en rojo.
- **Qué falta medir:** si el equipo configura `LIVE_BASE_URL`, comprobar que *API Tests (Live)* pase contra el despliegue en AWS.

