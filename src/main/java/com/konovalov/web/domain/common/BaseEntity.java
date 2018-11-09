package com.konovalov.web.domain.common;


import lombok.*;

import javax.persistence.*;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
@MappedSuperclass        //Autowired in all class
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    public BaseEntity() {
        super();
    }
    public BaseEntity(Long id) {
        super();
        this.id=id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
