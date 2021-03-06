/**
 * 
 */
package kth.ii2202.pubsub.testbed;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author pradeeppeiris
 * java -cp "target/testbed-1.0.jar:config" kth.ii2202.pubsub.testbed.Main consumer
 */
public abstract class Consumer {

	private static final Logger logger = LogManager.getLogger(Consumer.class);
	private static final int INTERVAL_PRINT=10000;

	protected String brokerUrl;
	protected final String queueName;
	protected String username;
	protected String password;

	private int index=0;
	public Consumer(String brokerUrl, String queueName) {
		this.brokerUrl = brokerUrl;
		this.queueName = queueName;
		logger.info("TIME MESSAGEID LATENCY");
	}

	public void receiveMessages() throws Exception {
		createConnection();
		listenForMessages();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public abstract void listenForMessages() throws Exception;
	protected abstract void createConnection() throws Exception;
	int number=0;
	protected void logMessage(String message) {
		number++;
		System.out.println(queueName+" consumed  number "+ number);

		if(index>INTERVAL_PRINT){
			System.out.println(queueName+" ----------->  "+message);
			index=0;
		}else{
			index++;
		}
//		new Thread(new ConsumerLog(message, System.currentTimeMillis())).start();
	}
	
	private class ConsumerLog implements Runnable {
		String message;
		long receivedTime;
		
		ConsumerLog(String message, long receivedTime) {
			this.message = message;
			this.receivedTime = receivedTime;
		}
		
		@Override
		public void run() {
			processMessage();
		}
		
		private void processMessage() {
			String[] data = message.split(",");
			
			String messageId = data[0];
			long sentTime = Long.valueOf(data[1]);
			long elappsedTime = (receivedTime - sentTime);
			logger.info("{} {} {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(receivedTime)), messageId, elappsedTime);	
		}
	}
}
