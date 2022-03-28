package it.cnr.isti.labsedc.concern;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;
import it.cnr.isti.labsedc.concern.event.ConcernDTEvent;
import it.cnr.isti.labsedc.concern.event.ConcernNetworkEvent;

public class Probe {

	public static void testProbe(String brokerUrl, String topicName, 
									String username, String password, 
									String eventData, String eventName,
									String extension, String checksum,
									String sessionID, String sender,
									String destination) {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, brokerUrl);
			Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);     
			ObjectMessage msg = session.createObjectMessage();
			
			ConcernBaseEvent<String> event = new ConcernBaseEvent<String>(
					System.currentTimeMillis(), 
					sender, destination, sessionID, 
					checksum, eventName, eventData,
					CepType.DROOLS, extension);
			
 				msg.setObject(event);
				producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		String brokerUrl = "tcp://0.0.0.0:61616";

		/*
		//String brokerUrl = "tcp://sedc-nethd.isti.cnr.it:49195";
		printHello();
		TestmoreThanOneConn(brokerUrl);
		Thread.sleep(3000);
		TestlocalGlobalAvgDelayCheck(brokerUrl);
		//testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "Robot-ONE", "SLA Alert");
		*/
		
		testNetworkCongestion(brokerUrl);
		
		/*testDTProbe(brokerUrl);
		
		Thread.sleep(10000);
		
		TestDTValidation(brokerUrl);
		*/
		System.out.println("SENT");
	}

	private static void testNetworkCongestion(String brokerUrl) {

		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vera","griselda", brokerUrl);
			Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("DROOLS-InstanceOne");
            MessageProducer producer = session.createProducer(topic);     
			
            //MapMessage msg = session.createMapMessage();
			  
            ObjectMessage msg = session.createObjectMessage();
            
//			ConcernNetworkEvent<String> connectionAmount = new ConcernNetworkEvent<String>(
//					System.currentTimeMillis(), 
//					"probeROS", "monitoring","sessionOne",
//					"asd", "connectionAmount", "4", 
//					CepType.DROOLS,3);  
			ConcernNetworkEvent<String> connectionAmount = new ConcernNetworkEvent<String>(
					System.currentTimeMillis(), 
					"probeROS", "monitoring","sessionOne",
					"asd", "connectionEvent", "7", 
					CepType.DROOLS);  
            
            
			msg.setObject(connectionAmount);
			
			producer.send(msg);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public static void testDTProbe(String brokerUrl) throws InterruptedException {

		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vera","griselda", brokerUrl);
			Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("DROOLS-InstanceOne");
            MessageProducer producer = session.createProducer(topic);     
			ObjectMessage msg = session.createObjectMessage();
			
			ConcernDTEvent<String> previous = null;
			
			ConcernDTEvent<String> event = new ConcernDTEvent<String>(
					System.currentTimeMillis(), 
					"DigitalTwin", "Monitoring", "123098", 
					"rfng3o49bfn", "NextEventWillBE", "930",
					CepType.DROOLS, previous);
 				msg.setObject(event);
				producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void TestDTValidation(String brokerUrl) throws InterruptedException {
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "930", "NextEventWillBE", "giogio", "asd", "123098", "DigitalTwin", "Monitoring");	
	}
	
	
	public static void TestmoreThanOneConn(String brokerUrl) throws InterruptedException {
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "LocalPlanner", "Connect to:", "port:1234", "checksum", "sessionA", "sender", "destination");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "LocalPlanner", "Connect to:", "port:1234", "checksum", "sessionA", "sender", "destination");
	}
	
	public static void TestlocalGlobalAvgDelayCheck(String brokerUrl) throws InterruptedException {
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "LocalPlanner", "GlobalPlanner");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "GlobalPlanner", "Monitoring");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "LocalPlanner", "GlobalPlanner");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "GlobalPlanner", "Monitoring");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "LocalPlanner", "GlobalPlanner");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "GlobalPlanner", "Monitoring");

	}

//
//	private static void printHello() {		
//System.out.println("  _     _    __   _  _  _  _  _ \n"
//		+ " /_`| |/_`/|//   /_//_// //_)/_`\n"
//		+ "/_, |//_,/ |/   /  / \\/_//_)/_, \n"
//		+ "                                \n");	
//	}
}
