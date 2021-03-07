package io.analog.alex.grpcclient.interactors

interface Interactor<I, O> {
    fun interact(input: I): O
}
