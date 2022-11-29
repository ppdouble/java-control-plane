package io.envoyproxy.controlplane.pemo.service;

public interface HandleUserRequest {

  void handleRequest(String c, String ip, Integer port, String version);
}
