//import {Control, defaults as defaultControls} from './ol/control.js';
class LoginControl extends ol.control.Control {
  /**
   * @param {Object} [opt_options] Control options.
   */
  
  //container;
  
  constructor(opt_options) {
	
	
	
    const options = opt_options || {};

		
			var container = LoginControl.createContainer(options);
			super({element: container, target: options.target, });
			
			this._count=0;
				
				Object.defineProperty(this, "test", {
				  value: 42,
				  writable: false,
				  enumerable:false,
				});
  }

  set count(value)
  {
	this._count=value;
  }
  get count(){
	return this._count;
  }
  static exp(){
	var dd=RouteControl.get();
	const cWaypoint = document.getElementById('userName');
	cWaypoint.appendChild(dd);
  }
  
  static getLoginControlContent()
    {
  	let loginContent = '<div class="ccontainer"> \n \
  	    <div class="card" id="log-in-card">\n \
  	      <header class="card-header"><h1 class="card-header-title" style="padding: 0.1rem;">Log In</h1><h1 class="card-header-title" style="padding: 0.1rem;">\n \
		   <a href="#Foo" onclick="LoginControl.exp()" >route</a></h1>\n \
		 </header>\n \
  	      <div class="card-content" style="padding: 0.3rem;">\n \
  	        <div class="content">\n \
  	          <div id="userName" class="field">\n \
  	            <label class="label">Username</label>\n \
  	            <div class="control">\n \
  	              <input id="username" class="input" type="email" placeholder="user@example.com">\n \
  	            </div>\n \
  	          </div>        \n \
  	          <div class="field">\n \
  	            <label class="label">Password</label>\n \
  	            <div class="control">\n \
  	              <input id="password" class="input" type="password">\n \
  	            </div>\n \
  	          </div>\n \
  	          <p style="text-align: right;"><a href="#" id="open-userinfo">Forgot your password?</a></p>\n \
  	        </div>\n \
  	      </div>\n \
  	      <footer class="card-footer" style="padding: 0.3rem;">\n \
  	        <div class="card-footer-item" style="padding: 0.2rem;">\n \
  				<a href="#" onclick="LoginControl.logIn();" class="button is-primary" id="log-in-button">Log In</a>\n \
  				&nbsp;\n \
  				<a href="#" onclick="LoginControl.PrincipalRegistration();" class="button is-primary" id="log-in-button">Registration</a>\n \
  			</div>\n \
  	      </footer>\n \
  	    </div>\n \
  	  </div>';
   return loginContent;
    }
  
	
	static logIn() {
		

		var username=document.getElementById('username').value;
		var password=document.getElementById('password').value;

		callRPC("login",username,username).then((resultJson) => {
	
			if(resultJson==null){

				   }
				   else 
				   {
					
					
					let result=JSON.parse(resultJson);
					
					Cookie.setCookie("user",username);
					Cookie.setCookie("token",result.sessionId);
					Cookie.setCookie("isTaxi",result.isTaxi);
					LoginControl.visible(false);
					TaxiControl.visible(true);
					RouteControl.logIn();
				   }
				   });
				   
				
		
		
	
	//log("log In ");
	}

	static PrincipalRegistration() {
		
		var username=document.getElementById('username').value;
		var password=document.getElementById('password').value;
		
		wsPrincipalRegistration(username,password);
		
	Cookie.setCookie("user",username)
	//log("PrincipalRegistration ");
	}

	
	static visible(mode)
	{
		var container=document.getElementById('LoginControlContainer');
		if(mode){
			
			container.setAttribute('style', ' background-color: lightblue; min-width: 350;position: absolute; right : 0em; top : 0em');
			//container.setAttribute('style', ' background-color: lightblue; min-width: 350;position: absolute; right : 0em; bottom : 0em ');
			}
		else 
			container.setAttribute('style', 'style="display: none" ');
	}
	
  static createContainer(options) {
		var node_1 = document.createElement('DIV');
 			node_1.setAttribute('name', 'LoginControlContainer');
  			node_1.setAttribute('id', 'LoginControlContainer');
  			node_1.setAttribute('class', '     border w3-border-red  ');
  			node_1.setAttribute('style', ' background-color: lightblue; min-width: 350;position: absolute; right : 0em; bottom : 0em ');

  		
		var node_10 = document.createElement('DIV');
  			node_10.setAttribute('name', 'Waypoint');
  			node_10.setAttribute('id', 'Waypoint');
  			node_1.appendChild(node_10);
  			
  			LoginControl.getLoginControlContent();
  				node_10.innerHTML=LoginControl.getLoginControlContent();
  		return node_1;
  		}
 
}