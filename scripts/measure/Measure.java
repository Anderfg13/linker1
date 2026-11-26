import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Automated, technical measurement of the Linker1 "create a branded link and
 * have it ready to share" task (docs: indicador de valor, section 3.3).
 *
 * It measures the SYSTEM side only (HTTP round-trips on a local instance); it
 * does NOT replace timing real people. Run it against a throw-away instance:
 *   LINKER_DB_PATH=%TEMP%\measure.db LINKER_PORT=18080 java -jar target\...jar
 *   java scripts/measure/Measure.java http://localhost:18080 30
 */
public class Measure {
    static final HttpClient C = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();

    static HttpResponse<String> send(HttpRequest r) throws Exception {
        return C.send(r, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    static HttpRequest.Builder req(String base, String path) {
        return HttpRequest.newBuilder(URI.create(base + path));
    }

    static HttpResponse<String> create(String base, String json) throws Exception {
        return send(req(base, "/link").header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json)).build());
    }

    static double pct(List<Double> v, double p) {
        var s = new ArrayList<>(v);
        Collections.sort(s);
        return s.get((int) Math.min(s.size() - 1, Math.ceil(p * s.size()) - 1));
    }

    public static void main(String[] a) throws Exception {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        var base = a.length > 0 ? a[0] : "http://localhost:8080";
        int n = a.length > 1 ? Integer.parseInt(a[1]) : 30;
        var run = Long.toString(System.currentTimeMillis(), 36);
        var createMs = new ArrayList<Double>();
        var flowMs = new ArrayList<Double>();
        int ok = 0, openOk = 0, statsOk = 0;
        for (int i = 0; i < n; i++) {
            var alias = "m" + run + i;
            long t0 = System.nanoTime();
            var r = create(base, "{\"url\":\"https://example.com/pagina/" + i + "\",\"alias\":\"" + alias + "\"}");
            long t1 = System.nanoTime();
            boolean created = r.statusCode() == 201 && r.body().equals(alias);
            if (created) ok++;
            createMs.add((t1 - t0) / 1e6);
            var open = send(req(base, "/" + alias).GET().build());
            boolean redirected = open.statusCode() == 302 && open.headers().firstValue("Location").isPresent();
            if (redirected) openOk++;
            var st = send(req(base, "/link/" + alias + "/stats").GET().build());
            if (st.statusCode() == 200 && st.body().replace(" ", "").contains("\"clicks\":1")) statsOk++;
            flowMs.add((System.nanoTime() - t0) / 1e6);
        }
        // Error-message checks (clear Spanish message, correct status).
        var bad = create(base, "{\"url\":\"ftp://x\",\"alias\":\"ok" + run + "\"}");
        var dup = create(base, "{\"url\":\"https://example.com\",\"alias\":\"m" + run + "0\"}");
        var res = create(base, "{\"url\":\"https://example.com\",\"alias\":\"link\"}");

        System.out.printf("Instancia: %s | repeticiones: %d%n%n", base, n);
        System.out.println("| Métrica | Resultado |");
        System.out.println("|---|---|");
        System.out.printf("| Creación con alias (201) | %d/%d |%n", ok, n);
        System.out.printf("| Apertura con 302 + Location | %d/%d |%n", openOk, n);
        System.out.printf("| Conteo de aperturas = 1 tras abrir | %d/%d |%n", statsOk, n);
        System.out.printf("| Crear enlace, mediana / p95 (ms) | %.1f / %.1f |%n", pct(createMs, .5), pct(createMs, .95));
        System.out.printf("| Crear+abrir+consultar, mediana / p95 (ms) | %.1f / %.1f |%n", pct(flowMs, .5), pct(flowMs, .95));
        System.out.printf("| URL inválida | %d: %s |%n", bad.statusCode(), bad.body());
        System.out.printf("| Alias ocupado | %d: %s |%n", dup.statusCode(), dup.body());
        System.out.printf("| Alias reservado (\"link\") | %d: %s |%n", res.statusCode(), res.body());
    }
}
