//readme https://www.liedman.net/leaflet-routing-machine/api/

var map = L.map('map').setView([43.193163, 27.850342], 13);



	const kurl = new URL(location.origin);


L.tileLayer('https://'+kurl.hostname+':8443/tile/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

/*
L.marker([51.5, -0.09]).addTo(map)
    .bindPopup('A pretty CSS popup.<br> Easily customizable.')
    .openPopup();
*/    
    
    
    
var control = L.Routing.control({
	//	router: L.routing.osrmv1(	{serviceUrl: 'http://127.0.0.1:5000/route/v1'	}),
	router: L.routing.osrmv1(	{serviceUrl: 'https://'+kurl.hostname+':8443/route/v1'	}),
		plan: L.Routing.plan(waypoints = [	]
		, {
			createMarker: function(i, wp) {
				return L.marker(wp.latLng, {
					draggable: true,
					icon: L.icon.glyph({ glyph: String.fromCharCode(65 + i) })
				});
			},
			geocoder: L.Control.Geocoder.nominatim(),
			routeWhileDragging: true
		}),
		
		routeWhileDragging: true,
		  fitSelectedRoutes: false,
		routeDragTimeout: 250,
		showAlternatives: true,
		altLineOptions: {
			styles: [
				{color: 'black', opacity: 0.15, weight: 9},
				{color: 'white', opacity: 0.8, weight: 6},
				{color: 'blue', opacity: 0.5, weight: 2}
			]
		}
	})




	control.addTo(map)
	.on('routingerror', function(e) {
		try {
			map.getCenter();
		} catch (e) {
			map.fitBounds(L.latLngBounds(waypoints));
		}

		handleError(e);
	});
    
    
 /*   
    
    var stateFilterIda = L.easyButton({
states: [{
      title: 'Filtrar rota ida',
      icon:      'fa-long-arrow-right',               // and define its properties
      onClick: function() {       // and its callback
        control.getRouter().options.autoRoute = false;
        control.route();  
      }
  }]});
stateFilterIda.addTo(map);
   */ 
    
    
    
    
    
    
    
    /*hide control
	    
    const routingControlContainer = control.getContainer()
	const controlContainerParent = routingControlContainer.parentNode
	controlContainerParent.removeChild(routingControlContainer)
    
    */
    /*
   	const popup = L.popup().setLatLng([43.516689, 27.817383]).setContent('I am a standalone popup.').openOn(map);


	function onMapClick(e) {
	var waypointsArray = control.getWaypoints();
	
		popup
			.setLatLng(e.latlng)
			.setContent(`You clicked the map at ${e.latlng.toString()}`)
			.openOn(map);
	}

	map.on('click', onMapClick);
	*/
	