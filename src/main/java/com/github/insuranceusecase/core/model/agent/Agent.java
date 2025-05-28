package com.github.insuranceusecase.core.model.agent;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Aggregate root. An employee of the insurance company in charge
 * of examination of claims.
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Agent {

    AgentId id;

    /*
        Other attributes for the agent.
     */
}
