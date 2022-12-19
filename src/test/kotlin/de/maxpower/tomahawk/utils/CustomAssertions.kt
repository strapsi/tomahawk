package de.maxpower.tomahawk.utils

import strikt.api.Assertion
import strikt.assertions.isNotNull

// Use on nullables to assert that the value is not null and then make more assertions
infix fun <T> Assertion.Builder<T?>.must(assertion: Assertion.Builder<T>.() -> Unit): Assertion.Builder<T> =
    this.isNotNull().and(assertion)