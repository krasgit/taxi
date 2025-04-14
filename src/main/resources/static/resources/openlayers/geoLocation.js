
	var count = 0;
	var lastGeoLocationEvent;
	var geoPositionOverlay;
	function geoPosition() {
			var markerEl = document.createElement('IMG');
			markerEl.setAttribute('id', 'geolocation_marker_heading');
			markerEl.setAttribute('src', '/resources/openlayers/geolocation_marker_heading.png');
			markerEl.setAttribute('alt', 'geolocation_marker_heading');

			geoPositionOverlay = new ol.Overlay({positioning: 'center-center', element: markerEl, stopEvent: false, });
			map.addOverlay(geoPositionOverlay);

	}
	
	function updatePositionMarker(evJSON) {
		
		let ev=JSON.parse(evJSON);
		
			var coordss = ev.coords;
			//log(coordss);
			const projectedPosition = ol.proj.fromLonLat([coordss.longitude, coordss.latitude]);
			var heading = coordss.heading;
			geoPositionOverlay.setPosition(projectedPosition);
			map.render();
			
			
			var f=RouteControl.getFeatureByIndex(0);
			var geometry =f.getGeometry().clone().transform(map.getView().getProjection(), 'EPSG:4326');
			var coordinates = geometry.flatCoordinates;
							var lngLat = coordinates[0]+','+ coordinates[1];	
			
				const ca = [];
			
				
				startPos=coordinates[0];
				
				if(startPos==0) //no start pozition
					{
						Route.removeAllRoute('route2start');
						return 	
					}
				
				ca.push([coordinates[0], coordinates[1]]);	
				
				ca.push([coordss.longitude, coordss.latitude]);
				
				//[coordss.longitude, coordss.latitude]
				//[coordss.longitude, coordss.latitude]		);					
							
				var path =Route.getPath(ca) 	
				Route.createRoute(path,'route2start');
											
								
		}

	
	var markerOverlay

	function updateGeoMarker() {
		var coordss = lastGeoLocationEvent.coords;
		//log(coordss);
		const projectedPosition = ol.proj.fromLonLat([coordss.longitude, coordss.latitude]);
		var heading = coordss.heading;
		markerOverlay.setPosition(projectedPosition);
		map.render();
	}

	
	function getGeolocationPositionJSON(event)
	{
		let geoPosition = {
		  timestamp: event.timestamp,
		  coords: {accuracy: event.coords.accuracy, latitude: event.coords.latitude,longitude: event.coords.longitude}
		};

		 
		return  JSON.stringify(geoPosition); 
	}
	
	
	function geosuccess(event) {
		if(lastGeoLocationEvent===undefined)
		   lastGeoLocationEvent = event;
		
		var curentCoord = event.coords;
		
		var lastCoord=lastGeoLocationEvent.coords;
		
		var distance=utils.distanceBetweenPoints ([curentCoord.longitude, curentCoord.latitude], [lastCoord.longitude, lastCoord.latitude]);
		
		
		
		
		
		if(distance>1)// TODO UNCOMMENT
			{
			//	document.getElementById("b1").innerHTML=""+distance ;
			//wsUpdatePostion(event);
		
			const gp=getGeolocationPositionJSON(event);	
			//updatePositionMarker(gp);
			updatePostion(gp);
			
			
			
			
			
	        }	
		 lastGeoLocationEvent = event;
		updateGeoMarker();
		//log("geo: " + count++ + " : " + event.coords.heading + " speed:" + event.coords.speed);
		var heading = event.coords.heading;
		var speed = event.coords.speed;

		var sp = Math.round(speed * 3.6)
		var ss = '<svg width="32" height="32"  viewBox="0 0 32 32"  xmlns="http://www.w3.org/2000/svg"><circle cx="16" cy="16" r="14" stroke="blue" stroke-width="4" fill="white"></circle><text x="16" y="20" alignment-baseline="middle" font-size="12" stroke-width="0" stroke="#000" font-weight="bold" text-anchor="middle" fill="black">' + sp + '</text></svg>';
		if (sp >= 10) {
			ss = '<svg width="32" height="32"  viewBox="0 0 32 32"  xmlns="http://www.w3.org/2000/svg"><circle cx="16" cy="16" r="14" stroke="red" stroke-width="4" fill="white"></circle><text x="16" y="20" alignment-baseline="middle" font-size="12" stroke-width="0" stroke="#000" font-weight="bold" text-anchor="middle" fill="black">' + sp + '</text></svg>';
		}
		//const speedctr = document.getElementById('speedctr');
		//speedctr.innerHTML= ss;
		/*
		if (heading != null) {
		   moveCompassNeedle(heading);
			}
		if (speed != null) {
			// update the speed
			moveSpeed(speed);
		}
					*/
	}
	
	function  updatePostion(event){
						
			var user =Cookie.getCookie("user") ;
			var token =Cookie.getCookie("token") ;
						
			callRPC("updatePostion",user,token,event).then((result) => {
			//	RouteControl.loadOrderCB(event); //SELF npls 
			//	RouteControl.loadOrderCB(result); //OLD
     		  
								   });
			}	
	
	
	function geofailure(event) {
		log("geofailure:  : " + event);
	}
	
	function initGeo() {
		var markerEl = document.createElement('IMG');
		markerEl.setAttribute('id', 'geolocation_marker');
		markerEl.setAttribute('src', '/resources/openlayers/geolocation_marker.png');
		markerEl.setAttribute('alt', 'geolocation_marker');

		markerOverlay = new ol.Overlay({positioning: 'center-center', element: markerEl, stopEvent: false, });
		map.addOverlay(markerOverlay);

		geoPosition();
		
		navigator.geolocation.watchPosition(geosuccess, geofailure, {enableHighAccuracy: true, maximumAge: 30000, timeout: 20000}
		); 			//moveSpeed(30);			//moveCompassNeedle(56);
	}

	
	
	
