//import {Control, defaults as defaultControls} from './ol/control.js';
class RouteControl extends ol.control.Control {
  /**
   * @param {Object} [opt_options] Control options.
   */
  constructor(opt_options) {
    const options = opt_options || {};

 //   const button = document.createElement('button');
//	button.setAttribute("id", "b1");
 //   button.innerHTML ='<i class="fa fa-sign-in"></i>';
	//button.style.width="3.875em";
    
	//button.style.backgroundColor ="#4CAF50";
	
	
	const button = document.createElement('button');
	   button.innerHTML = 'Ntreterterterttretr';
	
	const element = document.createElement('routeControlId');
	//element.setAttribute("id", "speedctr");
  //  element.className = 'routeControl';
  element.className = 'ol-unselectable ol-control';
	
	//element.style.backgroundColor = " rgba(0,0,136,0.5)";
	//element.style.backgroundColor ="#4CAF50";
	
	//element.style.bottom="68px";
	//element.style.left=".5em";
	
	element.style.right= "0em";
	element.style.top="0em"
	
	
	
	var v=RouteControl.getctr();
	 element.appendChild(v);

    super({
      element: element,
      target: options.target,
    });

  //  button.addEventListener('click', this.handleRotateNorth.bind(this), false);
  }

  
static getctr() {
    var root = document.createElement('DIV');
    root.setAttribute('id', 'rcorners2');
    root.setAttribute('class', 'leaflet-control-container inline');

    var node_2 = document.createElement('DIV');
    node_2.setAttribute('class', 'leaflet-top leaflet-left');
    root.appendChild(node_2);

    var node_3 = document.createElement('DIV');
    node_3.setAttribute('class', 'leaflet-control-zoom leaflet-bar leaflet-control');
    node_2.appendChild(node_3);

    var node_4 = document.createElement('A');
    node_4.setAttribute('class', 'leaflet-control-zoom-in');
    node_4.setAttribute('href', '#');
    node_4.setAttribute('title', 'Zoom in');
    node_4.setAttribute('role', 'button');
    node_3.appendChild(node_4);

    var node_5 = document.createElement('SPAN');
    node_4.appendChild(node_5);

    var node_6 = document.createElement('A');
    node_6.setAttribute('class', 'leaflet-control-zoom-out');
    node_6.setAttribute('href', '#');
    node_6.setAttribute('title', 'Zoom out');
    node_6.setAttribute('role', 'button');
    node_3.appendChild(node_6);

    var node_7 = document.createElement('SPAN');
    node_6.appendChild(node_7);

    var node_8 = document.createElement('DIV');
    node_8.setAttribute('class', 'leaflet-top leaflet-right');
    root.appendChild(node_8);

    var node_9 = document.createElement('DIV');
    node_9.setAttribute('class', 'leaflet-routing-container leaflet-bar leaflet-control');
    node_8.appendChild(node_9);

    var node_10 = document.createElement('DIV');
    node_10.setAttribute('class', 'leaflet-routing-geocoders ');
    node_9.appendChild(node_10);

    var node_11 = document.createElement('DIV');
    node_11.setAttribute('class', 'leaflet-routing-geocoder');
    node_10.appendChild(node_11);


var node_12 =RouteControl.autoCompleteBtn("startID",'Start');
node_11.appendChild(node_12);

    var node_14 = document.createElement('SPAN');
    node_14.setAttribute('class', 'leaflet-routing-remove-waypoint');
    node_11.appendChild(node_14);

    var node_15 = document.createElement('DIV');
    node_15.setAttribute('class', 'leaflet-routing-geocoder');
    node_11.appendChild(node_15);

    var node_16 = document.createElement('DIV');
    node_16.setAttribute('class', 'autocomplete');
    node_16.setAttribute('style', 'width:300px;');
    node_15.appendChild(node_16);
 
var node_17 =RouteControl.autoCompleteBtn("endID",'End');
node_16.appendChild(node_17);


    var node_18 = document.createElement('SPAN');
    node_18.setAttribute('class', 'leaflet-routing-remove-waypoint');
    node_15.appendChild(node_18);

    var node_19 = document.createElement('BUTTON');
    node_19.setAttribute('class', 'leaflet-routing-add-waypoint ');
    node_19.setAttribute('type', 'button');
    node_11.appendChild(node_19);
    
	
	
    return root;
  }
  
  static autoCompleteBtn(btnId,placeholder) {
  

	var root = document.createElement('DIV');
	root.setAttribute('class', 'autocomplete');
	root.setAttribute('style', 'width:300px;');

	var input = document.createElement('INPUT');
	input.setAttribute('id', btnId);
	input.setAttribute('type', 'text');
	input.setAttribute('name', 'myCountry');
	input.setAttribute('placeholder', placeholder);
	root.appendChild(input);
	autocomplete(input, countries);
	return root;
  }
}



