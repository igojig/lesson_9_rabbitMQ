package ru.igojig.rabbitmq.console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ItProducer {

    private static final String EXCHANGER_NAME = "It_Exchanger";

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()
        ) {
            // создаем Exchanger в поставщике сообщений
            channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.DIRECT);
            while (true) {
                System.out.println("Enter message [tag message]: ");
                String inputText = sc.nextLine();
                if (inputText.equals("exit")) {
                    break;
                }
                String[] inputArr = inputText.split("\\s", 2);
                if (inputArr.length > 1) {
                    String routeKey = inputArr[0];
                    String message = inputArr[1];
                    channel.basicPublish(EXCHANGER_NAME, routeKey, null, message.getBytes(StandardCharsets.UTF_8));
                }

            }
        }

    }


}
