package com.konovalov.web.domain.common;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MappedSuperclass        //Autowired in all class
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // поставил пока на IDENTITY, знаю что єто для мускула
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

}
