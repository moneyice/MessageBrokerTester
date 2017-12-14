/**
 * 
 */
package kth.ii2202.pubsub.testbed;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import kth.ii2202.pubsub.testbed.activemq.ActiveMqConsumer;
import kth.ii2202.pubsub.testbed.kafka.KafkaMsgConsumer;
import kth.ii2202.pubsub.testbed.rabbitmq.RabbitMqReceiver;
import kth.ii2202.pubsub.testbed.rocketmq.RocketMqConsumer;
import kth.ii2202.pubsub.testbed.sqs.SQSConsumer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * @author pradeeppeiris
 *
 */
public class ConsumerFactory {
	private static final String PROP_BROKER = "main.type";
	private static final String BROKER_ACTIVEMQ = "activemq";
	private static final String BROKER_RABBITMQ = "rabbitmq";
	private static final String BROKER_KAFKA = "kafka";
	private static final String BROKER_ROCKETMQ = "rocketmq";
	private static final String BROKER_SQS = "sqs";
	
	private static final String PROP_QUEUE_NAME = "main.queue.name";
	private static final String PROP_USERNAME = "broker.username";
	private static final String PROP_PASSWORD = "broker.password";
	private static final String PROP_ACTIVEMQ_URL = "broker.activemq.url";
	private static final String PROP_RABBITMQ_URL = "broker.rabbitmq.url";
	private static final String PROP_KAFKA_URL = "broker.kafka.url";
	private static final String PROP_SQS_URL = "broker.sqs.url";
	private static final String PROP_ROCKETMQ_URL = "broker.rocketmq.url";
	private static final String PROP_QUEUE_NAME_FILE = "main.queue.name.file";


	public static List<Consumer> getMessageConsumer() throws Exception {
		Context context = Context.getInstance();
		String brokerType = context.getProperty(PROP_BROKER);
		String queueName = context.getProperty(PROP_QUEUE_NAME);
		String queueNameFile=context.getProperty(PROP_QUEUE_NAME_FILE);
		String username=context.getProperty(PROP_USERNAME);
		String password=context.getProperty(PROP_PASSWORD);

		List<String> queueNameList=getQueueNameList(queueNameFile);
		if(queueNameList==null||queueNameList.isEmpty()){
			return null;
		}

		List<Consumer> consumerList= Lists.newArrayList();
		for (String queueNameString:queueNameList) {
			Consumer consumer=createConsumer(context, brokerType, queueNameString,username,password);
			consumerList.add(consumer);
		}


		return consumerList;
	}

	private static Consumer createConsumer(Context context, String brokerType, String queueName, String username, String password) throws Exception {
		Consumer consumer=null;
		if(BROKER_ACTIVEMQ.equals(brokerType)) {
			consumer = new ActiveMqConsumer(context.getProperty(PROP_ACTIVEMQ_URL), queueName);
		} else if(BROKER_RABBITMQ.equals(brokerType)) {
			consumer = new RabbitMqReceiver(context.getProperty(PROP_RABBITMQ_URL), queueName);
		} else if(BROKER_KAFKA.equals(brokerType)) {
			consumer = new KafkaMsgConsumer(context.getProperty(PROP_KAFKA_URL), queueName);
		} else if(BROKER_SQS.equals(brokerType)) {
			consumer = new SQSConsumer(context.getProperty(PROP_SQS_URL), queueName);
		} else if(BROKER_ROCKETMQ.equals(brokerType)) {
			consumer = new RocketMqConsumer(context.getProperty(PROP_ROCKETMQ_URL), queueName);
		} else {
			throw new Exception("Invalid broker type specified");
		}
		if(username!=null){
			consumer.setUsername(username);
		}
		if(password!=null){
			consumer.setPassword(password);
		}

		return consumer;
	}

	private static List<String> getQueueNameList(String queueNameFile) {
		List<String> list= null;
		try {
			File file = new File(ConsumerFactory.class.getClassLoader().getResource(queueNameFile).getFile());
			list = Files.readLines(file, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
