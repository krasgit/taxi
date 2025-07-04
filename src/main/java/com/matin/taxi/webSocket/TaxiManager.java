package com.matin.taxi.webSocket;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.matin.taxi.db.model.Person;
import com.matin.taxi.db.model.PersonDAOImpl;

import jakarta.xml.bind.DatatypeConverter;

public class TaxiManager {

	
	protected static final Logger LOG = LoggerFactory.getLogger(TaxiManager.class);
	
	
	class Work {
		
		Long personId;
		public Long getPersonId() {
			return personId;
		}

		public void setPersonId(Long personId) {
			this.personId = personId;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}


		String msg;
		int i=0;
		
		public Work(Long personId, String msg, int i) {
			this.personId=personId;
			this.msg=msg;
			
		}

		static String hashNumber(String arg)  {
			String myHash;
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(arg.getBytes());
				byte[] digest = md.digest();
				 myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				myHash=e.getMessage();
			}
			
			return myHash;
		}
		
		
		 String hashNumber()  {
			String myHash;
			try {
				
				String arg=personId+ msg;
				
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(arg.getBytes());
				byte[] digest = md.digest();
				 myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				myHash=e.getMessage();
			}
			
			return myHash;
		}
		
	}
	protected SignalingSocketHandlerRPC signalingSocketHandlerRPC = null;
	PersonDAOImpl personDAO = null;
	
	Map<String, Work> hm = new HashMap<String, Work>();
	



	public TaxiManager(SignalingSocketHandlerRPC signalingSocketHandlerRPC, PersonDAOImpl personDAO) {
		this.signalingSocketHandlerRPC=signalingSocketHandlerRPC;
		this.personDAO=personDAO;
	}


	
	


	public void add(Long personId, String msg) {
		
		Work work =new Work (personId,msg,0);
		
		hm.put(work.hashNumber(), work);
		
	}
	
	boolean send(WebSocketSession webSocket,String msg )
	{
		  try {
				 ResultMessage resultMessage = new ResultMessage(msg);	
				 webSocket.sendMessage(new TextMessage(resultMessage.toObjectMapperString()));
				
				 return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
		  return false;
	}
	
	
	void process(){
		
		int size=hm.size();
		
		 for (Map.Entry<String, Work> me : hm.entrySet()) {

            System.out.print(me.getKey() + ":");
            Work work= me.getValue();
            
            
           Person person = personDAO.getPersonById(work.getPersonId());
           WebSocketSession webSocket = signalingSocketHandlerRPC.getConnectedUsers().get(person.getToken());
           
           if(webSocket==null)
           {
        	   LOG.info("Not connection for person" +person.getName());
        	   return ;
           }
           
           boolean res=send(webSocket,work.getMsg());
           if(res)
            hm.remove(me.getKey());
           else 
           {
        	   
        	   String m=person.getName() +""+work.getMsg();
        	   LOG.info("wait");
           }
           
		 }
           
	}
	
	
	
}
