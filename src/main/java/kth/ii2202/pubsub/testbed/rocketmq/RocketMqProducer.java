/**
 * 
 */
package kth.ii2202.pubsub.testbed.rocketmq;

import kth.ii2202.pubsub.testbed.Producer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @author pradeeppeiris
 *
 */
public class RocketMqProducer extends Producer {
	private DefaultMQProducer producer;

	public RocketMqProducer(String brokerUrl, String queueName) {
		super(brokerUrl, queueName);
	}
	
	@Override
	protected void createConnection() throws Exception {
		producer = new DefaultMQProducer("rmq-group");
		producer.setNamesrvAddr(brokerUrl);
		producer.setInstanceName("producer");
		producer.start();
	}

	@Override
	protected void sendMessage(String message) throws Exception {
		Message msg = new Message(queueName,// topic
				"TagA",// tag
				message.getBytes()// body
		);
		SendResult sendResult = producer.send(msg);
	}

	@Override
	protected void closeConnection() throws Exception {
		producer.shutdown();
	}
}
