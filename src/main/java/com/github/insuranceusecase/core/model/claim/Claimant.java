package com.github.insuranceusecase.core.model.claim;

import com.github.insuranceusecase.core.model.InvalidDomainObjectError;
import com.github.insuranceusecase.core.model.client.ClientId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

/**
 * Aggregate. A claimant represents an insured party which
 * can issue claims.
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Claimant {

    /**
     * ID which uniquely identifies this claimant (client).
     */
    @Getter
    ClientId clientId;

    /**
     * Claimants may be of different types.
     */
    @Getter
    ClaimantType type;

    @Builder
    public Claimant(ClientId clientId, ClaimantType type) {
        this.clientId = Optional.ofNullable(clientId).orElseThrow(InvalidDomainObjectError::new);
        this.type = Optional.ofNullable(type).orElseThrow(InvalidDomainObjectError::new);
    }
}
