/*
    COPYRIGHT DISCLAIMER:
    ----------
    The code in this project has been inspired by the article
    by Alistair Cockburn: "Structuring use cases with goals", 1995.
    Article can be consulted from:
    https://web.archive.org/web/20170620145208/http://alistair.cockburn.us/Structuring+use+cases+with+goals

    The names of some methods and the terms used in the accompanying comments
    are taken exactly as they figure in the original article by Alistair.
    This is only for reasons of the illustration. All relevant copyrights
    are presumed reserved by Alistair Cockburn.
    Alistair's site: https://alistair.cockburn.us/.
 */

package com.github.insuranceusecase.core.usecase.claimprocessing;

import com.github.insuranceusecase.core.model.claim.ClaimId;
import com.github.insuranceusecase.core.model.policy.PolicyNumber;

/**
 * "User goal" or Use Case for a claimant asking to be reimbursed
 * for charges related to a car accident. It consists of
 * a sequence of "interactions" between "primary actors" and
 * our system (application) which plays the role of "insurance
 * company".
 */
public interface ClaimProcessingInputPort {

    /**
     * Step: "1. Claimant submits claim with substantiating data." (from the example use case
     * by Alistair).
     *
     * @param claimEntry data filed by a claimant
     */
    void claimantSubmitsClaimWithSubstantiatingData(ClaimEntry claimEntry);

    /**
     * "Extension" step: "1a2. Claimant supplies missing information" (from the example
     * use case by Alistair).
     *
     * @param claimEntry data filed by a claimant
     */
    void claimantSuppliesMissingInformation(ClaimEntry claimEntry);

    /**
     * Step: "2. Insurance company verifies claimant owns a valid policy" (from the example
     * use case by Alistair).
     *
     * @param claimId ID of the claim
     */
    void insuranceCompanyVerifiesThatClientHasValidPolicy(ClaimId claimId);

    /**
     * Step: "3. Insurance company assigns agent to examine case" (from the example
     * use case by Alistair).
     *
     * @param claimId      ID of the claim to be examined
     * @param policyNumber policy number identifying it among all policies held by the claimant
     */
    void insuranceCompanyAssignesAgentToCase(ClaimId claimId, PolicyNumber policyNumber);

    /**
     * Step: "4. Agent verifies all details are within policy guidelines" (from the example
     * use case by Alistair).
     *
     * @param claimId      ID of the examined claim
     * @param policyNumber policy number identifying it among all policies held by the claimant
     */
    void agentVerifiesClaimDetailsWithinPolicyGuidelines(ClaimId claimId, PolicyNumber policyNumber);

    /**
     * @param claimId      ID of claim
     * @param policyNumber number of the policy identifying it among all policies held by the claimant
     */
    void insuranceCompanyPaysClaimant(ClaimId claimId, PolicyNumber policyNumber);

    // other extensions are omitted for brevity
}
