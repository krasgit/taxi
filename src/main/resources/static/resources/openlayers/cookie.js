class Cookie {
	
	static prefix="";
	
		constructor( ) {	}
		
		static updateRouteInfo(json){
			
		}
		
		static setPrefix(prefix) {
			Cookie.prefix=prefix;
		}
		static setCookie(cname, cvalue, exdays) {
			
			
			cname=Cookie.prefix+cname;
			
		  const d = new Date();
		  d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
		  let expires = "expires="+d.toUTCString();
		  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
		}

		static getCookie(cname) {
			cname=Cookie.prefix+cname;
		  let name = cname + "=";
		  let ca = document.cookie.split(';');
		  for(let i = 0; i < ca.length; i++) {
		    let c = ca[i];
		    while (c.charAt(0) == ' ') {
		      c = c.substring(1);
		    }
		    if (c.indexOf(name) == 0) {
		      return c.substring(name.length, c.length);
		    }
		  }
		  return "";
		}

		
	}
