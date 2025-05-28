package com.github.insuranceusecase.core.usecase.claimprocessing;

import com.github.insuranceusecase.core.model.client.ClientId;

/**
 * "User goal" or Use Case for a claimant asking to be reimbursed
 * for charges related to a car accident. It consists of
 * a sequence of "interactions" between "primary actors" and
 * our system (application) which plays the role of "insurance
 * company".
 */
public interface ClaimProcessingInputPort {

    /**
     * Alistair's step: "1. Claimant submits claim with substantiating data."
     *
     * @param claimEntry data filed by a claimant
     */
    void claimantSubmitsClaimWithSubstantiatingData(ClaimEntry claimEntry);

    /**
     * Alistair's "extension" step: "1a2. Claimant supplies missing information"
     * @param claimEntry data filed by a claimant
     */
    void claimantSuppliesMissingInformation(ClaimEntry claimEntry);

    /**
     * Alistair's step: "2. Insurance company verifies claimant owns a valid policy"
     * @param clientId ID of the client
     */
    void insuranceCompanyVerifiesThatClientHasValidPolicy(ClientId clientId);
}
