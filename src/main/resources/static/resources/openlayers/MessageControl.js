class MessageControl extends ol.control.Control {
	
	
	
  constructor(opt_options) {
	const options = opt_options || {};
    const button = document.createElement('button');
	button.setAttribute("id", "b11");
    button.innerHTML ='#';// 'Login';
						
	const element = document.createElement('span');
	element.setAttribute("id", "MessageControlId");
	element.className = 'border w3-border-red  ';
	
				
//	z-index: 999;
	//element.style="background-color: lightblue; position: absolute;border: solid 1px black;top: 90px;left: 560px;min-width: 20em;   min-height:50px;";
	element.style="background-color: lightblue; position: absolute;border: solid 1px black;top: 50px;left: 0px;min-width: 20em;   min-height:50px;";
	
//	element.style.display="none";
	element.style.zIndex = "99";
						
	let template=`
		<div class="ccontainer">
		   <audio id="audio" src="" /*preload="auto"*/  ></audio>
			<msg id="msg"  />
			<div class="card" id="log-in-card1">
			<!-- 
				<header class="card-header">header</header>
			-->				
					        <div id="MessageControlContainer">
							
							
							<div id="videos">
							      <video class="video-player" id="user-1"  muted="muted" autoplay= playsinline width="64"></video>
								  <br/>
							      <video class="video-player" id="user-2" autoplay playsinline width="64"></video>
							  </div>
							  <!---
							<div>
							
							<table><tr>
							  <td><video id="v1" autoplay width=200></video></td>
							  <td><video id="v2" autoplay></video></td></tr>
							  <tr><td id="v1info">0x0</td>
							  		<td id="v2info">0x0</td></tr>
							</table>
							
							</div>
							--->
							<div>
							
							<button id='callButton' onclick="MessageControl.createOffer();">call</button>
							<button id='hangup' onclick="MessageControl.hangup();">hangup</button>
							<button id='answerButton' onclick="MessageControl.createAnswer();">answer</button>
							<!--
							<button onclick="addAnswer();">addAnswer</button>
							<button onclick="MessageControl.init()">Start!</button>
							
							<button onclick="MessageControl.start()">Start!</button>
							-->
							</div>
					           <div class="card-content" style="padding: 0.3rem;">
					              <div class="content">
					                 <div id="userName" class="field">
					                    <div class="control">
					                       <input id="histrory" class="input" type="" placeholder="fdasfds">
										   
										   <textarea class="textarea" id="text_box" rows="4" cols="30"></textarea>
										   
					                    </div>
					                 </div>
					                 <div class="field">
					                    <div class="control">
					                       <input id="message" class="input" type="" placeholder="message">
					                    </div>
					                 </div>
					              </div>
					           </div>
					           <footer class="card-footer" style="padding: 0.3rem;">
					              <div class="card-footer-item" style="padding: 0.2rem;">
					                 <a href="#" onclick="MessageControl.Close();" class="button is-primary" id="log-in-button">Close</a>
					                 &nbsp;
					                 <a href="#" onclick="MessageControl.Send();" class="button is-primary" id="log-in-button">Send</a>
					              </div>
					           </footer>
					        </div>
					     </div>
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
	
			 static hangup(){
				
				MessageControl.audio.pause();
				MessageControl.audio.currentTime = 0;
				document.getElementById("answerButton").style.display = "none";
				
			log("hangup");
				}
		static audio=null;
								
			static createOffer(){
				
				//talog("MsgCtr::createOffer{");
				var el=document.getElementById("msg");
			//	talog("MsgCtr::createOffer el"+el);
			
	
			//var	callButton=removeAllelementlictners(document.getElementById("callButton"));
			
			
			 MessageControl.audio=audioStart('../resources/audio/incoming-call.wav',3,MessageControl.callEnded);
		//	callButton.innerHTML='hangup'
		//	el.addEventListener('click', function () {
		//			  MessageControl.hangup(audio);
		//			}, false);
					
			
				_createOffer(el);
				//talog("MsgCtr::createOffer }");
				
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
						
							/*
							var audio=document.getElementById("audio");	
									
									//audio.src="https://www.sousound.com/music/healing/healing_01.mp3";
									audio.src="../resources/audio/outgoing-call2.ogg";
									
									
									audio.loop=true;
									
									audio.play();
							
							*/
							
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
			
			
					  
					  
					  
	static init(context,fromId,from,toId,to){
	
		
		document.getElementById("answerButton").style.display = "none";
		
		//todo
		document.getElementById('user-1').srcObject = localStream
		document.getElementById('user-2').srcObject = remoteStream
		
		
		var el=document.getElementById("msg");
		
		el.setAttribute('context', context);
		el.setAttribute('fromId', fromId );
		el.setAttribute('from', from);
		el.setAttribute('toId', toId);
		el.setAttribute('to', toId);
		let fmt=`context ${context} ,fromId ${fromId},from ${from},toId ${toId},to ${to} `;
		log(fmt);
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
			var fromId=el.getAttribute('fromId' );
			var toId=el.getAttribute('toId');
			
			var msg=document.getElementById("message").value;
		
			callRPC("SendMessage",context,fromId,toId,msg).then((result) => 
						{	
							log(result);//RouteControl.render(result);
							document.getElementById("message").value=""; 
						});
							
							
							
				}
		
		static OnMessage(message){
			log("OnMessage"+ message );
		var el=document.getElementById("histrory");	
		el.value=message;
		var el=document.getElementById("MessageControlId");
					el.style.display="";
		}
}
