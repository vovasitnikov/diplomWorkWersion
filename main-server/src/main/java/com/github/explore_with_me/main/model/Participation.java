package com.github.explore_with_me.main.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participations")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PARTICIPATION_USER"))
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "event_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PARTICIPATION_EVENT"))
    private Event event;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "participation_status")
    private RequestState status;

    @Column(name = "created_when")
    private LocalDateTime created;
}
