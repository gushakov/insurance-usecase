package com.github.insuranceusecase.core.usecase.claimprocessing;

import com.github.insuranceusecase.core.model.InvalidDomainObjectError;
import com.github.insuranceusecase.core.model.claim.Claim;
import com.github.insuranceusecase.core.model.claim.ClaimId;
import com.github.insuranceusecase.core.model.claim.Claimant;
import com.github.insuranceusecase.core.model.client.ClientId;
import com.github.insuranceusecase.core.port.ids.IdsOperationsOutputPort;
import com.github.insuranceusecase.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.insuranceusecase.core.port.persistence.PersistenceOperationsError;
import com.github.insuranceusecase.core.port.security.InsufficientAuthorizationError;
import com.github.insuranceusecase.core.port.security.SecurityOperationsOutputPort;
import com.github.insuranceusecase.core.port.security.UserNotAuthenticatedError;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClaimProcessingUseCase implements ClaimProcessingInputPort {

    // output (driven) ports
    ClaimsProcessingPresenterOutputPort presenter;
    SecurityOperationsOutputPort securityOps;
    PersistenceGatewayOutputPort persistenceOps;
    IdsOperationsOutputPort idsOps;

    @Override
    public void claimantSubmitsClaimWithSubstantiatingData(ClaimEntry claimEntry) {

        try {

            ClientId clientId;
            try {
                // get unique person ID of the user
                clientId = securityOps.userClientId();

                // assert that the user is actually a claimant
                securityOps.assertUserHasReclamationPrivileges();

            } catch (UserNotAuthenticatedError | InsufficientAuthorizationError e) {

                // FAILURE of interaction: current user is not authenticated or not a claimant
                presenter.presentErrorWhenUserIsNotClaimant(e);
                return;
            }

            Claimant claimant;
            try {
                // load claimant aggregate corresponding to the current user
                claimant = persistenceOps.loadClaimant(clientId);
            } catch (PersistenceOperationsError e) {
                /*
                    FAILURE of interaction: cannot find claimant (client) information
                    for the current user
                 */
                presenter.presentErrorWhenLookingUpClaimantForUser(clientId);
                return;
            }

            Claim claim;
            try {
                // make a new ID for a new claim
                ClaimId claimId = idsOps.newClaimId();

                // create a new instance of claim aggregate
                claim = Claim.builder()
                        .claimId(claimId)
                        .clientId(clientId)
                        .claimantType(claimant.getType())
                        .build();

                /*
                    We assume here that claim is further mutated
                    according to various data from "ClaimEntry"
                    DTO.
                 */

            } catch (InvalidDomainObjectError e) {

                // FAILURE of interaction: cannot create new claim
                presenter.presentErrorIfCannotCreateNewClaim(claimant);
                return;
            }

            /*
                Example of use case "extension".

                Alistair: "1a. Submitted data is incomplete:
                            1a1. Insurance company requests missing information
                            1a2. Claimant supplies missing information"
             */

            // check if the claim's data is incomplete
            if (!claim.isDataEntryComplete()) {

                /*
                    FAILURE of interaction: claim is missing some data.
                    Note that this is a partial failure of the use case,
                    since the user can still achieve her goal by resubmitting
                    additional information (in a new interaction).
                 */
                presenter.presentErrorIfNewClaimDataIsIncomplete(claim, claimant, claimEntry);
                return;
            }

            try {
                // save new claim
                persistenceOps.saveClaim(claim);

                // SUCCESS of interaction: a claimant has filed a new claim
                presenter.presentSuccessfulSubmissionOfNewClaim(claim, claimant);
            } catch (PersistenceOperationsError e) {

                // FAILURE of interaction: cannot save new claim
                presenter.presentErrorIfCannotPersistNewClaim(claim);
                return;
            }

        } catch (Exception e) {
            // FAILURE of interaction: unspecified error has occurred
            presenter.presentError(e);
            return;
        }

    }

    @Override
    public void claimantSuppliesMissingInformation(ClaimEntry claimEntry) {
        // not shown for brevity, interaction is similar to the one
    }

    @Override
    public void insuranceCompanyVerifiesThatClientHasValidPolicy(ClientId clientId) {

    }

}
