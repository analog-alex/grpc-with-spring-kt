package io.analog.alex.grpcclient.configuration

import java.util.concurrent.atomic.AtomicInteger

object ActionOrderSingleton {
    val order = AtomicInteger(0)
}
