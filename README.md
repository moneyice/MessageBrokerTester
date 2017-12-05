# MessageBrokerTestbed
Testbed for performance benchmark of Message Broker systems

Change properties file resources/testbed.properties

How to run:
java -cp ./:MessageBrokerTestbed-1.0.jar kth.ii2202.pubsub.testbed.MainConsumer
java -cp ./:MessageBrokerTestbed-1.0.jar kth.ii2202.pubsub.testbed.MainProducer


mvn activemq-perf:consumer -Dfactory.brokerURL=tcp://10.202.0.28:61616 -DsysTest.numClients=5 -DsysTest.reportDir=./ -Dconsumer.destName=queue://TEST_FOO -Dconsumer.recvDuration=3000000

mvn activemq-perf:producer -Dfactory.brokerURL=tcp://10.202.0.28:61616 -DsysTest.numClients=5 -DsysTest.reportDir=./ -Dproducer.destName=queue://TEST_FOO -Dproducer.recvDuration=300000