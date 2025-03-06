	class RouteControl  {
		role;
		constructor( opt_options) {
			const options = opt_options || {};

		
		//	var container = RouteControl.createContainer(options);
			
		//	RouteControl.loadOrders();//todo
			
		//	super({element: container, target: options.target, });
		}

		
		static get() {
			var LonLat = [0, 0];
						var feature = RouteControl._createFeature(LonLat);
						var node_11 = RouteControl.autoCompleteBtnf(feature, '');
						
						return node_11;
		}
		static autoCompleteBtnf(feature, value) {

					var featureId = feature.getId();

					var node_11 = document.createElement('DIV');

					node_11.reature
					node_11.setAttribute('class', 'input-group mb-1 mt-1');

					node_11.setAttribute('id', "inputGroupfeatureId" + featureId);
					node_11.feature = feature;
					node_11.featureId = featureId;
					/*
					var node_12 = document.createElement('SPAN');
					node_12.setAttribute('class', 'input-group-text');
					node_12.setAttribute('style', 'display:none1');
					node_11.appendChild(node_12);

					
					var node_13 = document.createElement('IMG');
					node_13.setAttribute('src', 'data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==');
					node_13.setAttribute('alt', 'Red dot');
					node_12.appendChild(node_13);
		            */
					var node_14 = document.createElement('INPUT');
					node_14.setAttribute('type', 'text');
					node_14.setAttribute('class', 'form-control');
					node_14.setAttribute('style', 'min-width: 320px');
					node_14.setAttribute('id', "bnt" + featureId);
					node_14.setAttribute('featureId', featureId);

					//node_14.setAttribute('placeholder', value);
					
					if( !(!value || value.length === 0 ))
						node_14.value=value;

					node_11.appendChild(node_14);

					autocomplete(node_14);

					/*
					var node_15 = document.createElement('BUTTON');
					node_15.setAttribute('type', 'button');
					node_15.setAttribute('class', ' btn btn-primary btn-sm');
					node_11.appendChild(node_15);

					var node_16 = document.createElement('IMG');
					node_16.setAttribute('class', 'geolocation_marker');
					node_15.appendChild(node_16);
					*/
					var node_17 = document.createElement('BUTTON');
					node_17.setAttribute('type', 'button');
					node_17.setAttribute('class', 'btn btn-primary btn-sm');
					node_17.setAttribute('onclick', 'RouteControl.delete(' + featureId + ')');
					node_11.appendChild(node_17);

					var node_18 = document.createElement('I');
					node_18.setAttribute('class', 'fa fa-trash');
					node_17.appendChild(node_18);
					return node_11;
				}
		
		
		init() {
			const cWaypoint = document.getElementById('Waypoint');
			var LonLat = [0, 0];
			var feature = RouteControl._createFeature(LonLat);
			var node_11 = RouteControl.autoCompleteBtn(feature, '');
			cWaypoint.appendChild(node_11);

			var feature1 = RouteControl._createFeature(LonLat);
			var node_111 = RouteControl.autoCompleteBtn(feature1, '');
			cWaypoint.appendChild(node_111);
			
			RouteControl.refresh();
		}

		static updateFeature(featureId) {
			/*
			var bntEl = document.getElementById("bnt" + featureId);

			if (value == ""){
				reverseGeocoding(coordinates[0], coordinates[1], bntEl, callbackSetInputElVal)
				//reverseGeocodingProton(coordinates[0], coordinates[1], bntEl, callbackSetInputElVal)
			}else{
				bntEl.value = value;
}
			var feature = vectorSource.getFeatureById(featureId);
			var coord = getPointFromLongLat(coordinates[0], coordinates[1]);

			feature.getGeometry().setCoordinates(coord);

			RouteControl.showFeature(featureId);
			*/
			RouteControl.refresh()
		}
		
        //deprecate
		static showFeature(featureId) {
			log("showFeature");
			
			var feature = vectorSource.getFeatureById(featureId);

			var bntEl = document.getElementById("bnt" + featureId);
								
				var fn=bntEl.placeholder;
			
			var st = getFeatureStyle(fn);

			feature.setStyle(st);
		}
		//deprecate
		static hideFeature(featureId) {
			log("hideFeature");
			var feature = vectorSource.getFeatureById(featureId);
			feature.setStyle(new ol.style.Style(null));
			
			var coord = getPointFromLongLat(0, 0);
					feature.getGeometry().setCoordinates(coord);
			
		}
		
		
		
		//kilometers: "km",meters: "m"
		static updateRouteInfo(json)
			{
			const	un= { meters: 'm', kilometers: 'km' }
			var duration=	json.duration;
			var distance=json.distance;
	
			var v =distance; //roundFn(d, sensitivity);
			var  data = {
				    value: v >= 1000 ? (v / 1000) : v,
				    unit: v >= 1000 ? un.kilometers : un.meters
				  };
				
				var refDistance = document.getElementById("refDistance");
				refDistance.innerHTML=data.value.toFixed(1) +' '+data.unit +' '+(duration/60).toFixed(1)+' min';
				
			}	
			
			
			static loadOrderCB(json){
				
				const cWaypoint = document.getElementById('Waypoint');
							
							cWaypoint.innerText="";
							vectorSource.clear();
							const obj = JSON.parse(json);
							

							for (let item of obj.coord) {
								log(item);
								var LonLat = [item.lon, item.lat];
								var feature = RouteControl._createFeature(LonLat);
								var node_11 = RouteControl.autoCompleteBtn(feature, item.name);
								cWaypoint.appendChild(node_11);
							}						
							RouteControl.refresh();
			}
			
			static loadOrder(){
							
				var user =Cookie.getCookie("user") ;
				var token =Cookie.getCookie("token") ;
							
				callRPC("loadOrder",user,token).then((result) => {
									
										  log( result);
										  RouteControl.loadOrderCB(result);
									   });
				}	 

			
			static createOrder()
			{
				var order = {};
				var coord = []
				order.coord = coord;

				
				var inputGroupfeatureIdEl = document.getElementById("Waypoint");
				var features=utils.elementChildren(inputGroupfeatureIdEl);
						
				let path = "";	
				var count=0;
				var featuresLength=features.length;
				for (let i = 0; i < featuresLength; i++) {
								
									var feature =features[i];
									
									var f=feature.feature;
									var featureId=	f.getId();	 
									
									var bntEl = document.getElementById("bnt" + featureId);
									var waypointName=utils.getWaypointName(i,featuresLength);
									bntEl.placeholder=waypointName;
									
									var st = getFeatureStyle(waypointName);
										f.setStyle(st);
									
									
									var geometry =f.getGeometry().clone().transform(map.getView().getProjection(), 'EPSG:4326');
									var coordinates = geometry.flatCoordinates;
									
									var coord = {  "lon": coordinates[0],  "lat": coordinates[1],  "name":bntEl.value ,"waypointName":waypointName }
									order.coord.push(coord);
					
									}
				log('order json '+JSON.stringify(order));	
				
				//wsCreateOrder(JSON.stringify(order));
				var user =Cookie.getCookie("user") ;
				var token =Cookie.getCookie("token") ;
				var orderStr=JSON.stringify(order);
						callRPC("createOrder",user,token,orderStr).then((result) => {
							RouteControl.loadOrders()
						   });
			}
			
			
			static loadOrders()
			{
			var user =Cookie.getCookie("user") ;
			var token =Cookie.getCookie("token") ;
			//client
				callRPC("loadOrders",user,token).then((result) => 
					{	
						RouteControl.render(result); });
			//taxi			
			var user =Cookie.getCookie("user") ;
				var token =Cookie.getCookie("token") ;
					callRPC("loadTaxiOrders",user,token).then((result) => {	RouteControl.TaxiRender(result); });			
						
						
						
			}

			
		
			
			static loadOrderById(id){
						callRPC("loadOrderById",id,id).then((result) => {	RouteControl.loadOrderCB(result); });
					}
			
			static deleteOrderById(id){
				var user =Cookie.getCookie("user") ;
						var token =Cookie.getCookie("token") ;
							callRPC("deleteOrderById",user,token,id).then((result) => {	RouteControl.loadOrders(); });
			}
			
			static acceptOrderClient(id){
						
						callRPC("acceptOrderClient",id).then((result) => 
							{	
						//	TaxiControl.render(result); 
						});
						
					}
			
			static acceptOrder(id){
											
					callRPC("acceptOrder",id).then((result) => 
												{	
											//	TaxiControl.render(result); 
											});
											
										}			
			
			static startOrder(id){
				callRPC("startOrder",id).then((result) => 
			{	
		//	TaxiControl.render(result); 
			});
																					
																				}			
										
				
			static loadOrder(){
				var user =Cookie.getCookie("user") ;
				var token =Cookie.getCookie("token") ;
								
				callRPC("loadOrder",user,token).then((result) => 
					{  
						RouteControl.loadOrderCB(result);   
					});
			}	 	
			
			
			static getFeatureByIndex(i)//delete update add move
				{
					var inputGroupfeatureIdEl = document.getElementById("Waypoint");
					var features=utils.elementChildren(inputGroupfeatureIdEl);
					var feature =features[i];
					var f=feature.feature;
				return f;
				}	
	
				
	static removeAllRoute(){
		var f=vectorSource.getFeatures();
		for (var i = 0; i < f.length; i++) {
		var dd=f[i];		
		var routeFeatureId=''+dd.getId(); //cast to string
		var routeFeature = vectorSource.getFeatureById(routeFeatureId);
		vectorSource.removeFeature(routeFeature);
		}			
				  }				
				
  static refresh()//delete update add move
		{
			
			RouteControl.removeAllRoute();
			
			
			var inputGroupfeatureIdEl = document.getElementById("Waypoint");
			var features=utils.elementChildren(inputGroupfeatureIdEl);
		
			let path = "";	
			var count=0;
			var featuresLength=features.length;
			
			const ca = [];
			
				for (let i = 0; i < featuresLength; i++) {
					var feature =features[i];
					var f=feature.feature;
					var featureId=	f.getId();	 
					var bntEl = document.getElementById("bnt" + featureId);
					var waypointName=utils.getWaypointName(i,featuresLength);
					bntEl.placeholder=waypointName;
					
					var st = getFeatureStyle(waypointName);
						f.setStyle(st);
					
					
					var geometry =f.getGeometry().clone().transform(map.getView().getProjection(), 'EPSG:4326');
					var coordinates = geometry.flatCoordinates;
					
					/** todo
					var st=f.style;
										if(st!=null)
										*/
					if(coordinates[0]!=0){			
						
					
								var bntEl = document.getElementById("bnt" + featureId);
													
									var fn=bntEl.placeholder;
								
								var st = getFeatureStyle(fn);

								f.setStyle(st);
						
						vectorSource.addFeature(f);
							
									}
										
					if(coordinates[0]!=0)
					{
						if(path != "")
						path+=";";
						
						var lngLat = coordinates[0]+','+ coordinates[1];
						count++;
						path += lngLat;
						ca.push([coordinates[0], coordinates[1]])
					}
					
					
					log(coordinates[0]+' '+ coordinates[1] );
                    log(feature);								
					}
					Route.removeAllRoute('routeFeature');	
			if(count>=2)
			{
				//create-order-button
				
				Route.createRoute(path,'routeFeature');
					var refDistance = document.getElementById("refDistance").innerHTML='';
			}
			else {
			var refDistance = document.getElementById("refDistance").innerHTML='';
			//dublicate
						var routeFeature = vectorSource.getFeatureById('routeFeature');
						if (routeFeature)
			     		vectorSource.removeFeature(routeFeature);	
					}
								
			log("refresh");
		}
		
		static delete(featureId) {
			log("delete featureId " + featureId);
			var bntEl = document.getElementById("bnt" + featureId);

			if (bntEl.value != "") {
				bntEl.value = "";
				//RouteControl.hideFeature(featureId);
				var feature = vectorSource.getFeatureById(featureId);
				var coord = getPointFromLongLat(0, 0);
									feature.getGeometry().setCoordinates(coord);
				
				
				RouteControl.refresh();
				return;
			}

			try {
				//0
				var inputGroupfeatureIdEl = document.getElementById("Waypoint");
				if (utils.elementChildren(inputGroupfeatureIdEl).length <= 2)
					return
				//1
				var inputGroupfeatureIdEl = document.getElementById("inputGroupfeatureId" + featureId);
				inputGroupfeatureIdEl.parentNode.removeChild(inputGroupfeatureIdEl);

				var feature = vectorSource.getFeatureById(featureId);
				vectorSource.removeFeature(feature);


			}
			catch (err) {
				document.getElementById("demo").innerHTML = err.message;
			}
		RouteControl.refresh();
		}

		
	
		
		static logOut(){
			var user =Cookie.getCookie("user") ;
			var token =Cookie.getCookie("token") ;
			
			
				Cookie.setCookie("user","");
				Cookie.setCookie("token","");
			
			
			try{ //todo WebSocket is already in CLOSING or CLOSED state.
			
			callRPC("logOut",user,token).then((result) => {
			//	location.reload();
				   });
				  } catch (error) {
				    console.error(error);
				    // Expected output: ReferenceError: nonExistentFunction is not defined
				    // (Note: the exact output may be browser-dependent)
				  }
 
				  location.reload();
       }
		
		static add() {
			const cWaypoint = document.getElementById('Waypoint');
			var LonLat = [0, 0];
			var feature = RouteControl._createFeature(LonLat);
			var node_11 = RouteControl.autoCompleteBtn(feature, '');
			cWaypoint.appendChild(node_11);
			RouteControl.refresh();
		}

		static _createFeature(coord) {
			var g = new ol.geom.Point(ol.proj.fromLonLat(coord));
			var feature = new ol.Feature({type: 'place', geometry: g});
			feature.setStyle(new ol.style.Style(null));
		//	vectorSource.addFeature(feature);
			var translate1 = new app.Drag({features: new ol.Collection([feature])});
			map.addInteraction(translate1);

			var ol_uid = feature.ol_uid;
			feature.setId(ol_uid);
			return feature;
		}

		static createFeature() {
			log("createFeature");
			var LonLat = [27.9797311, 43.2388141];
			var feature = RouteControl._createFeature(LonLat);


			RouteControl.fid = feature.getId();
			vectorSource.getFeatureById(featureId);
			log("createFeature");

		}
		
		static test(){
			var f=vectorSource.getFeatures();
								
								for (var i = 0; i < f.length; i++) {
									var dd=f[i];		
								
									var routeFeatureId=''+dd.getId(); //cast to string
									
									log("fuond rute "+routeFeatureId)
										
						}	 
}
		

		static role(role) {
			log(role);
       }
		
	   
	   
	   static logIn(){
	   		var user =Cookie.getCookie("user") ;
	   		const logoutButton = document.getElementById('log-out-button');
			
			var logautlink=`<a href="#" onclick="RouteControl.logOut();">logout:+${user}</a>`;
			
			//todo taxi indicator
	   		//if(Cookie.getCookie("isTaxi")=='true')
	   		// buttonContent+='<i class="fa fa-taxi" style="font-size:10px"></i>';
	   		
	   		logoutButton.innerHTML = logautlink;
	   		
			//todo separate init 
	   		const role = document.getElementById('role');
			if(Cookie.getCookie("isTaxi")=='true')
							role.innerHTML='<a href="#Foo" onclick="RouteControl.role(false)">taxi</a>';
							else
							role.innerHTML='<a href="#Foo" onclick="RouteControl.role(true)">qaz</a>';
	   		
	   		
	   		RouteControl.loadOrders();
	   	}
	   
		
		
		//todo move
		static createElementFromHTML(htmlString) {
		  var div = document.createElement('div');
		  div.innerHTML = htmlString.trim();

		  // Change this to div.childNodes to support multiple top-level nodes.
		  return div.firstChild;
		}
		
		static autoCompleteBtn(feature, value) {

			var featureId = feature.getId();

			var node_11 = document.createElement('DIV');

			node_11.reature
			node_11.setAttribute('class', 'input-group mb-1 mt-1');

			node_11.setAttribute('id', "inputGroupfeatureId" + featureId);
			node_11.feature = feature;
			node_11.featureId = featureId;
			/*
			var node_12 = document.createElement('SPAN');
			node_12.setAttribute('class', 'input-group-text');
			node_12.setAttribute('style', 'display:none1');
			node_11.appendChild(node_12);

			
			var node_13 = document.createElement('IMG');
			node_13.setAttribute('src', 'data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==');
			node_13.setAttribute('alt', 'Red dot');
			node_12.appendChild(node_13);
            */
			var node_14 = document.createElement('INPUT');
			node_14.setAttribute('type', 'text');
			node_14.setAttribute('class', 'form-control');

			node_14.setAttribute('id', "bnt" + featureId);
			node_14.setAttribute('featureId', featureId);

			node_14.feature = feature;
			
			
			//node_14.setAttribute('placeholder', value);
			
			if( !(!value || value.length === 0 ))
				node_14.value=value;

			node_11.appendChild(node_14);

			autocomplete(node_14);

			var deleteBtn =`<a onclick="RouteControl.delete(${featureId})" class="button is-primary" id=""><i class='fa fa-trash'></i></a>`
			
			node_11.appendChild(RouteControl.createElementFromHTML(deleteBtn));

			return node_11;
		}
		
		
		
		
  static orders(order){
	
	
	
	
	var  tableRuws=`
		<table id="owners" style="	height: 50px;  overflow-y: auto;  overflow-x: hidden;" class="table table-striped" border="2">
	   <tbody">
	   
	   </tbody>
	   	</table>`;
	   
  }	
  
  static getRouteName(route){
	var ret='';
	
	try {
	const jsonRoute = JSON.parse(route);
	var coord=jsonRoute.coord;
	} catch (error) {
	  console.error(error);
	  return  "error";
	}
	
    for (var ii = 0; ii < coord.length; ii++){
      var jsonRouteRow = coord[ii];
	    ret+=jsonRouteRow.name+'<br/>';
	}
	return ret;
   }
  	
   
//---------------------------------------------------------------------------------------------------------------		
static createInnerOrders()
{
	return 	class Orders {
	   		constructor() {
	   		  this.row="";
	   		 }
	   	addRow(order){
	   		
		var st="";
		if(order.state==1){
			st='<i class="fa fa-hourglass-start" aria-hidden="true"></i>';
			}
		if(order.state==2){
			st='<i class="fa fa-check" aria-hidden="true"></i>';	
		}
		if(order.state==3)
			{
				
					st='<i class="fa fa-check-circle-o" aria-hidden="true"></i>';
			}
			
		
		
		
		
			
	   	var routeName=RouteControl.getRouteName(order.route);
	   	var	tableRuws=
	   		  `<tr> 
	   		     <td>${order.state}  ${st}</td>
	   		     <td style="padding-left: 5px;padding-bottom:3px; font-size: 12px;">
	   		       <a href="#Foo" onclick="RouteControl.loadOrderById(${order.id})">
	   			   ${routeName}
	   			   
	   			   </a>
	   		      </td>
	   		      <td>
				  
				  <a href="#" onclick="RouteControl.deleteOrderById(${order.id});" class="button is-primary" id="log-in-button"><i class="fa fa-trash" aria-hidden="true"></i> </a>
	   		    										
									
	   				</td>
	   			 </tr>`;
	   			this.row += tableRuws;
	       }
	   		  
	       getOrderTable(){
	   		
	   		if (this.row === "") {
	   		 return "";
	   		}
	   		
	   		
	         var  ret=this.row;
	   				
	   			
	          return ret ;	  
	   		}

	}
	}
	
	static createInnerTemplates()
	{
		return 	class Orders {
		   		constructor() {
		   		  this.row="";
		   		 }
		   	addRow(order){
		   		
		   	var routeName=RouteControl.getRouteName(order.route);
		   	var	tableRuws=
		   		  `<tr> 
		   		     <td><i class="fa fa-book" aria-hidden="true"></i></td>
		   		     <td style="padding-left: 5px;padding-bottom:3px; font-size: 12px;">
		   		       <a href="#Foo" onclick="RouteControl.loadOrderById(${order.id})">
		   			   ${routeName}
		   			   
		   			   </a>
		   		      </td>
		   		      <td>
					  
					  <a href="#" onclick="RouteControl.deleteOrderById(${order.id});" class="button is-primary" id="log-in-button"><i class="fa fa-trash" aria-hidden="true"></i> </a>
					  <a href="#" onclick="RouteControl.acceptOrderClient(${order.id});" class="button is-primary" id="log-in-button"><i class="fa fa-sign-in" aria-hidden="true"></i> </a>
		   				</td>
		   			 </tr>`;
		   			this.row += tableRuws;
		       }
		   		  
		       getOrderTable(){
		   		
		   		if (this.row === "") {
		   		 return "";
		   		}
		   		
		   		
		         var  ret=this.row;
		   				
		   			
		          return ret ;	  
		   		}

		}
		}		
		
//-----------------------------------------------------------------------------

static createOrdersEx()
{
	return 	class Orders {
	   		constructor() {
	   		  this.row="";
	   		 }
	   	addRow(order,icon,button){
	   		
	   	var routeName=RouteControl.getRouteName(order.route);
	   	var	tableRuws=
	   		  `<tr> 
	   		     <td>${order.state}  ${icon}</td>
	   		     <td style="padding-left: 5px;padding-bottom:3px; font-size: 12px;">
	   		       <a href="#Foo" onclick="RouteControl.loadOrderById(${order.id})">
	   			   ${routeName}
	   			   
	   			   </a>
	   		      </td>
	   		      <td>
				  
		${button}
	   		    										
									
	   				</td>
	   			 </tr>`;
	   			this.row += tableRuws;
	       }
	   		  
	       getOrderTable(){
	   		
	   		if (this.row === "") {
	   		 return "";
	   		}
	   		
	   		
	         var  ret=this.row;
	   				
	   			
	          return ret ;	  
	   		}

	}
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

  static TaxiRender(orders){
    const InnerOrders1= RouteControl.createOrdersEx();//1 STATE_CLIENT_START
	const innerOrders1=new InnerOrders1();
				
	const InnerOrders2= RouteControl.createOrdersEx();//2 STATE_TAXI_ACCEPTED
	const innerOrders2=new InnerOrders2();
				
    var ordersTable = document.getElementById("taxiOrder");		
	
	if(orders==null){
				ordersTable.innerHTML="";
				return;
	  }
	  
	  
	  const jsonData = JSON.parse(orders);
	    
    for (var i = 0; i < jsonData.length; i++){
	  var order = jsonData[i];
	  var orderState =order.state;
	      
	  var icon,button
	  switch (orderState) {
        case 1: 
				icon='<i class="fa fa-hourglass-start" aria-hidden="true"></i>';
				button=`<a href="#" onclick="RouteControl.acceptOrder(${order.id});" class="button is-primary" id="log-in-button">
                          <i class="fa fa-trash" aria-hidden="true"></i>
						  Accept 
						</a>`;
		
			innerOrders1.addRow(order,icon,button);	break;
	    case 2: 
			icon='<i class="fa fa-check" aria-hidden="true"></i>';
			button=`<a href="#" onclick="RouteControl.startOrder(${order.id});" class="button is-primary" id="log-in-button">
					<i class="fa fa-" aria-hidden="true"></i>
						Start 
								</a>
			<a href="#" onclick="RouteControl.finishOrder(${order.id});" class="button is-primary" id="log-in-button">
						<i class="fa fa-" aria-hidden="true"></i> 
                         Finish
					</a>`;
		innerOrders2.addRow(order,icon,button);break;
		
		case 3: 
				icon='<i class="fa fa-check" aria-hidden="true"></i>';
				button=`<a href="#" onclick="RouteControl.finishOrder(${order.id});" class="button is-primary" id="log-in-button">
							<i class="fa fa-" aria-hidden="true"></i> 
		                       Finish
						</a>`;
			innerOrders2.addRow(order,icon,button);break;
		
		
	         default:
	     }
	     	}
	  	
		var trOrder1=innerOrders1.getOrderTable();									
	  	var trOrder2=innerOrders2.getOrderTable();
	  	
		var  table=`<table id="owners" style="	height: 50px;  overflow-y: auto;  overflow-x: hidden;" class="table table-striped" border="2">
	  			      <tbody">
					    ${trOrder1}
					    ${trOrder2}
                      </tbody>
					</table>`;			
	  					
	  		ordersTable.innerHTML=table;
	  
  }				
//-----------------------------------------------------------------------------		
  static render(orders)
  {
	
	const InnerTemplates= RouteControl.createInnerTemplates();//0 STATE_CREATED
	const innerTemplates=new InnerTemplates();
		
	const InnerOrders= RouteControl.createInnerOrders();//1 STATE_CLIENT_START
	const innerOrders=new InnerOrders();
	
	
	const InnerOrders2= RouteControl.createInnerOrders();//2 STATE_TAXI_ACCEPTED
	const innerOrders2=new InnerOrders();
	
	const InnerOrders3= RouteControl.createInnerOrders();//2 STATE_TAXI_ACCEPTED
		const innerOrders3=new InnerOrders();
	
	
  var ordersTable = document.getElementById("tbodyMainRoute");		
  if(orders==null){
	
	ordersTable.innerHTML="";
    
	return;
	}
	
  
  const jsonData = JSON.parse(orders);
  
  for (var i = 0; i < jsonData.length; i++){
    var order = jsonData[i];
    var orderState =order.state;
    
	var icon
    switch (orderState) {
       case 0: icon='<i class="fa fa-book" aria-hidden="true"></i>'; innerTemplates.addRow(order); break;
       case 1: icon='<i class="fa fa-hourglass-start" aria-hidden="true"></i>';innerOrders.addRow(order); 	 break;
       case 2: icon='<i class="fa fa-check" aria-hidden="true"></i>';innerOrders2.addRow(order);break;
       case 3: icon='<i class="fa fa-check" aria-hidden="true"></i>';innerOrders3.addRow(order);break;
       default:
   }
   	}
	
		var trTemplates=innerTemplates.getOrderTable();				
		var trOrder=innerOrders.getOrderTable();
		var trOrder2=innerOrders2.getOrderTable();									
		var trOrder3=innerOrders3.getOrderTable();
		
		
			var  table=`<table id="owners" style="	height: 50px;  overflow-y: auto;  overflow-x: hidden;" class="table table-striped" border="2">
						    <tbody">
							${trTemplates}
							${trOrder}
							${trOrder2}
							${trOrder3}
					 </tbody></table>`;			
	
					
		ordersTable.innerHTML=table;
	}
		
	
	static center() {
		
		var layerExtent = vectorSource.getExtent();

		if (layerExtent) {
		    map.getView().fit(layerExtent);
		}
		}
		static createContainerEx(options) {

			const mode =options.mode;

					
					var footer=`<footer class="card-footer" style="padding: 0.3rem;">
										<div class="@card-footer-item" style="padding: 0.2rem;">
										<a href="#" onclick="RouteControl.add();"         class="button is-primary" id="r-in-button">	  
														<i class="fa fa-plus" aria-hidden="true"></i>
													</a>
											&nbsp;
											  		<a href="#" onclick="RouteControl.createOrder();" class="button is-primary" id="create-order-button">CreateOrder</a>
													<a href="#" onclick="RouteControl.center();" class="button is-primary" id="log-in-button">center</a>
													
											<span id="refDistance" class="f_refDistance"></span>
										</div>
									</footer>`;				
				
									
										
var lc=`
<div>			      
  <div>
    <div name="Waypoint" id="Waypoint"></div>
      ${footer}
  </div>
  
  <div id="tbodyMainRoute">
 
  </div>
  
  <div id="taxiOrder">

   </div>
  
  
</div>`;									
			return lc;
			}
		
	}
