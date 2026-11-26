# Quick Start (sin credenciales privadas) — T-08-03

Sirve para que una persona evaluadora levante Linker1 y haga el recorrido principal **sin** claves de LaunchDarkly, OCI ni MySQL.

**Requisitos:** Java 21, Maven 3.7+, Git (o el Dev Container descrito en el [README](../README.md)).

```bash
git clone https://github.com/Anderfg13/linker1.git
cd linker1
git checkout develop
mvn -B package -DskipTests
LINKER_PORT=8080 LINKER_DB_PATH=demo.db java -jar target/linker1-1.0-jar-with-dependencies.jar
```

La app arranca sin `LD_SDK_KEY`: muestra un aviso y usa los valores por defecto de los feature flags (interfaz clásica v1). `/healthz` responderá `503` si no hay MySQL configurado; no afecta al resto.

**Recorrido principal** (en otra terminal, o desde `http://localhost:8080`):
```bash
ID=$(curl -s -X POST localhost:8080/link -H 'Content-Type: application/json' -d '{"url":"https://example.com"}')
curl -i localhost:8080/$ID          # 302 + Location
curl -s localhost:8080/link/$ID/stats   # {"id":"...","clicks":1}
curl -s -X POST localhost:8080/link -H 'Content-Type: application/json' -d '{"url":"javascript:alert(1)"}'   # 400 en español
```

**Feature flag (demo):** con una SDK key de LaunchDarkly (`LD_SDK_KEY=<tu clave>`) y el flag booleano `new-ui` activado, la app sirve la interfaz v2 sin reiniciar. Ver [`FEATURE_FLAGS.md`](FEATURE_FLAGS.md). `LD_OFFLINE=true` usa el cliente sin red.

Estos pasos son los mismos que ejecuta el CI (build + smoke test) y la verificación end-to-end de [HU-03](proceso/trazabilidad-HU-03.md). No se probaron aún desde un Dev Container limpio.
