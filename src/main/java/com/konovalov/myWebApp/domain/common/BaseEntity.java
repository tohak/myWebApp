package com.konovalov.myWebApp.domain.common;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@NoArgsConstructor
@ToString
@MappedSuperclass        //Autowired in all class
public abstract class BaseEntity {
    public static final int LENGTH_ENUM = 20;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // поставил пока на IDENTITY, знаю что єто для мускула
    private Long id;
}
