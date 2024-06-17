package org.javaacademy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nick_name")
    private String nickName;

    @OneToMany(mappedBy = "owner")
    private List<Video> videoList;

    public User(String nickName) {
        this.nickName = nickName;
    }
}
