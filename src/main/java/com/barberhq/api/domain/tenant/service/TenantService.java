package com.barberhq.api.domain.tenant.service;

import com.barberhq.api.domain.tenant.dto.CreateTenantRequest;
import com.barberhq.api.landlord.Tenant;
import com.barberhq.api.landlord.TenantRepository;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.Normalizer;
import java.util.regex.Pattern;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;
    private final DataSource dataSource;

    public TenantService(TenantRepository tenantRepository, DataSource dataSource) {
        this.tenantRepository = tenantRepository;
        this.dataSource = dataSource;
    }

    @Transactional
    public Tenant createTenant(CreateTenantRequest request) {
        String schemaName = generateSchemaName(request.name());

        Tenant newTenant = new Tenant();
        newTenant.setName(request.name());
        newTenant.setSchemaName(schemaName);
        Tenant savedTenant = tenantRepository.save(newTenant);

        // Agora, em vez do JdbcTemplate, usamos o Flyway para criar e migrar o schema
        Flyway flyway = Flyway.configure()
                .locations("classpath:db/tenant") // Aponta para uma nova pasta de migrations
                .dataSource(this.dataSource)
                .schemas(schemaName) // Especifica o schema a ser criado e gerenciado
                .load();
        flyway.migrate();

        return savedTenant;
    }

    private String generateSchemaName(String name) {
        // Remove acentos
        String nfdNormalizedString = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAccents = pattern.matcher(nfdNormalizedString).replaceAll("");

        // Converte para minúsculas, substitui espaços por underscore e remove outros caracteres
        return noAccents
                .toLowerCase()
                .replaceAll("\\s+", "_")
                .replaceAll("[^a-z0-9_]", "");
    }
}