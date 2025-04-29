package hooks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockHooks {

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void startWireMockServer() {
        wireMockServer = new WireMockServer(
                options()
                        .port(8089)
                        .usingFilesUnderDirectory("src/test/resources")
                        .extensions(new ResponseTemplateTransformer(true))
        );
        wireMockServer.start();
        System.out.println("✅ WireMock started at http://localhost:8089");
    }

    @AfterAll
    public static void stopWireMockServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
            System.out.println("🛑 WireMock stopped");
        }
    }
}
