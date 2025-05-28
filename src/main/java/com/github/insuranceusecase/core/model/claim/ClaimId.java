package com.github.insuranceusecase.core.model.claim;

import lombok.Value;

import java.util.UUID;

/**
 * Unique identifier for claim.
 */
@Value
public class ClaimId {
    UUID id;
}
