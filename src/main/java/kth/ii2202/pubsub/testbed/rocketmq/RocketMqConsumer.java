/**
 * 
 */
package kth.ii2202.pubsub.testbed.rocketmq;

import kth.ii2202.pubsub.testbed.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author pradeeppeiris
 *
 */
public class RocketMqConsumer extends Consumer {
	private static final Logger logger = LogManager.getLogger(RocketMqConsumer.class);

	DefaultMQPushConsumer consumer;

	public RocketMqConsumer(String brokerUrl, String queueName) {
		super(brokerUrl, queueName);
	}
	
	@Override
	protected void createConnection() throws Exception {
		consumer = new DefaultMQPushConsumer(
				"rmq-group");

		consumer.setNamesrvAddr(brokerUrl);
		consumer.setInstanceName("consumer");
		consumer.subscribe(queueName, "TagA");


	}
	
	@Override
	public void listenForMessages() throws Exception {
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				for (MessageExt msg : msgs) {
					logMessage(new String(msg.getBody()));
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
	}
}
