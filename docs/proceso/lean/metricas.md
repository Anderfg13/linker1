# Lean: desperdicios, métricas y mejora aplicada (HU-07)

> Todo lo de este documento sale de datos reales del repositorio (GitHub) del 25–26 nov 2026.
> Las observaciones las redactó un asistente de IA a partir de esos datos: **el equipo debe confirmarlas, corregirlas o descartarlas** antes de la sustentación.

## T-07-01 — Desperdicios observados (con evidencia)

| # | Desperdicio | Dónde apareció | Efecto | Evidencia |
|---|---|---|---|---|
| 1 | **Esperas y defectos tardíos por CI que no corría**: `ci.yml` disparaba con la rama `DEV` (mayúsculas) pero la rama real es `develop`. | PR #2 y PR #5 se integraron a `develop` sin build, tests ni smoke test; solo corrió SonarCloud. | Riesgo de integrar cambios sin validar. | [PR #2](https://github.com/Anderfg13/linker1/pull/2), [PR #5](https://github.com/Anderfg13/linker1/pull/5) (solo el check de SonarCloud) |
| 2 | **Retrabajo por pruebas desactualizadas**: el smoke test y las verificaciones k6/Postman seguían esperando HTTP 301 cuando HU-03 cambió la redirección a 302. | PR #6 falló el *Smoke Test* y hubo que corregir `ci.yml` y `tests/k6/switchover-checks.js`; Postman se corrigió en otro PR. | Dos PR extra y espera. | [PR #6](https://github.com/Anderfg13/linker1/pull/6), [PR #7](https://github.com/Anderfg13/linker1/pull/7) |
| 3 | **Configuración inconsistente de nombres de rama**: la protección de rama se creó para `dev` y la rama de trabajo es `develop`. | `develop` quedó sin protección hasta que se detectó. | Posible push directo a la rama de integración. | Historial de este proceso; regla actual en Settings → Branches |

Son **hechos verificables en GitHub**; la interpretación del efecto es una hipótesis del equipo a confirmar.

## T-07-02 — Línea base de dos métricas

**Métrica 1 — Tiempo de PR abierto a integrado** (propuesta permitida por la guía). Datos de los 8 PR del 25–26 nov (hora de Bogotá):

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

Mediana: **8 min 30 s**. Limitación: son 8 PR en un solo día y varios son cambios pequeños; el PR #6 (la funcionalidad grande) incluye la noche entre el trabajo y la revisión.

**Métrica 2 — Ítems terminados por sprint.** Al 26 nov 2026 en Azure Boards (Sprint 1, 05/11–26/11) las tareas T-01-01…T-02-04 están *In Progress* y ninguna figura *Done*. **Línea base: 0 terminadas hasta ahora.** Actualizar este número cuando el equipo mueva las tareas a Done.

## T-07-03 — Mejora aplicada y resultado

- **Situación inicial:** desperdicio 1 (CI sin correr en `develop`).
- **Acción:** [PR #3](https://github.com/Anderfg13/linker1/pull/3) y [PR #4](https://github.com/Anderfg13/linker1/pull/4) cambiaron los disparadores de `DEV` a `develop`.
- **Resultado observado:** el [PR #6](https://github.com/Anderfg13/linker1/pull/6) ya ejecutó build, tests, empaquetado y smoke test, y el smoke test **detectó** la expectativa 301 obsoleta antes de integrar.
- **Qué falta medir:** repetir la métrica 1 en el siguiente sprint y registrar cuántos defectos detecta el CI antes de integrar. Nota: la mejora se aplicó dentro del mismo Sprint 1, no en el siguiente.
