# Análisis de dependencias (SCA) con OWASP Dependency-Check

Basado en el laboratorio de la clase: https://eci-arcn.github.io/Labs/sca-dependency-check/

Complementa a SonarCloud (análisis del código propio) revisando las **librerías de terceros**, incluidas las transitivas, contra la base de vulnerabilidades NVD. Dependabot avisa de versiones nuevas; Dependency-Check dice qué CVE afectan a lo que ya usamos.

## Activarlo (una vez)
1. Pide una API key en https://nvd.nist.gov/developers/request-an-api-key y actívala con el correo que llega.
2. Guárdala como secreto de Actions `NVD_API_KEY` en Settings → Secrets and variables → Actions. Nunca va en el `pom.xml`.
3. Haz un push o un PR: aparece el check **Dependency Check (SCA)**. Sin el secreto el job se salta y queda en verde.

La primera ejecución descarga la base NVD y tarda varios minutos; las siguientes usan caché.

## Qué hace
- Falla si encuentra una vulnerabilidad con **CVSS ≥ 7** (High o Critical), con `failBuildOnCVSS` en el `pom.xml`.
- Genera un informe HTML (artefacto del run) y un SARIF que se sube a Security → Code scanning.
- No se ejecuta con `mvn verify`; en local: `NVD_API_KEY=... mvn org.owasp:dependency-check-maven:13.0.0:check`.

## Si encuentra algo
1. Actualiza la dependencia a la versión corregida (preferido).
2. Si es un falso positivo o no aplica, agrégalo a `dependency-check-suppression.xml` con motivo, quién lo revisó y fecha de expiración (`until`).

## Ejercicio del laboratorio (pendiente del equipo)
Crear una rama con `commons-text` 1.9 (CVE-2022-42889), ver cómo el check falla, subir a 1.15.0 y ver cómo pasa, y practicar una supresión. Debe hacerlo el equipo y documentar el resultado con capturas; no se ejecutó aquí.
