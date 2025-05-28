package com.github.insuranceusecase.core.model.claim;

/**
 * In Alistair's terms, this is a part of a "variation"
 * of our use case. A claimant can be "a person",
 * "another insurance company", or "the government".
 */
public enum ClaimantType {
    PhysicalPerson,
    GovernmentEntity,
    InsuranceCompany,
}
