package com.satyam.DevBoard.service;

import com.satyam.DevBoard.model.StandupRun;
import org.springframework.data.repository.Repository;

import java.util.UUID;

interface StandupRunRepository extends Repository<StandupRun, UUID> {
}
