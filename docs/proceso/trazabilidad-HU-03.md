# Trazabilidad de una historia: HU-03 (T-08-06) y evidencia de CI/pruebas (T-08-02)

Cadena: **Azure Story → tareas → rama/PR → CI/tests → verificación end-to-end → versión/demo**.

| Eslabón | Evidencia |
|---|---|
| Historia en Azure Boards | HU-03 «Contar aperturas de forma persistente» (Sprint 1) en el [backlog](https://dev.azure.com/linker1/linker1/_backlogs/backlog/linker1%20Team/Backlog%20items). *Añadir aquí el ID del work item de Azure.* |
| Criterios de aceptación | [`AZURE_BOARDS_LINKER1.md`](../AZURE_BOARDS_LINKER1.md) (HU-03 y HU-04) |
| Tareas | T-03-01 migración, T-03-02 repositorio, T-03-03 redirección 302, T-03-04 pruebas; HU-04: T-04-01 endpoint `/link/{id}/stats`, T-04-02 UI |
| Desarrollo | Rama `feature/open-counts` → [PR #6](https://github.com/Anderfg13/linker1/pull/6) (autor Waldron63) |
| Código | `src/linker/LinkRepository.java` (`ensureClicksColumn`, `incrementClicks`, `findClicksById`), `src/linker/LinkService.java` (`open`), `src/linker/routes/LinkRoutes.java`, `public/app.js` |
| Revisión | Comentario «LGTM» de Anderfg13 en el PR #6 (ver limitación abajo) |
| CI del PR | Commit `f562217`: [Build](https://github.com/Anderfg13/linker1/actions/runs/45848076888/job/140006861310), [Tests](https://github.com/Anderfg13/linker1/actions/runs/45848076888/job/140007014243), [Smoke Test](https://github.com/Anderfg13/linker1/actions/runs/45848076888/job/140007692019), [SonarCloud](https://github.com/Anderfg13/linker1/runs/140007004340): todos en verde |
| Verificación end-to-end | Workflow temporal que levantó la app real y ejecutó 22 comprobaciones de HU-03/04 (migración, 302, `no-store`, HEAD/stats sin sumar, 404, 50 GET en paralelo, alias, logs sin URL): [resultado](https://github.com/Anderfg13/linker1/runs/140033474640) |
| Integración | Mezclado en `develop` el 26 nov 2026 |
| Demo / entrega | Pendiente: despliegue y grabación (ver [`proceso/README.md`](README.md)) |

## Qué comprueba la prueba relevante (T-08-02)

`LinkRoutesTest` levanta el servidor Javalin con SQLite en memoria y hace peticiones HTTP reales (sin seguir redirecciones):
- `eachGetAddsOneClick`: dos GET → el contador pasa de 0 a 2.
- `headAndStatsDoNotAddClicks`: HEAD y consultar `/stats` no suman.
- `statsForUnknownIdReturns404` / `getUnknownIdDoesNotCreateOrCountAnything`: un id inexistente da 404 y no crea nada.
- `LinkRepositoryTest.ensureClicksColumnMigratesLegacyTableKeepingRows`: una base antigua conserva sus filas y empieza en 0.

## Limitación (honesta) sobre revisión cruzada (T-08-01)

De los PR #1 a #8 del 25–26 nov (los #9 a #18 posteriores, casi todos de Anderfg13 y de infraestructura, no se revisaron aquí): 5 los abrió la cuenta Anderfg13 (#1, #3, #4, #7, #8) y esa misma cuenta los integró. En el #1 el único review es un «LGTM» del propio autor; los #3, #4, #7 y #8 no tienen review. De los PR de otras personas: el #2 (Natalia-Espitia) tiene un «LGTM» de Anderfg13 y lo integró Natalia; el #5 (Natalia-Espitia) no tiene review y lo integró Anderfg13; el #6 (Waldron63) tiene un «LGTM» de Anderfg13, quien también lo integró. Los «LGTM» no incluyen comentarios de detalle. La regla de la guía («ningún autor aprueba su propio PR» y comentarios útiles) **no se cumplió de forma consistente**. Para el siguiente sprint se propone exigir al menos un review de otra persona antes de integrar (activar «Require approvals» en la regla de `develop`).
