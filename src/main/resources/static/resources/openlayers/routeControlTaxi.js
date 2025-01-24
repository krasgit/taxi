//RouteControl------------------------------------------------	  
	class RouteControlTaxi extends ol.control.Control {

		constructor( opt_options) {
			const options = opt_options || {};

		
			var container = RouteControlTaxi.createContainer(options);
			super({element: container, target: options.target, });
		}

		init() {
			const cWaypoint = document.getElementById('Waypoint');
			var LonLat = [0, 0];
			var feature = RouteControlTaxi._createFeature(LonLat);
			var node_11 = RouteControlTaxi.autoCompleteBtn(feature, 'Start');
			cWaypoint.appendChild(node_11);

			var feature1 = RouteControlTaxi._createFeature(LonLat);
			var node_111 = RouteControlTaxi.autoCompleteBtn(feature1, 'End');
			cWaypoint.appendChild(node_111);
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

			RouteControlTaxi.showFeature(featureId);
			RouteControlTaxi.refresh()
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
			
			static loadOrder(){
				log('loadOrder');
			var json=' {"coord":[{"lon":27.990425599999995,"lat":43.23737599999998,"name":"Васил Стоилов, Дълбокото дере, кв. Виница, Варна, Приморски, Варна, 9006, България"},{"lon":27.96982623476562,"lat":43.23512489505694,"name":"Горна Трака, кв. Виница, Варна, Приморски, Варна, 9022, България"}]}';
			             
			const cWaypoint = document.getElementById('Waypoint');
			
			cWaypoint.innerText="";
			vectorSource.clear();
			const obj = JSON.parse(json);
			

			for (let item of obj.coord) {
				log(item);
				var LonLat = [item.lon, item.lat];
				var feature = RouteControlTaxi._createFeature(LonLat);
				var node_11 = RouteControlTaxi.autoCompleteBtn(feature, item.name);
				cWaypoint.appendChild(node_11);
			}	
			
			
			var json1=' {"coord":[{"lon":27.990425599999985,"lat":43.23737599999998,"name":"Васил Стоилов, Дълбокото дере, кв. Виница, Варна, Приморски, Варна, 9006, България"},{"lon":27.96982623476562,"lat":43.23512489505694,"name":"Горна Трака, кв. Виница, Варна, Приморски, Варна, 9022, България"}]}';
						             
						const cWaypoint1 = document.getElementById('Waypoint');
						
						cWaypoint.innerText="";
						vectorSource.clear();
						const obj1 = JSON.parse(json1);
						

						for (let item of obj1.coord) {
							log(item);
							var LonLat = [item.lon, item.lat];
							var feature = RouteControlTaxi._createFeature(LonLat);
							var node_11 = RouteControlTaxi.autoCompleteBtn(feature, item.name);
							cWaypoint.appendChild(node_11);
						}	

						
						var kLonLat = [27.990425599999995, 43.23737599999998];
						var feature = RouteControlTaxi._createFeature(kLonLat);
						
						var st = getFeatureStyle("waypointName");
																feature.setStyle(st);
						
						
						Route.createRouteCB(this,"27.990425599999995,43.23737599999998;27.96982623476562,43.23512489505694",'routeFeature');
						Route.createRouteCB(this,"27.98390246767578,43.25088088328201;27.909058107324217,43.22461863910661",'routeFeature1');
						
								
			//RouteControlTaxi.refresh();
			}
			
			static createOrder()
			{
				//s://localhost:8443/route/v1/driving/27.98390246767578,43.25088088328201;27.909058107324217,43.22461863910661?overview=full&alternatives=true&steps=true
				Route.createRouteCB(this,"27.98390246767578,43.25088088328201;27.909058107324217,43.22461863910661",'routeFeature1');
				return;
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
									
									var coord = {  "lon": coordinates[0],  "lat": coordinates[1],  "name":bntEl.value }
									order.coord.push(coord);
					
									}
				log('order json '+JSON.stringify(order));	
				
		
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
				RouteControlTaxi.hideFeature(featureId);
				RouteControlTaxi.refresh();
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
		RouteControlTaxi.refresh();
		}

		static add() {
			const cWaypoint = document.getElementById('Waypoint');
			var LonLat = [0, 0];
			var feature = RouteControlTaxi._createFeature(LonLat);
			var node_11 = RouteControlTaxi.autoCompleteBtn(feature, '');
			cWaypoint.appendChild(node_11);
			RouteControlTaxi.refresh();
		}

		static _createFeature(coord) {
			var g = new ol.geom.Point(ol.proj.fromLonLat(coord));
			var feature = new ol.Feature({type: 'place', geometry: g});
			feature.setStyle(new ol.style.Style(null));
			vectorSource.addFeature(feature);
			//var translate1 = new app.Drag({features: new ol.Collection([feature])});
			//map.addInteraction(translate1);

			var ol_uid = feature.ol_uid;
			feature.setId(ol_uid);
			return feature;
		}

		static createFeature() {
			log("createFeature");
			var LonLat = [27.9797311, 43.2388141];
			var feature = RouteControlTaxi._createFeature(LonLat);


			RouteControlTaxi.fid = feature.getId();
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
			node_5.setAttribute('class', 'ref1');
			node_4.appendChild(node_5);

			var node_6 = document.createElement('DIV');
			node_6.setAttribute('class', '');
			node_3.appendChild(node_6);

			var node_7 = document.createElement('SPAN');
			node_7.setAttribute('class', 'ref2');
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
			node_21.setAttribute('onclick', 'RouteControlTaxi.add()');
			node_20.appendChild(node_21);

			var createOrder = document.createElement('BUTTON');
			createOrder.setAttribute('type', 'button');
			createOrder.setAttribute('class', 'btn btn-primary btn-sm');
			createOrder.setAttribute('onclick', 'RouteControlTaxi.createOrder()');
			createOrder.innerHTML = 'CreateOrder';
			node_20.appendChild(createOrder);

			var loadOrder = document.createElement('BUTTON');
				loadOrder.setAttribute('type', 'button');
				loadOrder.setAttribute('class', 'btn btn-primary btn-sm');
				loadOrder.setAttribute('onclick', 'RouteControlTaxi.loadOrder()');
				loadOrder.innerHTML = 'loadOrders';
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
			node_17.setAttribute('onclick', 'RouteControlTaxi.delete(' + featureId + ')');
			node_11.appendChild(node_17);

			var node_18 = document.createElement('I');
			node_18.setAttribute('class', 'fa fa-trash');
			node_17.appendChild(node_18);
			return node_11;
		}
	}
