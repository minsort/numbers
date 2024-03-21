package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.numbers.NumberServiceGrpc;

public class StartClient {
    private long tempVal = 0;
    private static String host = "localhost";
    private static int port = 8080;

    public static void main(String[] args) throws InterruptedException {
        //Создаем и настраиваем канал
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        System.out.println("Создан канал: " + host + ":" + port);

        //Создаем асинхронный stub - предоставляет асинхронные методы для отправки запросов и получения ответов
        NumberServiceGrpc.NumberServiceStub asyncClient = NumberServiceGrpc.newStub(managedChannel);
        new StartClient().clientAction(asyncClient);

        System.out.println("Клиент завершает работу");
        managedChannel.shutdown();
    }

    private void clientAction(NumberServiceGrpc.NumberServiceStub asyncClient) throws InterruptedException {
        ClientService clientService = new ClientService();
        asyncClient.number(makeNumberRequest(), clientService);

        for (int i = 0; i < 50; i++) {
            long valForPrint = getNextNumber(clientService);
            System.out.println("currentValue: " + valForPrint);
            Thread.sleep(1000);
        }
    }

    private long getNextNumber(ClientService clientService) {
        tempVal = tempVal + clientService.getLastValueAndReset() + 1;
        return tempVal;
    }

    private org.numbers.NumberRequest makeNumberRequest() {
        return org.numbers.NumberRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();
    }

}
