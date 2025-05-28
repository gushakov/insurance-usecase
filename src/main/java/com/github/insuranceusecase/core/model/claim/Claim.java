package com.github.insuranceusecase.core.model.claim;

import com.github.insuranceusecase.core.model.InvalidDomainObjectError;
import com.github.insuranceusecase.core.model.client.ClientId;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

/**
 * Aggregate modeling claim request by a claimant for
 * reimbursement of any expenses incurred due to an
 * accident.
 */
@Value
public class Claim {

    /**
     * Unique identifier for this claim.
     */
    ClaimId claimId;

    /**
     * ID of the client associated with this claim.
     */
    ClientId clientId;

    /**
     * Current status of the claim.
     */
    ClaimStatus claimStatus;

    /**
     * Type of the claimant issuing this claim.
     */
    ClaimantType claimantType;

    /**
     * Claim justification policy which applies for this
     * claim processing.
     */
    ClaimJustificationSpecification justificationPolicy;

    @Builder
    public Claim(ClaimId claimId, ClientId clientId, ClaimantType claimantType,
                 ClaimStatus claimStatus, ClaimJustificationSpecification justificationPolicy) {
        this.claimId = Optional.ofNullable(claimId).orElseThrow(InvalidDomainObjectError::new);
        this.clientId = Optional.ofNullable(clientId).orElseThrow(InvalidDomainObjectError::new);
        this.claimantType = Optional.ofNullable(claimantType).orElseThrow(InvalidDomainObjectError::new);

        // if claim status is not supplied, assume the claim is in initial state
        this.claimStatus = Optional.ofNullable(claimStatus).orElse(ClaimStatus.Filed);

        /*
            If claim justification policy is not supplied, determine it based on
            the type of the claimant.
         */
        this.justificationPolicy = Optional.ofNullable(justificationPolicy).orElse(determineJustificationPolicy(claimantType));
    }

    private ClaimJustificationSpecification determineJustificationPolicy(ClaimantType claimantType) {
        return switch (Optional.ofNullable(claimantType).orElseThrow(InvalidDomainObjectError::new)) {
            case PhysicalPerson -> new PhysicalPersonClaimJustificationSpecification();
            case GovernmentEntity, InsuranceCompany -> new MoralPersonClaimJustificationSpecification();
        };
    }

    private ClaimBuilder newClaim() {
        return new ClaimBuilder()
                .claimId(claimId)
                .clientId(clientId)
                .claimStatus(claimStatus)
                .justificationPolicy(justificationPolicy);
    }

    /**
     * A claim is considered as complete if it was just filed
     * and that all relevant data entry is complete, according
     * to the justification policy associated with the claim.
     *
     * @return {@code true} if the claim is complete
     * @throws InvalidDomainObjectError if the claim was not
     *                                  just filed
     */
    public boolean isDataEntryComplete() {
        return claimStatus == ClaimStatus.Filed
                && justificationPolicy.isClaimDataEntryComplete(this);
    }

}
