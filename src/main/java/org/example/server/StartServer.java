package org.example.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class StartServer {
    private static int serverPort = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(serverPort)
                .addService(new ServerService())
                .build();
        server.start();

        System.out.println("Сервер успешно стартовал на порту: " + serverPort); //Простота - не грех :)

        //Добавили хук завершения для адекватного завершения работы
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {   //Можно и лямдой, ну это кому как..
            @Override
            public void run() {
                server.shutdown();
                System.out.println("Сервер остановлен");
            }
        }));

        //Блокируем текущий поток
        server.awaitTermination();
    }

}