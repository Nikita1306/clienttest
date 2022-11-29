package com.example.clienttest;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@Log4j2
public class ClienttestApplication {

	@Value("${test.rCount}")
	private String rCount;
	@Value("${test.wCount}")
	private String wCount;

	@Value("${test.idList}")
	private String idList;

	public static void main(String[] args) {
		SpringApplication.run(ClienttestApplication.class, args);
	}
	@EventListener
	public void onAppContextStarted(ApplicationStartedEvent e) {
		int numberOfThreads = Integer.parseInt(wCount);
		ExecutorService serviceGet = Executors.newFixedThreadPool(numberOfThreads);
		String[] interval = idList.split(",");
		int lower = Integer.parseInt(interval[0]);
		int higher = Integer.parseInt(interval[1]);
		RestTemplate restTemplate = new RestTemplate();
		for (int i = 0; i < numberOfThreads; i++) {
		serviceGet.submit(() -> {
				Long test = restTemplate.getForObject("http://localhost:8080/test?id=" +  ThreadLocalRandom.current().nextInt(lower, higher + 1), Long.class);
				log.info(test);
			});
		}
		numberOfThreads = Integer.parseInt(rCount);
		ExecutorService servicePut = Executors.newFixedThreadPool(numberOfThreads);

		for (int i = 0; i < numberOfThreads; i++) {
			JSONObject personJsonObject = new JSONObject();
			personJsonObject.put("id", ThreadLocalRandom.current().nextInt(lower, higher + 1));
			personJsonObject.put("balance", ThreadLocalRandom.current().nextLong(-100L, 100L));
			servicePut.submit(() -> {
				restTemplate.put("http://localhost:8080/test",personJsonObject);
			});
		}
	}
}
