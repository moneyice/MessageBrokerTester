package kth.ii2202.pubsub.testbed;

class ConsumerWorker implements  Runnable{
    Consumer consumer;
		public ConsumerWorker(Consumer consumer){
			this.consumer=consumer;
		}

		@Override
		public void run() {
			try {
				consumer.receiveMessages();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}