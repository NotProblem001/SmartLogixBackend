package com.smartlogix.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;

/**
 * Configuración dinámica del decodificador de JWT.
 * Permite validar tokens de forma simétrica (usando JWT_SECRET) o asimétrica
 * a través de un Identity Provider OIDC (usando JWT_ISSUER_URI).
 */
@Configuration
public class JwtDecoderConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:#{null}}")
    private String issuerUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key:#{null}}")
    private String secretKey;

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        if (StringUtils.hasText(secretKey)) {
            // Validación simétrica (HS256)
            byte[] keyBytes = secretKey.getBytes();
            SecretKeySpec spec = new SecretKeySpec(keyBytes, "HmacSHA256");
            return NimbusReactiveJwtDecoder.withSecretKey(spec).build();
        } else if (StringUtils.hasText(issuerUri)) {
            // Validación asimétrica (OIDC / Auth0)
            return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
        } else {
            // Fallback para entornos locales de desarrollo si no hay variables configuradas
            String defaultSecret = "defaultsecretkeyplaceholderformicros32chars";
            byte[] keyBytes = defaultSecret.getBytes();
            SecretKeySpec spec = new SecretKeySpec(keyBytes, "HmacSHA256");
            return NimbusReactiveJwtDecoder.withSecretKey(spec).build();
        }
    }
}
