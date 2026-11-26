# Revisión de secretos, fuentes, licencias, reutilización y uso de IA (T-08-05)

## Secretos
- Búsqueda en el repositorio (`git grep`) de patrones de claves AWS, claves privadas, OCID de tenancy/usuario, API keys y SDK keys de LaunchDarkly: **sin coincidencias** (26 nov 2026).
- Archivos sensibles versionados (`.env`, `.tfvars`, `.pem`, `.key`, `.tfstate`, `.db`): **ninguno**. El `.gitignore` excluye `infra/terraform.tfvars`, `*.tfstate`, `*.env` y `*.db`.
- Los workflows leen credenciales de GitHub Secrets/Variables; ninguna está en el código. Usadas por el despliegue en AWS: secretos `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_SESSION_TOKEN` (temporales de Learner Lab; se renuevan en cada sesión), `MYSQL_PWD`, `LD_SDK_KEY`, `NVD_API_KEY`; variable `LIVE_BASE_URL`.
- La clave de la base de datos RDS se pasa por línea de comandos a Terraform (`-var db_password=…`) y se guarda como secreto; **no** va en el repositorio. El estado de Terraform queda en el computador de quien lo aplica: ahora está excluido en `.gitignore` (`infra-aws/`), que antes solo cubría `infra/`.
- Se repitió la búsqueda de patrones de claves sobre los archivos nuevos (`infra-aws/`, `scripts/aws/`, workflows): sin coincidencias.
- Las credenciales de AWS y de LaunchDarkly se manejaron fuera del chat con la IA. Si alguna se pegó en algún sitio público, hay que rotarla.
- Limitación: se revisó el árbol actual, **no** todo el historial de commits.

## Dependencias y licencias (según la información pública de cada proyecto; verificar antes de entregar)
| Dependencia | Licencia |
|---|---|
| Javalin, sqlite-jdbc, Jackson, OpenTelemetry, LaunchDarkly SDK, AWS Lambda libs | Apache-2.0 |
| Logback | EPL-1.0 / LGPL-2.1 |
| JUnit 5 | EPL-2.0 |
| Mockito | MIT |
| MySQL Connector/J | GPL-2.0 con «Universal FOSS Exception» (revisar que el uso del curso sea compatible). Versión actual: 26.10.0 |
| kotlin-stdlib (transitiva de Javalin) | Apache-2.0 (verificar) |
| Terraform y proveedor AWS (herramienta, no se distribuye) | Revisar si el equipo decide redistribuirlo |

## Vulnerabilidades en dependencias (SCA)
- Se integró OWASP Dependency-Check siguiendo el laboratorio de la clase ([`SCA.md`](../SCA.md)). Falla el CI con CVSS ≥ 7.
- Primer resultado (26 nov 2026): hallazgos en `sqlite-jdbc`, `mysql-connector-j`, `kotlin-stdlib` y Jetty 11 (transitiva de Javalin). Se actualizaron las tres primeras; Jetty 11 se **suprimió con vencimiento el 2027-03-01** porque no existe un Jetty 11 corregido en Maven Central. La supresión es una decisión de riesgo que el equipo debe aceptar o revertir (migrar a Javalin 7).
- Las actualizaciones pasaron el CI, pero **no se ha hecho una prueba manual completa** de la app con las versiones nuevas.
- El ejercicio del laboratorio (rama con `commons-text` vulnerable, corrección y supresión) **no se ha hecho**: debe hacerlo el equipo.

## Reutilización y fuentes
- El repositorio es un *fork* de `co-eiv-devsecops/linker1`; el código base y los workflows de despliegue provienen de ese repositorio.
- El equipo debe completar aquí cualquier fragmento copiado de otras fuentes (tutoriales, material del curso).

## Uso de IA (declaración)
- **Asistente de IA (Claude, de Anthropic), usado desde la cuenta de Anderfg13** el 25–26 nov 2026 para: configurar protección de ramas, agregar Dependabot (PR #1), corregir disparadores del CI (PR #3, #4), resolver conflictos del PR #6 y corregir las verificaciones 302 en CI/k6, actualizar Postman (PR #7), hacer opcional LaunchDarkly (PR #8), diseñar y escribir el despliegue blue/green en AWS con Terraform, un script y un workflow (PR #10, #11, #13), guiar paso a paso su puesta en marcha en Learner Lab, integrar Dependency-Check y corregir sus hallazgos (PR #15, #17), simplificar los workflows heredados, revisar las tareas T-01-01…T-02-04 contra sus criterios, ejecutar una verificación end-to-end de HU-03/04 y redactar estos documentos. Todo cambio pasó por PR y por el CI. Quien integró los PR fue la persona, no la IA. La IA **no pudo ejecutar** Terraform ni el despliegue en AWS: eso lo hizo el equipo siguiendo sus instrucciones.
- GitHub Copilot aparece con un commit en el historial (PR #99 del repositorio base).
- **Cada integrante debe añadir su propio uso de IA y poder explicar el código que presenta.**
