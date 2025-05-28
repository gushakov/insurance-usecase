package com.github.insuranceusecase.core.port.persistence;

import com.github.insuranceusecase.core.model.claim.Claim;
import com.github.insuranceusecase.core.model.claim.Claimant;
import com.github.insuranceusecase.core.model.client.ClientId;

public interface PersistenceGatewayOutputPort {

    Claimant loadClaimant(ClientId claimantId);

    void saveClaim(Claim claim);
}
