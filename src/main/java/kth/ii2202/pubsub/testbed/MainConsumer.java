package kth.ii2202.pubsub.testbed;

import java.util.List;

/**
 * @author pradeeppeiris
 * 
 *         ActiveMQ ./activemq start ./activemq stop
 *
 *
 *         RabittMQ /usr/local/Cellar/rabbitmq/3.6.4/sbin/rabbitmq-server
 *         ./rabbitmqctl stop
 *         /etc/init.d/rabbitmq-server restart
 * 
 *         kafka bin/zookeeper-server-start.sh config/zookeeper.properties
 *         bin/kafka-server-start.sh config/server.properties
 *         
 */
public class MainConsumer {
	
	public static void main(String[] args) throws Exception {
		startConsumer();
	}

	private static void startConsumer() throws Exception {
		List<Consumer> consumerList = ConsumerFactory.getMessageConsumer();
		for (Consumer consumer:consumerList){
			Runnable r=new ConsumerWorker(consumer);
			new Thread(r).start();
		}
	}
}
