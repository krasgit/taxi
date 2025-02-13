//RouteControl------------------------------------------------	  
	class RouteControl extends ol.control.Control {

		constructor( opt_options) {
			const options = opt_options || {};

		
			var container = RouteControl.createContainer(options);
			
			RouteControl.loadOrders();//todo
			
			super({element: container, target: options.target, });
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

		static updateFeature(featureId, coordinates, value) {
			var bntEl = document.getElementById("bnt" + featureId);

			if (value == "")
				reverseGeocoding(coordinates[0], coordinates[1], bntEl, callbackSetInputElVal)
			else
				bntEl.value = value;

			var feature = vectorSource.getFeatureById(featureId);
			var coord = getPointFromLongLat(coordinates[0], coordinates[1]);

			feature.getGeometry().setCoordinates(coord);

			RouteControl.showFeature(featureId);
			RouteControl.refresh()
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
				callRPC("loadOrders",user,token).then((result) => 
					{	
						RouteControl.render(result); });
			}

			
			static loadOrderById(id){
						callRPC("loadOrderById",id,id).then((result) => {	RouteControl.loadOrderCB(result); });
					}
			
			static deleteOrderById(id){
				var user =Cookie.getCookie("user") ;
						var token =Cookie.getCookie("token") ;
							callRPC("deleteOrderById",user,token,id).then((result) => {	RouteControl.loadOrders(); });
			}
			
			static acceptOrder(id){
						
						callRPC("acceptOrderClient",id).then((result) => 
							{	
						//	TaxiControl.render(result); 
						});
						
					}
			
			
			static render(orders)
			{
			/*  orders.id, orders.route, orders.clientid, orders.state,	orders.taxiid, orders.createtime */
			var ordersTable = document.getElementById("tbodyMainRoute");		
			var tableRuws="";
			const jsonData = JSON.parse(orders);
				
			for (var i = 0; i < jsonData.length; i++) 
				{
				var order = jsonData[i];
				
				var orderState =order.state;
				
				var route=order.route;
				const jsonRoute = JSON.parse(route);

				var coord=jsonRoute.coord;
						
				tableRuws+='<tr> <td><a href="/owners/1">'+orderState+'</a></td>';
				tableRuws+='<td style="padding-left: 5px;padding-bottom:3px; font-size: 12px;">';
				for (var ii = 0; ii < coord.length; ii++) 
					{
					var jsonRouteRow = coord[ii];
						tableRuws+=jsonRouteRow.name;
						tableRuws+='<br/>';
					}
				tableRuws+='</td>';
							
				tableRuws+='<td> ';
						if(orderState==0){
							tableRuws+='<button type="button" class="btn btn-primary btn-sm" onclick="RouteControl.deleteOrderById('+order.id+')"> Delete </button> ';
							tableRuws+='<button type="button" class="btn btn-primary btn-sm" onclick=" RouteControl.loadOrderById('+order.id+')"> Show </button> ';
							tableRuws+='<button type="button" class="btn btn-primary btn-sm" onclick="RouteControl.acceptOrder('+order.id+')"> Accept </button>';
							}
							
							
				tableRuws+=	'</td>';
				tableRuws+='</tr>';
				}
			ordersTable.innerHTML=tableRuws;
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
		static refresh()//delete update add move
		{
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
				
			if(count>=2)
			{
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
				RouteControl.hideFeature(featureId);
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

		
		static logIn(){
			var user =Cookie.getCookie("user") ;
			const logoutButton = document.getElementById('log-out-button');
			logoutButton.innerHTML = 'logout:'+user;
			logoutButton.setAttribute('onclick', 'RouteControl.logOut();');
		}
		
		static logOut(){
			var user =Cookie.getCookie("user") ;
			var token =Cookie.getCookie("token") ;
			callRPC("logOut",user,token).then((result) => {
				location.reload();
				   });
			
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
			vectorSource.addFeature(feature);
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

		static showFeature(featureId) {
			log("showFeature");
			var feature = vectorSource.getFeatureById(featureId);

			var bntEl = document.getElementById("bnt" + featureId);
								
				var fn=bntEl.placeholder;
			
			var st = getFeatureStyle(fn);

			feature.setStyle(st);
		}

		static hideFeature(featureId) {
			log("hideFeature");
			var feature = vectorSource.getFeatureById(featureId);
			feature.setStyle(new ol.style.Style(null));
			
			var coord = getPointFromLongLat(0, 0);
					feature.getGeometry().setCoordinates(coord);
			
		}

		static createContainer(options) {

			const mode =options.mode;
			
			var node_1 = document.createElement('DIV');
			node_1.setAttribute('name', 'RouteControl');
			node_1.setAttribute('id', 'control');
			node_1.setAttribute('class', '     border w3-border-red  ');
			node_1.setAttribute('style', ' background-color: lightblue; min-width: 400;position: absolute; right : 0em; top : 0em ');

			var node_2 = document.createElement('DIV');
			node_2.setAttribute('name', 'header');
			node_2.setAttribute('class', 'container ');
			node_2.setAttribute('style', ' ');
			node_1.appendChild(node_2);

			var node_3 = document.createElement('DIV');
			node_3.setAttribute('class', 'd-flex justify-content-between ');
			node_2.appendChild(node_3);

			var node_4 = document.createElement('DIV');
			node_4.setAttribute('class', '');
			node_3.appendChild(node_4);

			var node_5 = document.createElement('SPAN');
			//node_5.setAttribute('class', 'ref1');
			node_4.appendChild(node_5);

			//mode
			
			
			var logoutButton = document.createElement('A');
					logoutButton.setAttribute('href', '#');
					logoutButton.setAttribute('id', 'log-out-button');
					logoutButton.setAttribute('class', 'is-primary');

			if(mode)
				{			
					var user =Cookie.getCookie("user") ;
					logoutButton.setAttribute('onclick', 'RouteControl.logOut();');
					logoutButton.innerHTML = 'logout:'+user;
				}
				else
				{
				logoutButton.innerHTML = 'guest';
				}
			node_5.appendChild(logoutButton);	
			
			
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
			node_10.setAttribute('name', 'Waypoint');
			node_10.setAttribute('id', 'Waypoint');
			node_1.appendChild(node_10);

			var node_19 = document.createElement('DIV');
			node_19.setAttribute('name', 'footer');
			node_19.setAttribute('class', 'd-flex justify-content-between ');
			node_1.appendChild(node_19);

			var node_20 = document.createElement('DIV');
			node_20.setAttribute('class', '');
			node_19.appendChild(node_20);

			var node_21 = document.createElement('BUTTON');
			node_21.setAttribute('type', 'button');
			node_21.setAttribute('class', 'btn btn-primary btn-sm');
			node_21.setAttribute('onclick', 'RouteControl.add()');
			node_20.appendChild(node_21);

			var createOrder = document.createElement('BUTTON');
			createOrder.setAttribute('type', 'button');
			createOrder.setAttribute('class', 'btn btn-primary btn-sm');
			createOrder.setAttribute('onclick', 'RouteControl.createOrder()');
			createOrder.innerHTML = 'CreateOrder';
			node_20.appendChild(createOrder);

			var loadOrder = document.createElement('BUTTON');
				loadOrder.setAttribute('type', 'button');
				loadOrder.setAttribute('class', 'btn btn-primary btn-sm');
				loadOrder.setAttribute('onclick', 'RouteControl.loadOrder()');
				loadOrder.innerHTML = 'loadOrder';
				node_20.appendChild(loadOrder);
			
			var node_22 = document.createElement('SPAN');
			node_22.setAttribute('class', 'refAdd');
			node_21.appendChild(node_22);

			var node_23 = document.createElement('DIV');
			node_23.setAttribute('class', '');
			node_19.appendChild(node_23);

			var node_24 = document.createElement('SPAN');
			node_24.id='refDistance';
			//node_14.setAttribute('id', "refDistance");
			
			node_24.setAttribute('class', 'f_refDistance');
			node_23.appendChild(node_24);
			
			
			
			var route = document.createElement('DIV');
						route.setAttribute('name', 'footer');
						route.setAttribute('class', 'd-flex justify-content-between ');
						route.innerHTML="qaz";
						node_1.appendChild(route);
			
			
			
						
						var 	tableSTART='<table id="owners" style="	height: 50px;  overflow-y: auto;  overflow-x: hidden;" class="table table-striped" border="2">'
										//	var th='<thead><tr><th style="width: 150px;">Name</th><th style="width: 200px;">Address</th><th>City</th></thead>';
								var b='<tbody id="tbodyMainRoute">';
								var tableEND='</tbody></table>';
								var table= tableSTART+b+tableEND;
								
								route.innerHTML=table
			
			
		return node_1;
		}

		static autoCompleteBtn(feature, value) {

			var featureId = feature.getId();

			var node_11 = document.createElement('DIV');

			node_11.reature
			node_11.setAttribute('class', 'input-group mb-1 mt-1');

			node_11.setAttribute('id', "inputGroupfeatureId" + featureId);
			node_11.feature = feature;
			node_11.featureId = featureId;
			
			var node_12 = document.createElement('SPAN');
			node_12.setAttribute('class', 'input-group-text');
			node_12.setAttribute('style', 'display:none1');
			node_11.appendChild(node_12);

			var node_13 = document.createElement('IMG');
			node_13.setAttribute('src', 'data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==');
			node_13.setAttribute('alt', 'Red dot');
			node_12.appendChild(node_13);

			var node_14 = document.createElement('INPUT');
			node_14.setAttribute('type', 'text');
			node_14.setAttribute('class', 'form-control');

			node_14.setAttribute('id', "bnt" + featureId);
			node_14.setAttribute('featureId', featureId);

			//node_14.setAttribute('placeholder', value);
			
			if( !(!value || value.length === 0 ))
				node_14.value=value;

			node_11.appendChild(node_14);

			autocomplete(node_14);

			var node_15 = document.createElement('BUTTON');
			node_15.setAttribute('type', 'button');
			node_15.setAttribute('class', ' btn btn-primary btn-sm');
			node_11.appendChild(node_15);

			var node_16 = document.createElement('IMG');
			node_16.setAttribute('class', 'geolocation_marker');
			node_15.appendChild(node_16);

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
	}
