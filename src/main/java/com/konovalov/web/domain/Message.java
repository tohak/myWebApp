package com.konovalov.web.domain;


import com.konovalov.web.domain.common.BaseEntity;
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
public class Message extends BaseEntity {
    public static final boolean BUN_NULL = false;

    @NotBlank(message = "Input text message")
    @Length(max = 2000, message = "limit light message 2000 ")
    @Column(name = "text", nullable = BUN_NULL)
    private String text;

    @Length(max = 255, message = "limit light tag 255 ")
    @Column(name = "tag_text")
    private String tag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;


    public Message(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }
}
