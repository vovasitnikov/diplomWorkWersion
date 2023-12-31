package com.github.explore_with_me.stats.service;

import com.github.explore_with_me.stats.input_dto.InputHitDto;
import com.github.explore_with_me.stats.model.Hit;
import com.github.explore_with_me.stats.output_dto.StatsDto;
import com.github.explore_with_me.stats.repository.HitRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private HitRepository hitRepository;

    @Transactional()
    @Override
    public void saveHit(InputHitDto inputHitDto) {
        Hit hit = Hit.builder()
                .uri(inputHitDto.getUri())
                .ip(inputHitDto.getIp())
                .app(inputHitDto.getApp())
                .timestamp(inputHitDto.getTimestamp())
                .build();
        hitRepository.save(hit);
        log.info("Просмотр события с ip= " + inputHitDto.getIp() + " сохранён");
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start,
                                   LocalDateTime end,
                                   List<String> uris,
                                   boolean unique) {
        List<StatsDto> stats = new ArrayList<>();

        if (unique && uris != null) {
            stats = hitRepository.getUniqueStatsByUrisAndTimestamps(start, end, uris);
        }
        if (!unique && uris != null) {
            stats = hitRepository.getStatsByUrisAndTimestamps(start, end, uris);
        }
        if (!unique && uris == null) {
            stats = hitRepository.getAllStats(start, end);
        }
        if (unique && uris == null) {
            stats = hitRepository.getAllUniqueStats(start, end);
        }
        log.info("Статистика по просмотру событий получена= " + stats);
        return stats;
    }
}
