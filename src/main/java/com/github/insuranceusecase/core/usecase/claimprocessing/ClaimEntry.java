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

import lombok.Data;

/**
 * DTO encapsulating all relevant information submitted by a
 * claimant. This is an example of a "Request Model" as
 * described by Robert C. Martin.
 */
@Data
public class ClaimEntry {

    /*
        Any information related to the accident: i.e.:
        place and time.
     */

}
