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
	element.setAttribute("id", "speedctr");
  //  element.className = 'routeControl';
  element.className = 'ol-unselectable ol-control';
	
	element.style.backgroundColor = " rgba(0,0,136,0.5)";
	//element.style.backgroundColor ="#4CAF50";
	
	//element.style.bottom="68px";
	//element.style.left=".5em";
	
	element.style.right= "2.5em";
	element.style.top="0.5em"
	
	
	var f='<div class="leaflet-routing-container leaflet-bar leaflet-control"><div class="leaflet-routing-geocoders "><div class="leaflet-routing-geocoder"><input class="" placeholder="Start"><span class="leaflet-routing-remove-waypoint"></span></div><div class="leaflet-routing-geocoder"><input class="" placeholder="End"><span class="leaflet-routing-remove-waypoint"></span></div><button class="routing-add-waypoint " type="button"></button></div><div class="leaflet-routing-alternatives-container"></div></div>';
	var ff='  <div  id="rcorners2" class="leaflet-control-container inline" >      <div class="leaflet-top leaflet-left">         <div class="leaflet-control-zoom leaflet-bar leaflet-control">            <a class="leaflet-control-zoom-in" href="#" title="Zoom in" role="button" aria-label="Zoom in" aria-disabled="false">                     <span aria-hidden="true">+</span>            </a>           <a class="leaflet-control-zoom-out" href="#" title="Zoom out" role="button" aria-label="Zoom out">                   <span aria-hidden="true">−</span>           </a>       </div>      </div>      <div class="leaflet-top leaflet-right">         <div class="leaflet-routing-container leaflet-bar leaflet-control">            <div class="leaflet-routing-geocoders ">               <div class="leaflet-routing-geocoder"><input class="" placeholder="Start"><span class="leaflet-routing-remove-waypoint"></span>               <div class="leaflet-routing-geocoder"><input class="" placeholder="End"><span class="leaflet-routing-remove-waypoint"></span>            </div>                       <button class="leaflet-routing-add-waypoint " type="button"></button>          </div>           <div class="leaflet-routing-alternatives-container">               <div class="leaflet-routing-alt">                  <h2>Автомагистрала Хемус, 4</h2>                  <h3>422.4 km, 6 h 0 min</h3>                  <table class=" ">                     <colgroup class="">                        <col class="leaflet-routing-instruction-icon">                        <col class="leaflet-routing-instruction-text">                        <col class="leaflet-routing-instruction-distance">                     </colgroup>                     <tbody class="">                                   <tr class="">                           <td class=""><span class="leaflet-routing-icon leaflet-routing-icon-turn-right"></span></td>                           <td class="">Turn right onto Варна</td>                           <td class="">100 m</td>                        </tr>                        <tr class="">                           <td class=""><span class="leaflet-routing-icon leaflet-routing-icon-arrive"></span></td>                           <td class="">You have arrived at your destination</td>                           <td class="">0 m</td>                        </tr>                     </tbody>                  </table>               </div><span>d</span>               <div class="leaflet-routing-alt leaflet-routing-alt-minimizedkras">                  <h2>208, Автомагистрала Тракия</h2>                  <h3>488.4 km, 6 h 5 min</h3>                  <table class=" ">                     <colgroup class="">                        <col class="leaflet-routing-instruction-icon">                        <col class="leaflet-routing-instruction-text">                        <col class="leaflet-routing-instruction-distance">                     </colgroup>                     <tbody class="">                        <tr class="">                           <td class=""><span class="leaflet-routing-icon leaflet-routing-icon-depart"></span></td>                           <td class="">Head northwest</td>                           <td class="">1.5 km</td>                        </tr>                                            <tr class="">                           <td class=""><span class="leaflet-routing-icon leaflet-routing-icon-arrive"></span></td>                           <td class="">You have arrived at your destination</td>                           <td class="">0 m</td>                        </tr>                     </tbody>                  </table>               </div>            </div>         </div>      </div>   </div>   </div>';
	element.innerHTML = ff;

    super({
      element: element,
      target: options.target,
    });

  //  button.addEventListener('click', this.handleRotateNorth.bind(this), false);
  }

}



