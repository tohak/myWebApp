package com.konovalov.web.domain;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(name = "message_tbl")
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder
public class Message {
    public static final boolean BUN_NULL = false;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // поставил пока на IDENTITY, знаю что єто для мускула
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Input text message")
    @Length(max = 2000, message = "limit light message 2000 ")
    @Column(name = "text", nullable = BUN_NULL)
    private String text;

    @Length(max = 255, message = "limit light tag 255 ")
    @Column(name = "tag_text")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;


    public Message(Long id, String text, String tag) {
        this.id = id;
        this.text = text;
        this.tag = tag;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

}
