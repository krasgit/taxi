
var br = 2;

function formatAddresss(json, autocompleteList) {
	//alert("formatAddresss");
	var root = json.features;
	const fruits = [];
	for (let i = 0; i < root.length; i++) {
		var value = root[i];
		var pr = value.properties;
		//  if(pr.state==='Varna')
		fruits.push(pr.name + pr.state + " [" + pr.osm_id + "] " + value.geometry.coordinates);
	}
	return fruits;
}

function fetchMAddresscbb(inputEl, autocompleteList) {
	var q = inputEl.value;
	var url = 'https://' + hostname + ':8443/api?q=Varna ' + q;
	log("fetchMAddresscbb url" + url)
	fetch(url)
		.then(function (response) {
			return response.json();
		})
		.then(function (json) {
			log(json);
			newinitItem(autocompleteList, json, inputEl);
		})
}

function newinitItem(autocompleteList, json, inputEl) {

	var root = json.features;

	for (i = 0; i < root.length; i++) {
		var value = root[i];
		var pr = value.properties;

		var disp = pr.name + pr.state;

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
			var value = pr.name;
			RouteControl.updateFeature(featureId, coordinates, value);
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
			RouteControl.updateFeature(featureId, coordinates, "");
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