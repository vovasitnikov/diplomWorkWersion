package com.github.explore_with_me.main.service;

import com.github.explore_with_me.main.repository.ParticipationRepository;
import org.springframework.stereotype.Service;

@Service
public class ParticipationService {
    private final ParticipationRepository participationRepository;

    public ParticipationService(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }
}
