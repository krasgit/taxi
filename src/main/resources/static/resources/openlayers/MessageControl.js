class MessageControl extends ol.control.Control {
	
	
	
  constructor(opt_options) {
	const options = opt_options || {};
    const button = document.createElement('button');
	button.setAttribute("id", "b11");
    button.innerHTML ='#';// 'Login';
						
	const element = document.createElement('div');
	element.setAttribute("id", "MessageControlId");
	element.className = 'border w3-border-red  ';
	
//	element.style.display="none";
	element.style.zIndex = "99";
					
	
	let template=`<div class="ol-attribution ol-unselectable ol-control">
	
				<audio id="audio" src="" /*preload="auto"*/  ></audio>
				<msg id="msg"  />
				
				
				
				<div class="acard" id="log-in-card1">
				 <span>
				 <div style='text-align: left; border='1'  id="msgDl">
				 
				 
				 
				 </div>
				 
				 
				
					<div class="input-group mb-1 mt-1" id="messageTo" >
		
									
									
									</div>
					<div class="input-group mb-1 mt-1" >
					
					<nobr>
					             <input id="message" class="input" type="" placeholder="message">
								 <button type="button" title="repa" onclick="MessageControl.Send();" >
					</nobr>
					</div>
					
					
					<span style="width: 2em;	height: 2em;	  vertical-align: -0.125em;">
							   <svg xmlns="http://www.w3.org/2000/svg" class="icon" aria-hidden="true" focusable="false" viewBox="0 0 512 512">
					<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><!--!Font Awesome Free 6.7.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M498.1 5.6c10.1 7 15.4 19.1 13.5 31.2l-64 416c-1.5 9.7-7.4 18.2-16 23s-18.9 5.4-28 1.6L284 427.7l-68.5 74.1c-8.9 9.7-22.9 12.9-35.2 8.1S160 493.2 160 480l0-83.6c0-4 1.5-7.8 4.2-10.8L331.8 202.8c5.8-6.3 5.6-16-.4-22s-15.7-6.4-22-.7L106 360.8 17.7 316.6C7.1 311.3 .3 300.7 0 288.9s5.9-22.8 16.1-28.7l448-256c10.7-6.1 23.9-5.5 34 1.4z"/>
					</svg>			   
					
								   
								   </span></button>
					
					</span>
				   <button type="button" title="Messages">
				   <span style="width: 2em;	height: 2em;	  vertical-align: -0.125em;">
				   <svg xmlns="http://www.w3.org/2000/svg" class="icon" aria-hidden="true" focusable="false" viewBox="0 0 512 512">
				     <path
				       fill="currentColor"
				       d="M256 8C119 8 8 119 8 256s111 248 248 248 248-111 248-248S393 8 256 8zm0 448c-110.5 0-200-89.5-200-200S145.5 56 256 56s200 89.5 200 200-89.5 200-200 200zm61.8-104.4l-84.9-61.7c-3.1-2.3-4.9-5.9-4.9-9.7V116c0-6.6 5.4-12 12-12h32c6.6 0 12 5.4 12 12v141.7l66.8 48.6c5.4 3.9 6.5 11.4 2.6 16.8L334.6 349c-3.9 5.3-11.4 6.5-16.8 2.6z"
				     />
				   </svg>
				   
				   </span></button>
				 </div>`;		
					
					    element.innerHTML=template;

					    super({
					      element: element,
					      target: options.target,
					    });

					    button.addEventListener('click', this.handleRotateNorth.bind(this), false);
					  }

					  
					  handleRotateNorth(e) {
						
					  }
					  static audio=null;
					  
					  
					  static OnMessage(message){
					  			log("OnMessage"+ message );
					  			
					  			const msg = JSON.parse(message);
					  			
								//formId 		form		context			msg
								
								
					  			const parentElement = document.querySelector("#msgDl");
					  			var parentElement1 =parentElement.querySelector("#m"+msg.formId);
								
								if(!parentElement1)
									{

										 parentElement1 = document.createElement("div");
										 parentElement1.setAttribute('id', "m" +msg.formId );
										 parentElement1.setAttribute('style', 'text-align: left; 				 border:1px solid black;' );
										 
										 var a = document.createElement("a");
										 a.setAttribute('onclick', "MessageControl.setMsg("+msg.context+","+msg.formId+");" );
										 a.setAttribute('class', "button is-primary" );
										 const textnode = document.createTextNode(msg.form);
										 a.appendChild(textnode);
										
										 parentElement1.appendChild(a);
										 parentElement.appendChild(parentElement1);
										 
										
										 
									}
										const node = document.createElement("div");
										const textnode = document.createTextNode(msg.msg);
										node.appendChild(textnode);
									parentElement1.appendChild(node);
								
									
									
					  		//var el=document.getElementById("histrory");	
					  		//el.value=message;
					  		//var el=document.getElementById("MessageControlId");
					  		//			el.style.display="";
					  		}
					  		
					  
	static	setMsg(context,toId)
	{
		var el=document.getElementById("msg");
					
		el.setAttribute('context', context);
		
			
			el.setAttribute('toId', toId);
	}					
					  
	static init(context,fromId,from,toId,to){
	
		/*
		document.getElementById("answerButton").style.display = "none";
		
		//todo
		document.getElementById('user-1').srcObject = localStream
		document.getElementById('user-2').srcObject = remoteStream
		*/
		
		var el=document.getElementById("msg");
		
		el.setAttribute('context', context);
		el.setAttribute('fromId', fromId );
		el.setAttribute('from', from);
		el.setAttribute('toId', toId);
		el.setAttribute('to', to);
		
		
		document.getElementById("messageTo").innerHTML=to;
		//let fmt=`context ${context} ,fromId ${fromId},from ${from},toId ${toId},to ${to} `;
		//log(fmt);
		var el=document.getElementById("MessageControlId");
			el.style.display="";
    }
	
	static Close(){
		var el=document.getElementById("MessageControlId");
					el.style.display="none";
		} 
	static Send(){
		
		
		
		var audio=document.getElementById("audio");	
		
		//audio.src="https://www.sousound.com/music/healing/healing_01.mp3";
		audio.src="../resources/audio/hihat.mp3";
		
		audio.play();
			var el=document.getElementById("msg");
			
			var context=el.getAttribute('context');
			
			var fromId =Cookie.getCookie("personId") ;
			var user =Cookie.getCookie("user") ;
			var toId=el.getAttribute('toId');
			var to=el.getAttribute('to');
			var msg=document.getElementById("message").value;
		
			//--------------------
							const parentElement = document.querySelector("#msgDl");
							var parentElement1 =parentElement.querySelector("#m"+fromId);	
															
							if(!parentElement1)	{
							 parentElement1 = document.createElement("div");
							 parentElement1.setAttribute('id', "m" +fromId );
							 parentElement1.setAttribute('style', 'text-align: left; border:1px solid black;' );
							 var a = document.createElement("a");
							 a.setAttribute('onclick', "MessageControl.setMsg("+context+","+fromId+");" );
							 a.setAttribute('class', "button is-primary" );
			     			 const textnode = document.createTextNode(user+' '+to);
				    		 a.appendChild(textnode);
					 											
							 parentElement1.appendChild(a);
					    	 parentElement.appendChild(parentElement1);
							}
							const node = document.createElement("div");
							const textnode = document.createTextNode(msg);
						    node.appendChild(textnode);
							parentElement1.appendChild(node);
																						
							//--------------------
			
			
			
			callRPC("SendMessage",context,fromId,toId,msg).then((result) => 
						{	
							log(result);//RouteControl.render(result);
							
							document.getElementById("message").value=""; 
						});
							
                			
				}
		
		
		
		//phone			  
					 static hangUp(){
							
					    document.getElementById("callButton").style.display = "";	
						document.getElementById("answerButton").style.display = "none";
						document.getElementById("hangup").style.display = "none";
						
						var el=document.getElementById("msg");
						
						hangUp(el);
						console.log('Ending call');
					}
				
					static createOffer(){
						
					
						var el=document.getElementById("msg");
					 MessageControl.audio=audioStart('../resources/audio/incoming-call.wav',3,MessageControl.callEnded);
						_createOffer(el);
					}  
					
					
					static callEnded()
					{
							document.getElementById("answerButton").style.display = "none";
					}
					
					static audio = null;

					
					static Call(from, offerSDP )
								{
									
									talog("MsgCtr::Call call from "+from);	
									
									var el=document.getElementById("msg");
									el.setAttribute('data-offerSDP',offerSDP);
									el.setAttribute('callFrom',from);
									
									
									document.getElementById("answerButton").style.display = ""; 
									MessageControl.audio=audioStart('../resources/audio/incoming-call.wav',3,MessageControl.callEnded);
										
								}
					
								
					static createAnswer()
								{
							//		talog("MsgCtr::createAnswer");
									var el=document.getElementById("msg");
									
									_createAnswer(el);
								}		
					
					static addAnswer(from, offerSDP)
						{
					var el=document.getElementById("msg");
					
							el.setAttribute('data-answer-sdp', offerSDP); 
					_addAnswer(el);
					}				
					
					
		//phone end			
}
