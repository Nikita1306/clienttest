package com.example.clienttest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ClienttestApplication {
	@Value("${test.wCount}")
	private String wCount;

	public static void main(String[] args) {

		SpringApplication.run(ClienttestApplication.class, args);

	}
	@EventListener
	public void onAppContextStarted(ApplicationStartedEvent e) {
		int numberOfThreads = Integer.parseInt(wCount);
		ExecutorService service = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		for (int i = 0; i < numberOfThreads; i++) {
			service.submit(() -> {
				System.out.println(Thread.currentThread().getName());
				RestTemplate restTemplate = new RestTemplate();
				Long test = restTemplate.getForObject("http://localhost:8080/test?id=56", Long.class);
				System.out.println(test.toString());
				latch.countDown();
			});
		};
		System.out.println("sleep time : " + wCount);
	}
}
