package com.gaoxiong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaoxiong
 * @ClassName GaoxiongRateLimit
 * @Description TODO
 * @date 2019/8/29 16:33
 */
@SpringBootApplication
@EnableZuulProxy
public class GaoxiongRateLimit {

    public static void main ( String[] args ) {
        SpringApplication.run(GaoxiongRateLimit.class, args);
    }

    @RestController
    public class ServiceController {

        public static final String RESPONSE_BODY = "ResponseBody";

        @GetMapping("/serviceA")
        public ResponseEntity<String> serviceA() {
            return ResponseEntity.ok(RESPONSE_BODY);
        }

        @GetMapping("/serviceB")
        public ResponseEntity<String> serviceB() {
            return ResponseEntity.ok(RESPONSE_BODY);
        }

        @GetMapping("/serviceC")
        public ResponseEntity<String> serviceC() {
            return ResponseEntity.ok(RESPONSE_BODY);
        }

        @GetMapping("/serviceD/{paramName}")
        public ResponseEntity<String> serviceD(@PathVariable String paramName) {
            return ResponseEntity.ok(RESPONSE_BODY + " " + paramName);
        }

        @GetMapping("/serviceE")
        public ResponseEntity<String> serviceE() throws InterruptedException {
            Thread.sleep(1100);
            return ResponseEntity.ok(RESPONSE_BODY);
        }
    }
}
