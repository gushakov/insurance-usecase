package com.github.insuranceusecase.core.port.persistence;

import com.github.insuranceusecase.core.model.claim.Claim;
import com.github.insuranceusecase.core.model.claim.ClaimId;
import com.github.insuranceusecase.core.model.claim.Claimant;
import com.github.insuranceusecase.core.model.client.Client;
import com.github.insuranceusecase.core.model.client.ClientId;
import com.github.insuranceusecase.core.model.policy.InsurancePolicy;

import java.util.Optional;

public interface PersistenceGatewayOutputPort {

    Claimant loadClaimant(ClientId claimantId);

    void saveClaim(Claim claim);

    Client loadClient(ClientId clientId);

    Optional<InsurancePolicy[]> loadPolicies();

    Claim loadClaim(ClaimId claimId);
}
