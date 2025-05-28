package com.github.insuranceusecase.core.port;

/**
 * Presents all errors not explicitly handled by any other output port.
 * Other presenters (ports) will extend this port with error
 * handling specific to a particular scenario of a use case.
 */
public interface ErrorHandlingPresenterOutputPort {

    /**
     * Generic presentation of any failure outcome during the execution
     * of a scenario.
     *
     * @param e any error
     */
    void presentError(Exception e);

}
