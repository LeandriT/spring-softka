package ec.banco.pichincha.account_service.config;

import ec.banco.pichincha.account_service.config.auditor.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    @Bean
    public AuditorAware<String> currentProvider() {
        return new AuditorAwareImpl();
    }
}