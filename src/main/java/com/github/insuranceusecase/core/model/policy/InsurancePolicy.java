package com.github.insuranceusecase.core.model.policy;

import com.github.insuranceusecase.core.model.client.ClientId;
import lombok.Getter;

/**
 * Aggregate root. Insurance policy.
 */
public class InsurancePolicy {

    PolicyNumber policyNumber;

    /**
     * ID of the policyholder.
     */
    ClientId clientId;

    @Getter
    PolicyType type;

    /**
     * Returns {@code true} if this policy is still valid.
     *
     * @return {@code true} if this policy is valid
     */
    public boolean isValid() {
        return false;
    }

    /*
        Other attributes of an insurance policy: terms, period when active,
        premiums and reimbursement conditions.
     */
}
