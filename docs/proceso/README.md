# Índice de evidencias del proceso (T-09-01)

Estado a 26 nov 2026, 14:30 (Bogotá). **«Pendiente» = el equipo debe aportarlo; no se debe simular.**

| Evidencia | Enlace / estado |
|---|---|
| Repositorio | https://github.com/Anderfg13/linker1 (fork de `co-eiv-devsecops/linker1`) |
| Tablero Azure Boards | [Backlog](https://dev.azure.com/linker1/linker1/_backlogs/backlog/linker1%20Team/Backlog%20items) · *verificar permisos de la docente* |
| Definición de historias y tareas | [`AZURE_BOARDS_LINKER1.md`](../AZURE_BOARDS_LINKER1.md) |
| PR e integración | [PR #1–#18](https://github.com/Anderfg13/linker1/pulls?q=is%3Apr) (los #12, #14, #16 y #18 son promociones `develop` → `main`) |
| CI | [Actions](https://github.com/Anderfg13/linker1/actions) · ejemplo: [PR #6](trazabilidad-HU-03.md) |
| Calidad (SonarCloud) | https://sonarcloud.io/project/overview?id=Anderfg13_linker1 |
| Trazabilidad de una historia | [HU-03](trazabilidad-HU-03.md) |
| Lean: desperdicios y métricas (HU-07) | [`lean/metricas.md`](lean/metricas.md) |
| Operación y rollback | [`operacion-y-rollback.md`](operacion-y-rollback.md) |
| Secretos, licencias, IA | [`revision-secretos-licencias-ia.md`](revision-secretos-licencias-ia.md) |
| Despliegue en AWS (blue/green, RDS, Terraform) | [`DEPLOYMENT_AWS.md`](../DEPLOYMENT_AWS.md) · despliegue verde: [run 45882908020](https://github.com/Anderfg13/linker1/actions/runs/45882908020/job/140126393111) · *no se ha ensayado rollback* |
| Análisis de dependencias (SCA) | [`SCA.md`](../SCA.md) · job *Dependency Check (SCA)* del CI · ejercicio del laboratorio *pendiente del equipo* |
| Feature flag `new-ui` (LaunchDarkly) | [`FEATURE_FLAGS.md`](../FEATURE_FLAGS.md) · flag creado en el entorno Test; *pendiente confirmar que la VM desplegada lo lee* |
| Quick Start | [`../QUICKSTART.md`](../QUICKSTART.md) |
| Definition of Done | [propuesta](definition-of-done.md) — *pendiente de acuerdo* |
| Validación con usuarios (HU-05) | [plantillas](cliente/README.md) — *pendiente: ≥ 2 usuarios reales* |
| Objetivo del producto, roles, Planning, Daily, Review, retrospectiva, 2 sprints (HU-06) | *pendiente: registros reales del equipo* |
| Informe (≤ 8 páginas), presentación 15+5, reflexiones individuales, video de respaldo (HU-09) | *pendiente* |
