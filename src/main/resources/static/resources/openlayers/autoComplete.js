
var br = 2;

function fetchMAddresscbb(inputEl, autocompleteList) {
	var q = inputEl.value;
	var url = 'http'+getProtocolSuffix()+'://' + hostname + ':8443/api?q=Varna ' + q;
	log("fetchMAddresscbb url:" + url)
	fetch(url)
		.then(function (response) {
			return response.json();
		})
		.then(function (json) {
			log(json);
			newinitItem(autocompleteList, json, inputEl);
		})
}


function getAddressProton(address)
	{
		const name = address.name || 'N/A';
		const housenumber = address.housenumber || 'N/A';
		const street = address.street || 'N/A';
		const road = address.road || '';
		const district = address.district || 'N/A';
		const county = address.county || 'Unknown County';
		const city = address.city || 'N/A';
		const state = address.state || 'N/A';
		const postcode = address.postcode || 'N/A';
		const country = address.country || 'Unknown Country';

		const locality = address.locality || 'N/A';
			
		
		
		// If street is missing, fall back to combining housenumber and road
		let finalStreet = street;
		if (street === 'N/A' && (housenumber !== 'N/A' || road !== 'N/A')) {
		    finalStreet = `${housenumber} ${road}`.trim(); // Combine housenumber and road, remove extra spaces
		}

		//const addressParts = [name, finalStreet, district, county, city, state, postcode, country]
		const addressParts = [name, finalStreet, locality,district, county, city, state]
		  .filter(part => part !== 'N/A' && part !== 'Unknown County' && part !== 'Unknown Country' && part !== 'Варна'); // Remove 'N/A' and fallback values

		const formattedAddress = addressParts.join(', ');

		console.log(formattedAddress);
		
		return 	formattedAddress;
	}
	
	/*
	Allowed parameters are: [debug, query_string_filter, limit, distance_sort, osm_tag, lon, lang, radius, lat, layer]"
	*/
	function reverseGeocodingProton(lon, lat, el, callbackFN) {

			var url = 'http+getProtocolSuffix()+://' + hostname + ':8443/reverse?lon=' + lon + '&lat=' + lat+'&radius=1' ;
			log("reverseGeocodingProton url:" + url)
			fetch(url)
				.then(function (response) {
					return response.json();
				})
				.then(function (json) {
					
					var root = json.features;
					var value = root[0];
							var pr = value.properties;
					var	display_name=	getAddressProton(pr);
					callbackFN(el, display_name);
				})
		}




function newinitItem(autocompleteList, json, inputEl) {

	
	log("newinitItem"+json);
	var root = json.features;
	log("newinitItem"+root);
	for (i = 0; i < root.length; i++) {
		var value = root[i];
		var pr = value.properties;

		var disp =getAddressProton(pr);
		
	//	var disp = pr.name + pr.state;
		
	//var disp =pr.housenumber+', '+pr.district+', '+pr.county
		
	// disp+='('+pr.name+','+pr.street+', '+pr.district+', '+pr.county+')';
	
		
		
		

		b = document.createElement("DIV");
		b.innerHTML = "<strong>" + disp.substr(0, inputEl.value.length) + "</strong>";
		b.innerHTML += disp.substr(inputEl.value.length);
		b.innerHTML += "<input type='hidden' value='" + disp + "'   valueId='" + i + "'>";
		b.addEventListener("click", function (e) {

			var selectesItem = this.getElementsByTagName("input")[0];
			var featureId = inputEl.getAttribute("featureId");
			var valueId = selectesItem.getAttribute("valueId");
			
		
			
			
			var ff = root[valueId];//var valueId=selectesItem.getAttribute("valueId");
			//var ff= selectesItem.value;

			var pr = ff.properties;
			var coordinates = ff.geometry.coordinates;
			//var value = pr.name;
			var value=selectesItem.value;
			
			//
			inputEl.value=value;
			
			
			var feature =inputEl.feature;// vectorSource.getFeatureById(featureId);
			var coord = getPointFromLongLat(coordinates[0], coordinates[1]);

			feature.getGeometry().setCoordinates(coord);
			
			
			RouteControl.updateFeature(featureId);
			var x = document.getElementsByClassName("autocomplete-items");
			for (var i = 0; i < x.length; i++) {
				//if (elmnt != x[i] && elmnt != inp) 
				{
					x[i].parentNode.removeChild(x[i]);
				}
			}

		});
		autocompleteList.appendChild(b);
	}
}

