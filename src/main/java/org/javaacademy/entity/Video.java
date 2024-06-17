package org.javaacademy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User owner;

    @OneToMany(mappedBy = "video")
    private List<Comment> comments;

    public Video(String videoName, String description, User owner) {
        this.title = videoName;
        this.description = description;
        this.owner = owner;
    }
}
