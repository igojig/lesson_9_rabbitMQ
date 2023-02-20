package ru.igojig.rabbitmq.console.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ItConsumer {
//    private static final String TASK_QUEUE_NAME = "it_queue";
    private static final String EXCHANGER_NAME="It_Exchanger";

    private static final Scanner sc=new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queueName=channel.queueDeclare().getQueue();
        System.out.println("QUEUE NAME: " + queueName);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

        String oldRouteKey=null;
        String routeKey=null;

        while (true){;
            System.out.println("Enter topic tag: [set_topic [tag]] ");
            String inputStr=sc.nextLine();
            if(inputStr.startsWith("set_topic ")){
                String[] inputArr=inputStr.split("\\s",2);
                if(oldRouteKey!=null){
                    channel.queueUnbind(queueName, EXCHANGER_NAME, oldRouteKey);
                }
                routeKey=inputArr[1];
                channel.queueBind(queueName, EXCHANGER_NAME, routeKey);
                oldRouteKey=routeKey;
            }

        }
    }



}
