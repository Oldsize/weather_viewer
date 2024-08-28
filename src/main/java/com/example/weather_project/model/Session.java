package com.example.weather_project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@ToString
@Getter
@Setter
@Table(schema = "default", name = "sessions")
public class Session {
    @Id
    @Column(name = "ID")
    private String session_id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "UserId")
    private User user;
    @Column(name = "ExpiresAt")
    private LocalDateTime session;
    @Version
    private Long version;

    public Session(String session_id, User user, LocalDateTime session) {
        this.session_id = session_id;
        this.user = user;
        this.session = session;
    }
}