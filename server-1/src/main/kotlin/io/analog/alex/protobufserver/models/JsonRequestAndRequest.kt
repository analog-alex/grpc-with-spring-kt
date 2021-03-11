package io.analog.alex.protobufserver.models

data class JsonRequest(val name: String, val author: String, val message: String, val order: Int)
data class JsonResponse(val greet: String)