package com.matin.taxi.webSocket;

import com.matin.taxi.db.model.Orders;

public class TaxiManager {

	
	
	//createOrder
	void CreateTemplate(Long clientId, String route)
	{
		Orders o = new Orders(clientId, (long) 0, Orders.STATE_CREATED, route);
	}
	
	
	
	
	
}
