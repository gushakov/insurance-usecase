package com.github.insuranceusecase.core.port.security;

import com.github.insuranceusecase.core.model.agent.AgentId;
import com.github.insuranceusecase.core.model.client.ClientId;

/**
 * Output port which provides security related capabilities:
 * asserting that the user has this or that role, for example.
 */
public interface SecurityOperationsOutputPort {

    /**
     * Asserts that the current user is fully authenticated.
     *
     * @throws UserNotAuthenticatedError if the user is not authenticated
     */
    void assertUserIsAuthenticated();

    /**
     * Asserts that the current user is authenticated and
     * has reclamation privileges.
     *
     * @throws UserNotAuthenticatedError      if the user is not authenticated
     * @throws InsufficientAuthorizationError if the currently authenticated
     *                                        user may not file claims
     */
    void assertUserHasReclamationPrivileges();

    /**
     * Client ID of the currently authenticated user.
     *
     * @return client ID of the user
     * @throws UserNotAuthenticatedError if the user is not authenticated
     */
    ClientId userClientId();

    /**
     * Asserts that the current user is authenticated and
     * has agent privileges.
     *
     * @throws UserNotAuthenticatedError      if the user is not authenticated
     * @throws InsufficientAuthorizationError if the currently authenticated user
     *                                        does not have an agent role
     */
    void assertUserHasAgentPrivileges();

    /**
     * Agent ID of the currently authenticated user.
     *
     * @return agent ID of the user
     * @throws UserNotAuthenticatedError if the user is not authenticated
     */
    AgentId userAgentId();

}
