class MessageControl extends ol.control.Control {
	
	static audio=null;
	static template=`
	<style>
		.kcontainer{ display: flex; flex-direction: column; width: 280px; max-width: 100%; background: ksalmon; padding: 5px; 
			max-height: 120px;
			--height: 120px;
			overflow-y: scroll;
			// flex-wrap: wrap;
		}
		
		.message { margin: 0 5px 2px; padding: 1px 2px; position: relative; font-size: 12px;   width: auto;max-width: 80%;
			--min-width:1vw;
		}
		
		.message.to { background-color: #673ab7; color: #fff; margin-left: auto; text-align: right; white-space: pre-wrap; border-radius: 10px 10px 0px 10px; }
		.message.from { background-color: #e5e4e9; color: #363636; margin-right: auto; border-radius: 10px 10px 10px 0px; }
	</style>
	
	<div class="ol-attribution ol-unselectable ol-control">
		<audio id="audio" src="" /*preload="auto"*/  ></audio>
		<msg id="msg"  />
					 			    
		<a onclick="MessageControl.upDateState();" class="button is-primary" id=""><el id="elopenclose"><<</el></a> 
									
		<div id="exp" style="display: none;">
			<div class="kcontainer"  id="msgDl" >
			</div>
					 			 
			<div class="input-group mb-1 mt-1" ><div class="input-group mb-1 mt-1" id="messageTo" ></div></div>
					 				 
			<div  class='input-group mb-1 mt-1'>		
 				<input id="message" class="form-control" type="" placeholder="message" style='width: 77%';>
				<a onclick="MessageControl.Send();" class="button is-primary" id="">Send</a>
			</div>
					 			 
		</div>
	</div>`;	
	
	constructor(opt_options) {
		const options = opt_options || {};
    	const button = document.createElement('button');
		button.setAttribute("id", "b11");
    	button.innerHTML ='#';// 'Login';
						
		const element = document.createElement('div');
		element.setAttribute("id", "MessageControlId");
		element.className = 'border w3-border-red  ';
		element.style.zIndex = "99";
		element.innerHTML=MessageControl.template;
    
		super({ element: element, target: options.target,  });
	    
		button.addEventListener('click', this.handleRotateNorth.bind(this), false);
	}
					  
	handleRotateNorth(e) {
	}
					  
	static upDateState(newState){
		var x = document.getElementById("exp");
		var elopenclose = document.getElementById("elopenclose");
		
		if(newState==true){
			x.style.display = "block";
			elopenclose.innerHTML="x";
			return;
			}
			
		if(newState==false){
			x.style.display = "none";
			elopenclose.innerHTML=">>"
			return;
		}	
		
		if (x.style.display === "none") {
		   	x.style.display = "block";
			elopenclose.innerHTML="x";
		  } else {
		    x.style.display = "none";
			elopenclose.innerHTML=">>"
		 }
	}			  

	static playAudio(){

		var audio=document.getElementById("audio");	
		audio.src="../resources/audio/hihat.mp3";
		audio.play();

		}				
		
		
	static init(){
	}	
	
	static Send(){

		var context=el.getAttribute('context');
		var fromId =Cookie.getCookie("personId") ;
		var toId=el.getAttribute('toId');
		
		var msg=document.getElementById("message").value;													
									
		callRPC("SendMessage",context,fromId,toId,msg).then((result) => 
			{	
				log(result);//RouteControl.render(result);
				document.getElementById("message").value=""; 
			});
					
		
	}
	  
	static _Send(){
		var audio=document.getElementById("audio");	
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
			parentElement1.setAttribute=('class', 'message from');
			
			var a = document.createElement("a");
			a.setAttribute('onclick', "MessageControl.setMsg("+context+","+fromId+",'"+to+"');" );
			
			
			a.setAttribute('class', "button is-primary" );
			const textnode = document.createTextNode(to);
			a.appendChild(textnode);
			parentElement1.appendChild(a);
			parentElement.appendChild(parentElement1);
			}
		const node = document.createElement("div");
		node.setAttribute('class', "message to" );
													
