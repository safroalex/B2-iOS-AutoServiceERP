package com.safroalex.AutoServiceERP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double costOur;
    private Double costForeign;

    // Геттер для id
    public Long getId() {
        return id;
    }

    // Сеттер для id (если необходимо)
    public void setId(Long id) {
        this.id = id;
    }

    // Геттер для name
    public String getName() {
        return name;
    }

    // Сеттер для name
    public void setName(String name) {
        this.name = name;
    }

    // Геттер для costOur
    public Double getCostOur() {
        return costOur;
    }

    // Сеттер для costOur
    public void setCostOur(Double costOur) {
        this.costOur = costOur;
    }

    // Геттер для costForeign
    public Double getCostForeign() {
        return costForeign;
    }

    // Сеттер для costForeign
    public void setCostForeign(Double costForeign) {
        this.costForeign = costForeign;
    }
}
