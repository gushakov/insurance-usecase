package com.github.insuranceusecase.core.usecase.claimprocessing;

import com.github.insuranceusecase.core.model.claim.Claim;
import com.github.insuranceusecase.core.model.claim.ClaimId;
import com.github.insuranceusecase.core.model.claim.Claimant;
import com.github.insuranceusecase.core.model.client.Client;
import com.github.insuranceusecase.core.model.client.ClientId;
import com.github.insuranceusecase.core.port.ErrorHandlingPresenterOutputPort;
import com.github.insuranceusecase.core.port.security.SecurityOperationsError;

public interface ClaimsProcessingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentErrorWhenUserIsNotClaimant(SecurityOperationsError e);

    void presentErrorWhenLookingUpClaimantForUser(ClientId claimantId);

    void presentErrorIfCannotCreateNewClaim(Claimant claimant);

    void presentErrorIfCannotPersistNewClaim(Claim claim);

    void presentSuccessfulSubmissionOfNewClaim(Claim claim, Claimant claimant);

    void presentErrorIfNewClaimDataIsIncomplete(Claim claim, Claimant claimant, ClaimEntry claimEntry);

    void presentErrorIfClientCannotBeLoaded(ClientId clientId);

    void presentErrorIfClientDoesNotHaveValidPolicy(Client client);

    void presentErrorIfPoliciesCannotBeLoadedForClient(ClientId clientId);

    void presentErrorIfClaimCannotBeLoadedForVerification(ClaimId claimId);

    void presentErrorIfCannotPersistDeclinedClaim(Claim claim);

    void presentErrorIfCannotDeclineClaim(Claim claim);
}
