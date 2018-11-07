package com.konovalov.web.domain.common;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@MappedSuperclass        //Autowired in all class
public abstract class BaseEntity {
    public static final int LENGTH_ENUM = 20;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // поставил пока на IDENTITY, знаю что єто для мускула
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
}
