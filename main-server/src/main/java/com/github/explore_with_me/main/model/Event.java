package com.github.explore_with_me.main.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "anotation", nullable = false)
    private String anotation;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "paid", columnDefinition = "boolean default false")
    private boolean paid;

    @Column(name = "participant_limit", columnDefinition = "integer default 0")
    private Integer participantLimit;

    @Column(name = "request_moderation", columnDefinition = "boolean default false")
    private boolean requestModeration;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EventState state;

    @Embedded
    @Column(nullable = false)
    private Location location;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_EVENT_CATEGORIES"))
    private Category category;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_EVENT_USERS"))
    User user;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Participation> participationList;
}
