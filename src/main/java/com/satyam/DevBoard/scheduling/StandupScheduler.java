package com.satyam.DevBoard.scheduling;

import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.Standup;
import com.satyam.DevBoard.repository.StandupRepository;
import com.satyam.DevBoard.service.StandupRunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StandupScheduler {

    private static final String LOCK_KEY = "lock:standup-scheduler";

    private final StandupRepository standupRepository;
    private final StandupRunService standupRunService;
    private final RedisLock redisLock;

    @Value("${devboard.scheduler.lock-enabled:true}")
    private boolean lockEnabled;

    @Scheduled(cron = "0 * * * * *")   // second 0 of every minute
    public void openDueStandups() {
        // TTL is shorter than the 60 s period. The lock is never released early, so a second
        // instance ticking a moment later in the same minute still skips.
        if (lockEnabled && !redisLock.tryAcquire(LOCK_KEY, Duration.ofSeconds(50))) {
            log.debug("Another instance holds the scheduler lock, skipping this tick");
            return;
        }

        Instant now = Instant.now();
        List<Standup> standups = standupRepository.findAllActiveByProjectStatus(Project.Status.ACTIVE);

        for (Standup standup : standups) {
            try {
                ZoneId zone = ZoneId.of(standup.getProject().getOrganization().getTimeZone());
                ZonedDateTime local = now.atZone(zone);

                if (!isScheduledDay(standup.getScheduleDays(), local.getDayOfWeek())) continue;
                if (local.toLocalTime().isBefore(standup.getScheduledTime())) continue;

                standupRunService.openDailyRun(standup.getId(), local.toLocalDate());
            } catch (DataIntegrityViolationException e) {
                log.info("Standup {} was already opened by another instance", standup.getId());
            } catch (Exception e) {
                // one bad standup, for example an invalid timezone, must not stop the others
                log.error("Could not process standup {}", standup.getId(), e);
            }
        }
    }

    private boolean isScheduledDay(String scheduleDays, DayOfWeek day) {
        String code = day.name().substring(0, 3);   // MONDAY becomes MON
        return Arrays.asList(scheduleDays.split("\\s*,\\s*")).contains(code);
    }
}