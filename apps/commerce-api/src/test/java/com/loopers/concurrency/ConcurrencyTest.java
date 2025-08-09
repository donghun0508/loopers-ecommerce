package com.loopers.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
class ConcurrencyTest {

    private int counter = 0; // 필드 변수 (공유 자원)

    @BeforeEach
    void setUp() {
        counter = 0; // 테스트 시작 전 카운터 초기화
    }

    @Test
    void testMultiThreadedIncrement() throws InterruptedException {
        counter = 0; // 초기화

        int threadCount = 1000;
        int incrementPerThread = 1000;

        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < incrementPerThread; j++) {
                        counter++;
                    }
                    latch.countDown();
                });
            }
        }
        latch.await();

        int expected = threadCount * incrementPerThread;
        log.info("예상값: {}", expected);
        log.info("실제값: {}", counter);
        log.info("차이: {}", expected - counter);
    }

    @Test
    void testConcurrencyProblem() throws InterruptedException {
        counter = 0; // 초기화

        int threadCount = 10000;
        int incrementPerThread = 20;

        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < incrementPerThread; j++) {
                        counter++; // 임계영역
                    }
                    latch.countDown();
                });
            }
        }
        latch.await();

        int expected = threadCount * incrementPerThread;
        log.info("예상값: {}", expected);
        log.info("실제값: {}", counter);
        log.info("차이: {}", expected - counter);
    }

    @Test
    void testConcurrencyWithSynchronized() throws InterruptedException {
        counter = 0; // 초기화
        final Object lock = new Object();
        int threadCount = 10000;
        int incrementPerThread = 100000;

        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < incrementPerThread; j++) {
                        synchronized (lock) {
                            counter++; // 임계영역
                        }
                    }
                    latch.countDown();
                });
            }
        }
        latch.await();

        int expected = threadCount * incrementPerThread;
        log.info("예상값: {}", expected);
        log.info("실제값: {}", counter);
        log.info("차이: {}", expected - counter);
    }

    @Test
    void testConcurrencyWithSynchronized2() throws InterruptedException {
        counter = 0; // 초기화
        final Object lock = new Object();
        int threadCount = 10000;
        int incrementPerThread = 100000;

        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    synchronized (lock) {
                        for (int j = 0; j < incrementPerThread; j++) {
                            counter++; // 임계영역
                        }
                    }
                    latch.countDown();
                });
            }
        }
        latch.await();

        int expected = threadCount * incrementPerThread;
        log.info("예상값: {}", expected);
        log.info("실제값: {}", counter);
        log.info("차이: {}", expected - counter);
    }
}
