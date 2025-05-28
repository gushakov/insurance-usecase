package com.github.insuranceusecase.core.model.claim;

public enum ClaimStatus {

    /**
     * State of a claim when it is initially registered in
     * the system.
     */
    Filed,

    /**
     * State of a claim when the company considers that the
     * claimant has provided "substantial" enough data in
     * justification of the claim.
     */
    Confirmed,

    /**
     * State of a claim after an agent verifies its compliance
     * with the company policy.
     */
    Verified,

    /**
     * State of the claim after it was satisfied (a payment was made
     * to the claimant).
     */
    Satisfied,

    /**
     * State of the claim after it was rejected.
     */
    Rejected;
}
