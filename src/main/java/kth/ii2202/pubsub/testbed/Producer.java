/**
 * 
 */
package kth.ii2202.pubsub.testbed;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * MessageProducer follows the Template Method design pattern
 * to provide the skeleton of message generating logic 
 * for any Message Producer client in Message broker systems.
 * 
 * @author pradeeppeiris
 *
 */
public abstract class Producer {
	protected final String brokerUrl;
	protected final String queueName;
	private String large_size_string_cache=null;




	private String createMessage(double messageSizeInByte) {
		StringBuilder message = new StringBuilder();
		message.append(UUID.randomUUID().toString());
		message.append(",").append(System.currentTimeMillis()).append(",");
		message.append(resizeMessage(messageSizeInByte));
		return message.toString();
	}

	private String resizeMessage(double messageSizeInByte) {
		Double dMsgSize = messageSizeInByte / 2;
		int msgSize = dMsgSize.intValue();

		char[] chars = new char[msgSize];
		Arrays.fill(chars, 'a');

		return new String(chars);
	}

	private void sendMessages(int batchSize, double messageSizeInByte) {
		ExecutorService executor = new ThreadPoolExecutor(4, 4,
				60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE/10));
		for (int i = 0; i < batchSize; i++){
			executor.execute(new MessageSender(messageSizeInByte));
		}
		waitForAllExecutersToComplete(executor);


//		String msg=createMessage(messageSizeInByte);
//		for (int i = 0; i < batchSize; i++){
//			try {
//				sendMessage(msg);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}

	private void waitForAllExecutersToComplete(ExecutorService executor) {
		executor.shutdown();
		while (!executor.isTerminated()) {	 
		}
	}
	
	public Producer(String brokerUrl, String queueName) {
		this.brokerUrl = brokerUrl;
		this.queueName = queueName;
	}
	
	public void generateMessages(int batchSize, double messageSizeInByte) throws Exception {
		createConnection();
		sendMessages(batchSize, messageSizeInByte);
		closeConnection();
	}
	
	protected abstract void createConnection() throws Exception;
	protected abstract void sendMessage(String message) throws Exception;
	protected abstract void closeConnection() throws Exception;
	
	private class MessageSender implements Runnable {	
		double messageSizeInByte;
		
		MessageSender(double messageSizeInByte) {
			this.messageSizeInByte = messageSizeInByte;
		}
		
		@Override
		public void run() {
			String message = createMessage();
			try {
				sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		private String createMessage() {
			StringBuilder message = new StringBuilder();
			message.append(UUID.randomUUID().toString());
			message.append(",").append(System.currentTimeMillis()).append(",");
			message.append(resizeMessage());
			
			return message.toString();
		}
		
		private String resizeMessage() {
			if(large_size_string_cache==null){
				Double dMsgSize = messageSizeInByte/2;
				int msgSize = dMsgSize.intValue();

				char[] chars = new char[msgSize];
				Arrays.fill(chars, 'a');

				large_size_string_cache= new String(chars);
			}
			return large_size_string_cache;

		}
		
	}
}
