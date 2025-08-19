package com.barberhq.api.domain.tenant.controller;

import com.barberhq.api.domain.tenant.dto.CreateTenantRequest;
import com.barberhq.api.domain.tenant.service.TenantService;
import com.barberhq.api.landlord.Tenant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<Tenant> registerTenant(@Valid @RequestBody CreateTenantRequest request) {
        Tenant createdTenant = tenantService.createTenant(request);
        // Retorna status 201 Created com a localização do novo recurso
        return ResponseEntity.created(URI.create("/api/tenants/" + createdTenant.getId())).body(createdTenant);
    }
}