//RouteControl------------------------------------------------	  
	class TaxiControl extends ol.control.Control {

		constructor( opt_options) {
			const options = opt_options || {};

		
			var container = TaxiControl.createContainer(options);
			super({element: container, target: options.target, });
		}

		init() {
			
		}

		
		static acceptOrder(id){
			
			callRPC("acceptOrder",id).then((result) => 
				{	
			//	TaxiControl.render(result); 
			});
			
		}
		
		
		static finishOrder(id){
					
					callRPC("finishOrder",id).then((result) => 
						{	
					//	TaxiControl.render(result); 
					});
					
				}
		
		
		static loadOrders()
		{
		var user =Cookie.getCookie("user") ;
		var token =Cookie.getCookie("token") ;
			callRPC("loadTaxiOrders",user,token).then((result) => {	TaxiControl.render(result); });
		}
		
		static render(orders)
		{
		/*  orders.id, orders.route, orders.clientid, orders.state,	orders.taxiid, orders.createtime */
		var ordersTable = document.getElementById("tbodyRoute");	
		
		
		if(orders==null)
			{
				
				ordersTable.innerHTML="No active request";
				return;
				
			}
		
		
			
		var tableRuws="";
		const jsonData = JSON.parse(orders);
			
		for (var i = 0; i < jsonData.length; i++) 
			{
			var order = jsonData[i];
			var orderState =order.state;
			var route=order.route;
			const jsonRoute = JSON.parse(route);

			var coord=jsonRoute.coord;
					
			tableRuws+='<tr> <td><a href="#"> state('+orderState+') orderId('+order.id+')</a></td>';
			tableRuws+='<td style="padding-left: 5px;padding-bottom:3px; font-size: 12px;">';
			for (var ii = 0; ii < coord.length; ii++) 
				{
				var jsonRouteRow = coord[ii];
					tableRuws+=jsonRouteRow.name;
					tableRuws+='<br/>';
				}
			tableRuws+='</td>';
						
			tableRuws+='<td>'; 
					
					tableRuws+='	<button type="button" class="btn btn-primary btn-sm" onclick=" RouteControl.loadOrderById('+order.id+')">Show</button>';
							
					if(orderState==1){
					tableRuws+='	<button type="button" class="btn btn-primary btn-sm" onclick="TaxiControl.acceptOrder('+order.id+')">Accept</button>';
					}
					
					if(orderState==2){
						tableRuws+='	<button type="button" class="btn btn-primary btn-sm" onclick="TaxiControl.finishOrder('+order.id+')">Finish</button>';
										}
			tableRuws+='</td>';
			tableRuws+='</tr>';
			}
		ordersTable.innerHTML=tableRuws;
		}
				
		static loadOrder(){
			var user =Cookie.getCookie("user") ;
			var token =Cookie.getCookie("token") ;
							
			callRPC("loadOrder",user,token).then((result) => {  RouteControl.loadOrderCB(result);   });
		}	 
	
		static visible(mode){
			var container=document.getElementById('TaxiControlContainer');
			if(mode){
				TaxiControl.loadOrders();
				container.setAttribute('style', ' background-color: lightblue; min-width: 350;position: absolute; right : 0em; bottom : 0em ');
				}
				else 
				{
				container.setAttribute('style', 'style="display: none" ');
			}
		}
		
		static createContainer(options) {

		//	var orders=TaxiControl.loadOrders();
			var loadOrder = document.createElement('BUTTON');
				loadOrder.setAttribute('type', 'button');
				loadOrder.setAttribute('class', 'btn btn-primary btn-sm');
				loadOrder.setAttribute('onclick', 'TaxiControl.loadOrders()');
				loadOrder.innerHTML = '#';
			//			node_20.appendChild(loadOrder); 
			
			var node_1 = document.createElement('DIV');
			node_1.setAttribute('name', 'TaxiControlContainer');
			node_1.setAttribute('id', 'TaxiControlContainer');
			node_1.setAttribute('class', '     border w3-border-red  ');
			node_1.setAttribute('style', ' background-color: lightblue; min-width: 400;position: absolute; right : 0em; bottom : 0em ');

			var node_2 = document.createElement('DIV');
			node_2.setAttribute('name', 'header');
			node_2.setAttribute('class', 'container ');
			node_2.setAttribute('style', ' ');
			node_1.appendChild(node_2);

			var node_3 = document.createElement('DIV');
			node_3.setAttribute('class', 'd-flex justify-content-between ');
			node_2.appendChild(node_3);
			
			var loadOrder = document.createElement('BUTTON');
				loadOrder.setAttribute('type', 'button');
				loadOrder.setAttribute('class', 'btn btn-primary btn-sm');
				loadOrder.setAttribute('onclick', 'TaxiControl.loadOrders()');
				loadOrder.innerHTML = '#';
			
				node_3.appendChild(loadOrder); 
			var node_4 = document.createElement('DIV');
			node_4.setAttribute('class', '');
			node_3.appendChild(node_4);

			var node_5 = document.createElement('SPAN');
			//node_5.setAttribute('class', 'ref1');
			node_4.appendChild(node_5);
			
			var node_6 = document.createElement('DIV');
			node_6.setAttribute('class', '');
			node_3.appendChild(node_6);

			var node_7 = document.createElement('SPAN');
			node_7.setAttribute('id', 'routeUpdateInfo');	
			node_6.appendChild(node_7);

			var node_8 = document.createElement('DIV');
			node_8.setAttribute('class', '');
			node_3.appendChild(node_8);

			var node_9 = document.createElement('SPAN');
			node_9.setAttribute('class', 'refX');
			node_8.appendChild(node_9);

			var node_10 = document.createElement('DIV');
			node_10.setAttribute('name', 'ordersTable');
			node_10.setAttribute('id', 'ordersTable');
			node_1.appendChild(node_10);

			
			var 	tableSTART='<table id="owners" class="table table-striped" border="2">'
					//	var th='<thead><tr><th style="width: 150px;">Name</th><th style="width: 200px;">Address</th><th>City</th></thead>';
			var b='<tbody id="tbodyRoute">';
			var tableEND='</tbody></table>';
			var table= tableSTART+b+tableEND;
			
			node_10.innerHTML=table
			
			var pages='<div><span>Pages:</span><span>[</span><span><span>1</span></span><span><a href="/owners?page=2">2</a></span><span><a href="/owners?page=3">3</a></span><span>]&nbsp;</span>			  <span><span title="First" class="fa fa-fast-backward"></span></span>			  <span><span title="Previous" class="fa fa-step-backward"></span></span>			  <span><a href="/owners?page=2" title="Next" class="fa fa-step-forward"></a></span>			  <span><a href="/owners?page=3" title="Last" class="fa fa-fast-forward"></a></span>			</div>';			
			
			node_10.innerHTML=table;//+pages;

		return node_1;
		}
	}
