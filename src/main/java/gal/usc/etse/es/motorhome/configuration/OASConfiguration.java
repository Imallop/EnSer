package gal.usc.etse.es.motorhome.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@SecuritySchemes({
        @SecurityScheme(
                type = SecuritySchemeType.HTTP,
                name = "jwt",
                description = "Standard JWT token authentication",
                in = SecuritySchemeIn.HEADER,
                paramName = HttpHeaders.AUTHORIZATION,
                scheme = "Bearer",
                bearerFormat = "JWT"
        )
})

@OpenAPIDefinition(
        info = @Info(
                title = "REST API de exemplo",
                description = "REST API de exemplo para Enxeñaria de Servizos",
                summary = """
                            Esta é unha API de exemplo para Enxeñaria de servizos.
                            Todos os endpoits son de mentira, e o seu obxectivo e amosarvos
                            un exemplo de como desenvolver unha API con Spring Boot
                        """,
                contact = @Contact(
                        name = "Victor Gallego",
                        email = "victorjose.gallego@usc.gal"
                ),
                license = @License(
                        name = "Apache 2.0",
                        identifier = "Apache-2.0",
                        url = "https://spdx.org/licenses/Apache-2.0.html"
                ),
                version = "0.1.0"
        ),
        servers = {
                @Server(
                        url = "localhost:18080",
                        description = "API para desenvolvemento"
                ),
                @Server(
                        url = "api.example.app",
                        description = "API de produción"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "jwt"
                ),
                @SecurityRequirement(
                        name = "refresh"
                ),
        }
)
public class OASConfiguration {
}
