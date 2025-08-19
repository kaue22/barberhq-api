package com.barberhq.api.landlord;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tenants", schema = "public")
@Data
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "schema_name", nullable = false, unique = true) // Explicitando o nome da coluna
    private String schemaName;
}