function fff()
{
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
	
}


function autocomplete(inp) {
	var currentFocus;
	inp.addEventListener("input", function (e) {
		var a, b, i, val = this.value;
		/*close any already open lists of autocompleted values*/
		closeAllLists();
		if (!val) {return false;}
		currentFocus = -1;

		autocompleteList = document.createElement("DIV");
		autocompleteList.setAttribute("id", this.id + "autocomplete-list");
		autocompleteList.setAttribute("class", "autocomplete-items");
		/*append the DIV element as a child of the autocomplete container:*/
		this.parentNode.appendChild(autocompleteList);

		fetchMAddresscbb(inp, autocompleteList);
	});
	inp.addEventListener("focus", function (e) {
		var a, b, i, val = this.value;
		/*close any already open lists of autocompleted values*/
		closeAllLists();
		currentFocus = -1;
		autocompleteList = document.createElement("DIV");
		autocompleteList.setAttribute("id", this.id + "autocomplete-list");
		autocompleteList.setAttribute("class", "autocomplete-items");
		/*append the DIV element as a child of the autocomplete container:*/
		this.parentNode.appendChild(autocompleteList);
		var vall = "Current Location";

		b = document.createElement("DIV");

		b.innerHTML += vall;
		b.innerHTML += "<input type='hidden' value='" + vall + "'>";
		b.addEventListener("click", function (e) {
			var featureId = inp.getAttribute("featureId");
			var coordsss = getLastGeoLocationEvent()
			const coordss = coordsss.coords;
			var coordinates = [coordss.longitude, coordss.latitude];
			
			var bntEl = document.getElementById("bnt" + featureId);
			var feature =bntEl.feature;// vectorSource.getFeatureById(featureId);
			
			reverseGeocoding(coordinates[0], coordinates[1], bntEl, callbackSetInputElVal);
			
			var feature =bntEl.feature;// vectorSource.getFeatureById(featureId);
						var coord = getPointFromLongLat(coordinates[0], coordinates[1]);

						feature.getGeometry().setCoordinates(coord);
			
			
			RouteControl.updateFeature(featureId);
			var x = document.getElementsByClassName("autocomplete-items");
			for (var i = 0; i < x.length; i++) {
				//if (elmnt != x[i] && elmnt != inp) 
				{
					x[i].parentNode.removeChild(x[i]);
				}
			}

		});
		autocompleteList.appendChild(b);
	});

	/*execute a function presses a key on the keyboard:*/
	inp.addEventListener("keydown", function (e) {
		var x = document.getElementById(this.id + "autocomplete-list");
		if (x) x = x.getElementsByTagName("div");
		if (e.keyCode == 40) {
			currentFocus++;
			addActive(x);
		} else if (e.keyCode == 38) { //up
			currentFocus--;
			addActive(x);
		} else if (e.keyCode == 13) {
			e.preventDefault();
			if (currentFocus > -1) {
				if (x) x[currentFocus].click();
			}
		}
	});
	function addActive(x) {
		if (!x) return false;
		removeActive(x);
		if (currentFocus >= x.length) currentFocus = 0;
		if (currentFocus < 0) currentFocus = (x.length - 1);
		x[currentFocus].classList.add("autocomplete-active");
	}
	function removeActive(x) {
		for (var i = 0; i < x.length; i++) {
			x[i].classList.remove("autocomplete-active");
		}
	}
	function closeAllLists(elmnt) {
		var x = document.getElementsByClassName("autocomplete-items");
		for (var i = 0; i < x.length; i++) {
			if (elmnt != x[i] && elmnt != inp) {
				x[i].parentNode.removeChild(x[i]);
			}
		}
	}
	document.addEventListener("click", function (e) {
		closeAllLists(e.target);
	});
}