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

    void presentErrorWhenUserIsNotAgent(SecurityOperationsError e);
}