		const textnode = document.createTextNode(msg);
		node.appendChild(textnode);
		parentElement1.appendChild(node);
															
									
		callRPC("SendMessage",context,fromId,toId,msg).then((result) => 
			{	
				log(result);//RouteControl.render(result);
				document.getElementById("message").value=""; 
			});
			MessageControl.scrollToBottom();
	}
						
	static OnMessage(message){
		log("OnMessage"+ message );
		MessageControl.upDateState(true);  			
		const msg = JSON.parse(message);
					  			
								//formId 		form		context			msg
		const parentElement = document.querySelector("#msgDl");
		var parentElement1 =parentElement.querySelector("#m"+msg.fromId);
		                                                         
		if(!parentElement1)
			{
			parentElement1 = document.createElement("div");
			parentElement1.setAttribute('id', "m" +msg.formId );
			//parentElement1.setAttribute('style', 'text-align: left; 	border:1px solid black;' );
			parentElement1.setAttribute=('class', 'message');
								 
			var a = document.createElement("a");
            //a.setAttribute('onclick', "MessageControl.setMsg("+msg.context+","+msg.fromId+");" );
			a.setAttribute('onclick', "MessageControl.setMsg("+msg.context+","+msg.fromId+",'"+msg.from+"');" );
			a.setAttribute('class', "button is-primary" );
			const textnode = document.createTextNode(msg.from);
			a.appendChild(textnode);
			parentElement1.appendChild(a);
			parentElement.appendChild(parentElement1);
			}
									
		const node = document.createElement("div");
		node.setAttribute('class', "message from" );
		const textnode = document.createTextNode(msg.msg);
									
		//a.setAttribute('class', "message from" );
		node.appendChild(textnode);
		parentElement1.appendChild(node);
									
	//var el=document.getElementById("histrory");	
	//el.value=message;
	//var el=document.getElementById("MessageControlId");
	//			el.style.display="";
			
	MessageControl.scrollToBottom();
	}
	
	static scrollToBottom(){
		var scrollableDiv = document.getElementById('msgDl');
		var bottomElement = scrollableDiv.lastElementChild;
		bottomElement.scrollIntoView({ behavior: 'smooth', block: 'end' });
	} 
					  
	static	setMsg(context,toId,to)
	{
		var el=document.getElementById("msg");
					
		el.setAttribute('context', context);
		el.setAttribute('toId', toId);
		el.setAttribute('to', to);
		document.getElementById("messageTo").innerHTML="Message to "+to;
	}					
					  
	static init(context,toId,to){
		/*
		document.getElementById("answerButton").style.display = "none";
		
		//todo
		document.getElementById('user-1').srcObject = localStream
		document.getElementById('user-2').srcObject = remoteStream
		*/
		var el=document.getElementById("msg");
		
		el.setAttribute('context', context);
		//el.setAttribute('fromId', fromId );
		//el.setAttribute('from', from);
		el.setAttribute('toId', toId);
		el.setAttribute('to', to);
		
		
		document.getElementById("messageTo").innerHTML="Message to "+to;
		
		
		MessageControl.upDateState(true);
		
		let fmt=`context ${context} ,toId ${toId},to ${to} `;
		log(fmt);
		
    }
	
	static Close(){
		var el=document.getElementById("MessageControlId");
		el.style.display="none";
	} 
	
		//phone	
	static audio = null;		  
	
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

	static callEnded(){
		document.getElementById("answerButton").style.display = "none";
	}
						
	static Call(from, offerSDP ){
		talog("MsgCtr::Call call from "+from);	
		var el=document.getElementById("msg");
		el.setAttribute('data-offerSDP',offerSDP);
		el.setAttribute('callFrom',from);
	
		document.getElementById("answerButton").style.display = ""; 
		MessageControl.audio=audioStart('../resources/audio/incoming-call.wav',3,MessageControl.callEnded);
	}
					
								
	static createAnswer(){
	var el=document.getElementById("msg");
	_createAnswer(el);
	}		
					
	static addAnswer(from, offerSDP){
	var el=document.getElementById("msg");
	el.setAttribute('data-answer-sdp', offerSDP); 
	_addAnswer(el);
	}				
					
		//phone end			
}
