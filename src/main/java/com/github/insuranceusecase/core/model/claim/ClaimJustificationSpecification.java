package com.github.insuranceusecase.core.model.claim;

public interface ClaimJustificationSpecification {

    boolean isClaimDataEntryComplete(Claim claim);

    boolean isClaimSubstantiated(Claim claim);

    boolean isClaimVerified(Claim claim);
}
