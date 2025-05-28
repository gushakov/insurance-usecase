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

import com.github.insuranceusecase.core.model.InvalidDomainObjectError;
import com.github.insuranceusecase.core.model.agent.AgentId;
import com.github.insuranceusecase.core.model.claim.Claim;
import com.github.insuranceusecase.core.model.claim.ClaimId;
import com.github.insuranceusecase.core.model.claim.Claimant;
import com.github.insuranceusecase.core.model.client.Client;
import com.github.insuranceusecase.core.model.client.ClientId;
import com.github.insuranceusecase.core.model.policy.InsurancePolicy;
import com.github.insuranceusecase.core.model.policy.PolicyNumber;
import com.github.insuranceusecase.core.model.policy.PolicyType;
import com.github.insuranceusecase.core.port.ids.IdsOperationsOutputPort;
import com.github.insuranceusecase.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.insuranceusecase.core.port.persistence.PersistenceOperationsError;
import com.github.insuranceusecase.core.port.security.InsufficientAuthorizationError;
import com.github.insuranceusecase.core.port.security.SecurityOperationsOutputPort;
import com.github.insuranceusecase.core.port.security.UserNotAuthenticatedError;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.Optional;

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
                // assert that the user is actually a claimant
                securityOps.assertUserHasReclamationPrivileges();

                // get unique person ID of the user
                clientId = securityOps.userClientId();

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
        // implementation omitted for brevity
    }

    @Override
    public void insuranceCompanyVerifiesThatClientHasValidPolicy(ClaimId claimId) {

        try {

            /*
                This interaction is on behalf of the system (insurance company),
                so we do not need to assert any particular privilege for the
                authenticated user.
             */

            // load the claim aggregate
            Claim claim;
            ClientId clientId;
            try {
                claim = persistenceOps.loadClaim(claimId);
                clientId = claim.getClientId();
            } catch (PersistenceOperationsError e) {

                // FAILURE of interaction: the claim for the case cannot be loaded
                presenter.presentErrorIfClaimCannotBeLoadedForVerification(claimId);
                return;
            }

            Client client;
            try {
                // load the client and any policy she holds
                client = persistenceOps.loadClient(clientId);
            } catch (PersistenceOperationsError e) {
                // FAILURE of interaction: the client cannot be loaded
                presenter.presentErrorIfClientCannotBeLoaded(clientId);
                return;
            }

            Optional<InsurancePolicy[]> policies;
            try {
                // load all insurance policies for the current policyholder (client)
                policies = persistenceOps.loadPolicies();
            } catch (PersistenceOperationsError e) {

                // FAILURE of interaction: client's policies could not be loaded
                presenter.presentErrorIfPoliciesCannotBeLoadedForClient(clientId);
                return;
            }

            /*
                Main interaction logic for this step: verify that the client
                has a valid insurance policy for car accidents.
             */

            if (policies.isPresent() && Arrays.stream(policies.get())
                    .anyMatch(policy -> policy.getType() == PolicyType.CarAccidentInsurance
                            && policy.isValid())) {

                // SUCCESS of interaction: found a valid policy
                return;
            } else {
                // FAILURE of interaction: the client does not have a valid car insurance

                /*
                    In the example by Alistair, we have an extension to our use case.
                    He puts it this way:
                        "2a. Claimant does not own a valid policy:"
                            "2a1. Insurance company declines claim, notifies claimant, records all this, terminates proceedings."
                    We would have to call appropriate output ports for the actions above.
                 */

                // decline the claim
                Claim declinedClaim;
                try {
                    // update the state of the claim in the system
                    declinedClaim = claim.decline();
                } catch (InvalidDomainObjectError e) {
                    // FAILURE of interaction: cannot decline the claim
                    presenter.presentErrorIfCannotDeclineClaim(claim);
                    return;
                }

                try {
                    persistenceOps.saveClaim(declinedClaim);
                } catch (PersistenceOperationsError e) {
                    // FAILURE of interaction: the declined claim could not be persisted
                    presenter.presentErrorIfCannotPersistDeclinedClaim(claim);
                    return;
                }

                // FAILURE of interaction: the client does not have a valid policy
                presenter.presentErrorIfClientDoesNotHaveValidPolicy(client);
                return;
            }

        } catch (Exception e) {
            // FAILURE of interaction: unspecified error has occurred
            presenter.presentError(e);
            return;
        }

    }

    @Override
    public void insuranceCompanyAssignesAgentToCase(ClaimId claimId, PolicyNumber policyNumber) {
        // implementation omitted for brevity
    }

    @Override
    public void agentVerifiesClaimDetailsWithinPolicyGuidelines(ClaimId claimId, PolicyNumber policyNumber) {

        /*
            Here we have an example of how a "secondary actor" (the assigned agent)
            may intervene in the same use case where a claimant figures as the
            "primary agent".
         */

        AgentId agentId;
        try {
            securityOps.assertUserHasAgentPrivileges();
            agentId = securityOps.userAgentId();
        } catch (UserNotAuthenticatedError | InsufficientAuthorizationError e) {

            // FAILURE of interaction: current user is not authenticated or not an agent
            presenter.presentErrorWhenUserIsNotAgent(e);
            return;
        }


        //  the rest of the implementation omitted for brevity
    }

    @Override
    public void insuranceCompanyPaysClaimant(ClaimId claimId, PolicyNumber policyNumber) {
        // implementation omitted for brevity
    }


}
