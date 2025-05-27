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
			
			
			
			const isTaxi=Cookie.getCookie("isTaxi");
			if(isTaxi=='true'){
				cWaypoint.style.display = "none"; 				
								}
			
			
			
			
			
			
			
			
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
			//var color=	style.getStroke().getColor();
				
			const	un= { meters: 'm', kilometers: 'km' }
			var duration=	json.duration;
			var distance=json.distance;
	
			var v =distance; //roundFn(d, sensitivity);
			var  data = {
				    value: v >= 1000 ? (v / 1000) : v,
				    unit: v >= 1000 ? un.kilometers : un.meters
				  };
				
				//var refDistance = document.getElementById("refDistance"+index);
				//refDistance.innerHTML=data.value.toFixed(1) +' '+data.unit +' '+(duration/60).toFixed(1)+' min';
				return data.value.toFixed(1) +' '+data.unit +' '+(duration/60).toFixed(1)+' min';
				
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
					
					
			//		log(coordinates[0]+' '+ coordinates[1] );
            //        log(feature);								
					}
					Route.removeAllRoute('routeFeature');	
					
					
			var createOrderButton = document.getElementById('create-order-button');
					
			if(count>=2)
			{
				 createOrderButton.removeAttribute("disabled");
				
				Route.createRoute(path,'routeFeature');
					var refDistance = document.getElementById("refDistance0").innerHTML='';
					document.getElementById("refDistance1").innerHTML='';
			}
			else {
				createOrderButton.setAttribute("disabled", true);
				var refDistance = document.getElementById("refDistance0").innerHTML='';
					document.getElementById("refDistance1").innerHTML='';
			//dublicate
						var routeFeature = vectorSource.getFeatureById('routeFeature');
						if (routeFeature)
			     		vectorSource.removeFeature(routeFeature);	
					}
								
	//		log("refresh");
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
		//		document.getElementById("demo").innerHTML = err.message;
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
	   		     <td> ${st} ${order.state}  ${order.id} ${order.personName} ${order.taxiName}</td>
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
	   		     <td>${icon} ${order.state} ${order.id} ${order.personName} </td>
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
				RouteControl.render(result); 
			});
			
		}
		
		
		static finishOrder(id){
					
					callRPC("finishOrder",id).then((result) => 
						{	
						RouteControl.render(result); 
					});
					
				}
		//context from to 		
		static sendMessageFromOrder(id,fromId,from,toId,to){
			
			
			MessageControl.init(id,fromId,from,toId,to);
			//	callRPC("sendMessageFromOrder",id).then((result) => 
			//			{	
			//				RouteControl.render(result); 
			//			});
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
			//			taxiId: 1			taxiName: "qaz"
			//			createpersonId: 1	personName: "qaz"
			
			
			let onc=`RouteControl.sendMessageFromOrder(${order.id}	,${order.taxiId},'${order.taxiName}'  ,${order.createpersonId},'${order.personName}');`;
			
			button=`<a href="#" onclick="${onc};" class="button is-primary" id="log-in-button">
						<i class="fa fa-" aria-hidden="true"></i>
						Mesasage 
					</a>
			<a href="#" onclick="RouteControl.startOrder(${order.id});" class="button is-primary" id="log-in-button">
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
											  		<a href="#" onclick="RouteControl.createOrder();" class="button is-primary" id="create-order-button">
													<?xml version="1.0" encoding="utf-8"?>
													<!-- License: GPL. Made by Viglino: https://github.com/Viglino/font-gis -->
													<svg width="16px" height="16px" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img" class="iconify iconify--gis" preserveAspectRatio="xMidYMid meet">
														<path d="M21 32C9.459 32 0 41.43 0 52.94c0 4.46 1.424 8.605 3.835 12.012l14.603 25.244c2.045 2.672 3.405 2.165 5.106-.14l16.106-27.41c.325-.59.58-1.216.803-1.856A20.668 20.668 0 0 0 42 52.94C42 41.43 32.544 32 21 32zm0 9.812c6.216 0 11.16 4.931 11.16 11.129c0 6.198-4.944 11.127-11.16 11.127c-6.215 0-11.16-4.93-11.16-11.127c0-6.198 4.945-11.129 11.16-11.129z" fill="#000000"></path><path d="M87.75 0C81.018 0 75.5 5.501 75.5 12.216c0 2.601.83 5.019 2.237 7.006l8.519 14.726c1.193 1.558 1.986 1.262 2.978-.082l9.395-15.99c.19-.343.339-.708.468-1.082a12.05 12.05 0 0 0 .903-4.578C100 5.5 94.484 0 87.75 0zm0 5.724c3.626 0 6.51 2.876 6.51 6.492c0 3.615-2.884 6.49-6.51 6.49c-3.625 0-6.51-2.875-6.51-6.49c0-3.616 2.885-6.492 6.51-6.492z" fill="#000000"></path><path d="M88.209 37.412c-2.247.05-4.5.145-6.757.312l.348 5.532a126.32 126.32 0 0 1 6.513-.303zm-11.975.82c-3.47.431-6.97 1.045-10.43 2.032l1.303 5.361c3.144-.896 6.402-1.475 9.711-1.886zM60.623 42.12a24.52 24.52 0 0 0-3.004 1.583l-.004.005l-.006.002c-1.375.866-2.824 1.965-4.007 3.562c-.857 1.157-1.558 2.62-1.722 4.35l5.095.565c.038-.406.246-.942.62-1.446h.002v-.002c.603-.816 1.507-1.557 2.582-2.235l.004-.002a19.64 19.64 0 0 1 2.388-1.256zM58 54.655l-3.303 4.235c.783.716 1.604 1.266 2.397 1.726l.01.005l.01.006c2.632 1.497 5.346 2.342 7.862 3.144l1.446-5.318c-2.515-.802-4.886-1.576-6.918-2.73c-.582-.338-1.092-.691-1.504-1.068zm13.335 5.294l-1.412 5.327l.668.208l.82.262c2.714.883 5.314 1.826 7.638 3.131l2.358-4.92c-2.81-1.579-5.727-2.611-8.538-3.525l-.008-.002l-.842-.269zm14.867 7.7l-3.623 3.92c.856.927 1.497 2.042 1.809 3.194l.002.006l.002.009c.372 1.345.373 2.927.082 4.525l5.024 1.072c.41-2.256.476-4.733-.198-7.178c-.587-2.162-1.707-4.04-3.098-5.548zM82.72 82.643a11.84 11.84 0 0 1-1.826 1.572h-.002c-1.8 1.266-3.888 2.22-6.106 3.04l1.654 5.244c2.426-.897 4.917-1.997 7.245-3.635l.006-.005l.003-.002a16.95 16.95 0 0 0 2.639-2.287zm-12.64 6.089c-3.213.864-6.497 1.522-9.821 2.08l.784 5.479c3.421-.575 6.856-1.262 10.27-2.18zm-14.822 2.836c-3.346.457-6.71.83-10.084 1.148l.442 5.522c3.426-.322 6.858-.701 10.285-1.17zm-15.155 1.583c-3.381.268-6.77.486-10.162.67l.256 5.536c3.425-.185 6.853-.406 10.28-.678zm-15.259.92c-2.033.095-4.071.173-6.114.245l.168 5.541a560.1 560.1 0 0 0 6.166-.246z"
														 fill="#ffffff" fill-rule="evenodd">
														</path>
													</svg>
													</a>
													
													
													<i class="fa-light fa-route"></i>
													
													<a href="#" onclick="RouteControl.center();" class="button is-primary" id="log-in-button">
													<?xml version="1.0" encoding="utf-8"?>

													<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">

													<!-- License: Apache. Made by vaadin: https://github.com/vaadin/vaadin-icons -->
													<svg width="16px" height="16px" viewBox="0 0 16 16" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
													<path fill="#ffffff" d="M5.3 6.7l1.4-1.4-3-3 1.3-1.3h-4v4l1.3-1.3z"></path>
													<path fill="#ffffff" d="M6.7 10.7l-1.4-1.4-3 3-1.3-1.3v4h4l-1.3-1.3z"></path>
													<path fill="#ffffff" d="M10.7 9.3l-1.4 1.4 3 3-1.3 1.3h4v-4l-1.3 1.3z"></path>
													<path fill="#ffffff" d="M11 1l1.3 1.3-3 3 1.4 1.4 3-3 1.3 1.3v-4z"></path>
													</svg>
													
													</a>
													
													<div id="!refDistance" class="f_refDistance" style=" display: inline-block;">
														<h6 id="refDistance0" style=" font-size: 12px;color:red;">I am red</h6>
														<h6 id="refDistance1" style=" font-size: 12px;color:blue;">I am blue</h6>
													</div>
													
													<div id="!refDistance" class="f_refDistance" style=" display: inline-block;">
														<h6 id="route2start0" style=" font-size: 12px;color:DarkGreen;"></h6>
														<h6 id="route2start1" style=" font-size: 12px;color:DarkOliveGreen;"></h6>
													</div>
													
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
