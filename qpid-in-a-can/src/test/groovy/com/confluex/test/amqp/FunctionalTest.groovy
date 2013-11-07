package com.confluex.test.amqp

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.QueueingConsumer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
//
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration('classpath:/qpid-in-a-can.xml')
class FunctionalTest {

    Connection connection
    Channel channel

    @Before
    void initClient() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri('amqp://tester:testing@localhost:35672');
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
    }

    @After
    public void stopAmqpClient() throws IOException {
        try {
            channel.close();
        } finally {
            connection.close();
        }
    }
    @Test
    void localhostAmqpShouldWorkWithMagicCredentialsAndPort() {
        channel.exchangeDeclare('example.exchange', 'direct', false, true, new HashMap<String, Object>());
        channel.queueDeclare('example.queue', false, true, true, [:])
        channel.queueBind('example.queue', 'example.exchange', 'foo')
        QueueingConsumer consumer = new QueueingConsumer(channel)
        channel.basicConsume('example.queue', consumer)

        final importantMessage = 'Mr. Watson, come here. I want to see you.'

        channel.basicPublish('example.exchange', 'foo', new AMQP.BasicProperties(), importantMessage.bytes)

        def delivery = consumer.nextDelivery(2000);
        assert null != delivery
        assert importantMessage == new String(delivery.body)
    }
}
