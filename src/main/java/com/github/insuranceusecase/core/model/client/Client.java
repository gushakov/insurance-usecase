package com.github.insuranceusecase.core.model.client;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Aggregate root. {@code Client} models a generic customer with
 * whom the insurance company has business relations. This is
 * usually an insurance policyholder.
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Client {

    ClientId id;

    String firstName;
    String lastName;

    /*
        Other information related to the client as a physical or moral person.
     */
}
