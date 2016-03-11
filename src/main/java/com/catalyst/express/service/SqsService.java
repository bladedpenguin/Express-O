package com.catalyst.express.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.catalyst.express.domain.CoffeeOrder;
import com.catalyst.express.repository.CoffeeOrderRepository;
import com.catalyst.express.web.rest.CoffeeOrderResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SqsService {
	
	private static final String url = "https://sqs.us-east-1.amazonaws.com/599764098801/CoffeeOrders";
	private final Logger log = LoggerFactory.getLogger(SqsService.class);
	private final ObjectMapper mapper = new ObjectMapper();
	
	private AmazonSQSClient sqs;
	@Inject
	public CoffeeOrderRepository coffeeOrderRepository;
	public SqsService() {
		 this.sqs = new AmazonSQSClient();
		 
		 Region usEast = Region.getRegion(Regions.US_EAST_1);
		 sqs.setRegion(usEast);
		 Timer timer = new Timer(true);
		 timer.schedule(new RecieveMessagesTask(), 25000, 25000);
	}
	
	
	/**
	 * Inner class for polling sqs and keeping coffee orders.
	 * @author tRau
	 *
	 */
	class RecieveMessagesTask extends TimerTask {

		@Override
		public void run() {
			ReceiveMessageRequest rmr =  new ReceiveMessageRequest(url);
			rmr.setWaitTimeSeconds(20);
			ReceiveMessageResult result = sqs.receiveMessage(rmr);
			List<Message> messages = result.getMessages();
			for ( Message m : messages){
				log.warn("woot message found at  " + new Date() + " : " +m.getBody());
				try {
					CoffeeOrder coffeeOrder  = mapper.readerFor(CoffeeOrder.class).readValue(m.getBody());
					CoffeeOrder result1 = coffeeOrderRepository.save(coffeeOrder);
					log.info("Queue message successfully popped and processed: " + result1.toString());
					sqs.deleteMessage(new DeleteMessageRequest(url, m.getReceiptHandle()));
				} catch (JsonProcessingException e) {
					log.error("wow somebody put a bad coffee order into the queue: " + m.getBody() );
					sqs.deleteMessage(new DeleteMessageRequest(url, m.getReceiptHandle()));
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void orderCoffee(CoffeeOrder coffeeOrder){
		try {
			sqs.sendMessage(url, mapper.writeValueAsString(coffeeOrder));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
