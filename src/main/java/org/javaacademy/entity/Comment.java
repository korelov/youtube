package org.javaacademy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private Video video;

    @ManyToOne
    @ToString.Exclude
    private User owner;

    @Column
    private String description;

    public Comment(Video video, User owner, String description) {
        this.video = video;
        this.owner = owner;
        this.description = description;
    }
}
