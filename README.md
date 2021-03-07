# gRPC with some Spring Boot

## Introduction
This is small project that has two goals: 
 
- Serve as a quick _how to_ guide in setting up gRPC with **Gradle**  
- Serve as a quick _reminder_ as to how serve normal Spring request using the **protobuf** format and serializers  
- Be a small and unrepresentative test of the performance between a service that 1) uses JSON serialization, 2) uses protobuf serialization and 3) uses the gRPC protocol _in toto_.   

## Rundown

The project is divided in 3 modules:  

- **[server-1]** a small Spring Boot app that serves requests over HTTP with both JSON and protobuf serialization.  
- **[server-2]** a **Netty** based gRPC server.  
- **[client-1]** a client server that can make the 3 types of requests to the servers aforementioned.  


### Proto file

The same `.proto` file is copied across all three servers. To wit:
```proto

syntax = "proto3";

package io.analog.alex.grpcserver;

option java_multiple_files = true;
option java_outer_classname = "GreetingProto";

message GreetingRequest {
  string name = 1;
  string author = 2;
  string message = 3;
  int32 order = 4;
}

message GreetingResponse {
  string greet = 1;
}

service GreetingService {
  rpc sayHello (GreetingRequest) returns (GreetingResponse) {}
}
```


## How To

Simply run `./gradlew generateProto` followed by `./gradlew build`
