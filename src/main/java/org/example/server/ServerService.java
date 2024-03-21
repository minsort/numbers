package org.example.server;

import io.grpc.stub.StreamObserver;
import org.numbers.NumberRequest;
import org.numbers.NumberResponse;
import org.numbers.NumberServiceGrpc.NumberServiceImplBase;

import java.util.concurrent.atomic.AtomicLong;

public class ServerService extends NumberServiceImplBase {

    @Override
    public void number(NumberRequest request,
                       StreamObserver<NumberResponse> responseStreamObserver) {
        // Будет хранить текущее значение числа
        AtomicLong actualNumber = new AtomicLong(request.getFirstValue());

        // Задаем интервал между числами
        final long intervalInSeconds = 2;

        try {
            while (true) {
                // Получаем текущее значение и инкрементируем его
                long tempValue = actualNumber.incrementAndGet();

                // Создаем и отправляем ответ клиенту
                NumberResponse response = NumberResponse.newBuilder()
                        .setValue(tempValue)
                        .build();
                responseStreamObserver.onNext(response);

                // Проверяем, достигли ли мы последнего значения
                if (tempValue == request.getLastValue()) {
                    responseStreamObserver.onCompleted();
                    break;
                }

                // Ожидаем указанный интервал перед отправкой следующего числа
                Thread.sleep(intervalInSeconds * 1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
