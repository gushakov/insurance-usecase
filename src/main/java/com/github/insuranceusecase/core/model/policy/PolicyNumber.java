package com.github.insuranceusecase.core.model.policy;

import lombok.Value;

import java.util.UUID;

/**
 * Unique identifier for an insurance policy.
 */
@Value
public class PolicyNumber {
    UUID number;
}
