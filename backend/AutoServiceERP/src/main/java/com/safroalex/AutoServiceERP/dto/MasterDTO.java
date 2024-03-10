package com.safroalex.AutoServiceERP.dto;

public class MasterDTO {
    private Long id;
    private String name;
    private Long numberOfWorks; // Дополнительное поле для количества выполненных работ

    public MasterDTO() {
    }

    public MasterDTO(Long id, String name, Long numberOfWorks) {
        this.id = id;
        this.name = name;
        this.numberOfWorks = numberOfWorks;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumberOfWorks() {
        return numberOfWorks;
    }

    public void setNumberOfWorks(Long numberOfWorks) {
        this.numberOfWorks = numberOfWorks;
    }
}
