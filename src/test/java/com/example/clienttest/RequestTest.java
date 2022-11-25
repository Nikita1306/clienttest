package com.example.clienttest;

import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@Execution(ExecutionMode.CONCURRENT)
@EnableAsync
@SpringBootTest(classes = ClienttestApplication.class)
public class RequestTest {

    @Autowired
    private TestService service;


    @Value("${test.wCount}")
    private String wCount;

    @Test
    @Execution(ExecutionMode.CONCURRENT)
//    @Concurrent(count = wCount)
//    @RepeatedTest(12)
    public void test() throws Exception {
//        ExecutorService executor = Executors.newFixedThreadPool(12);
////        ResultActions test = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/test?id=56"))
////                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(240L)));
////        test.toString();
//        System.out.println(service.getList() + Thread.currentThread().getName());
//        RestTemplate restTemplate = new RestTemplate();
//        Long test = restTemplate.getForObject("http://localhost:8080/test?id=56", Long.class);
//        System.out.println(test.toString());
//        assertEquals(240, test);
        int numberOfThreads = Integer.parseInt(wCount);
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                RestTemplate restTemplate = new RestTemplate();
                Long test = restTemplate.getForObject("http://localhost:8080/test?id=56", Long.class);
                System.out.println(test.toString());
                latch.countDown();
            });
        }
    }
}
