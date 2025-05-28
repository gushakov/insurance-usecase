package com.github.insuranceusecase.core.model.agent;

import lombok.Value;

import java.util.UUID;

/**
 * Unique internal identifier of an insurance agent (employee).
 */
@Value
public class AgentId {
    UUID id;
}
