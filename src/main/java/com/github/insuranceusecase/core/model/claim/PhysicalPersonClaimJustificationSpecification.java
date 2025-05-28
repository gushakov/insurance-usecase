package com.github.insuranceusecase.core.model.claim;

public class PhysicalPersonClaimJustificationSpecification implements ClaimJustificationSpecification {
    @Override
    public boolean isClaimDataEntryComplete(Claim claim) {
        return false;
    }

    @Override
    public boolean isClaimSubstantiated(Claim claim) {
        return false;
    }

    @Override
    public boolean isClaimVerified(Claim claim) {
        return false;
    }
}
