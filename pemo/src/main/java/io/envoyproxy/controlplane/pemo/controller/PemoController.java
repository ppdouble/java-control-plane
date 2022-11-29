package io.envoyproxy.controlplane.pemo.controller;

import io.envoyproxy.controlplane.pemo.service.HandleUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PemoController {

    @Autowired
    HandleUserRequest handleUserRequest;

        @GetMapping("")
        public String pemodefault(@RequestParam(value = "customdefaultstr", defaultValue = "PefaultPage") String customdefaultstr) {
            return String.format("This is %s~", customdefaultstr);
        }

        @GetMapping("/pemohello")
        public String pemohello(@RequestParam(value = "name", defaultValue = "PemoWorld") String name) {
            return String.format("Hello %s~", name);
        }

        // cluster0, "127.0.0.1", 1234
        @GetMapping("/toenvoy")
        public String toenvoy(@RequestParam(value = "c", defaultValue = "cluster0") String c,
                              @RequestParam(value = "ip", defaultValue = "127.0.0.1") String ip,
                              @RequestParam(value = "port", defaultValue = "1234") Integer port,
                              @RequestParam(value = "ver", defaultValue = "1") String version) {

            handleUserRequest.handleRequest(c, ip, port, version);
            return c;
        }
}
