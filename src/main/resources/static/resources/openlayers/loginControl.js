//import {Control, defaults as defaultControls} from './ol/control.js';
class LoginControl extends ol.control.Control {
  /**
   * @param {Object} [opt_options] Control options.
   */
  constructor(opt_options) {
    const options = opt_options || {};

    const button = document.createElement('button');
	button.setAttribute("id", "b1");
    button.innerHTML ='<i class="fa fa-sign-in"></i>';// 'Login';
	////button.innerHTML ='<i class="fa fa-sign-out"></i>';// 'Loginout';
	//button.style.width="3.875em";
    
	//button.style.backgroundColor ="#4CAF50";
	
	
	const element = document.createElement('span');
	element.setAttribute("id", "d1");
    element.className = 'rotate-north ol-unselectable ol-control';
	
	//element.style.backgroundColor = " rgba(0,0,136,0.5)";
	//element.style.backgroundColor ="#4CAF50";
	
	
	//element.style.top = "95px"; 
    element.appendChild(button);

    super({
      element: element,
      target: options.target,
    });

    button.addEventListener('click', this.handleRotateNorth.bind(this), false);
  }

  
  setState(state) {

  }
  
  handleRotateNorth(e) {
	
	//const button = document.getElementById("b1");
	//button.innerHTML = 'A';
	const button=document.getElementById("b1");
	var innerHTML=button.innerHTML;
	
	if(innerHTML==='<i class="fa fa-sign-out"></i>')
		{
		//button.setAttribute("id", "b1");
		//TODO MAKE CALL
	    button.innerHTML = '<i class="fa fa-sign-in"></i>';
	 return;
		}
		
	document.getElementById('id01').style.display='block'
	
    //this.getMap().getView().setRotation(0);
  }
}

// export default RotateNorthControl;

