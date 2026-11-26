# Revisión de secretos, fuentes, licencias, reutilización y uso de IA (T-08-05)

## Secretos
- Búsqueda en el repositorio (`git grep`) de patrones de claves AWS, claves privadas, OCID de tenancy/usuario, API keys y SDK keys de LaunchDarkly: **sin coincidencias** (26 nov 2026).
- Archivos sensibles versionados (`.env`, `.tfvars`, `.pem`, `.key`, `.tfstate`, `.db`): **ninguno**. El `.gitignore` excluye `infra/terraform.tfvars`, `*.tfstate`, `*.env` y `*.db`.
- Los workflows leen credenciales de GitHub Secrets/Variables (`LD_SDK_KEY`, `OCI_*`, `MYSQL_*`, etc.); ninguna está en el código.
- Limitación: se revisó el árbol actual, **no** todo el historial de commits.

## Dependencias y licencias (según la información pública de cada proyecto; verificar antes de entregar)
| Dependencia | Licencia |
|---|---|
| Javalin, sqlite-jdbc, Jackson, OpenTelemetry, LaunchDarkly SDK, AWS Lambda libs | Apache-2.0 |
| Logback | EPL-1.0 / LGPL-2.1 |
| JUnit 5 | EPL-2.0 |
| Mockito | MIT |
| MySQL Connector/J | GPL-2.0 con «Universal FOSS Exception» (revisar que el uso del curso sea compatible) |

## Reutilización y fuentes
- El repositorio es un *fork* de `co-eiv-devsecops/linker1`; el código base y los workflows de despliegue provienen de ese repositorio.
- El equipo debe completar aquí cualquier fragmento copiado de otras fuentes (tutoriales, material del curso).

## Uso de IA (declaración)
- **Asistente de IA (Claude, de Anthropic), usado desde la cuenta de Anderfg13** el 25–26 nov 2026 para: configurar protección de ramas, agregar Dependabot (PR #1), corregir disparadores del CI (PR #3, #4), resolver conflictos del PR #6 y corregir las verificaciones 302 en CI/k6, actualizar Postman (PR #7), hacer opcional LaunchDarkly (PR #8), revisar las tareas T-01-01…T-02-04 contra sus criterios, ejecutar una verificación end-to-end de HU-03/04 y redactar estos documentos. Todo cambio pasó por PR y por el CI.
- GitHub Copilot aparece con un commit en el historial (PR #99 del repositorio base).
- **Cada integrante debe añadir su propio uso de IA y poder explicar el código que presenta.**
