package com.digitalbank.account.client;

import com.digitalbank.account.client.dto.UserStatusRs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/api/v1/users/{userId}/status")
    UserStatusRs getUserStatus(@PathVariable("userId") Long userId);
}