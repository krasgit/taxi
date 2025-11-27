let callTimer;
let callDuration = 0;
const MAX_CALL_DURATION = 10; // in seconds

function startCallTimer() {
        callDuration = 0;
        updateTimerDisplay();
        callTimer = setInterval(() => {
            callDuration++;
            updateTimerDisplay();
            
            // Auto hangup after max duration
            if (callDuration >= MAX_CALL_DURATION) {
                log("Call time limit reached. The call will now end.");
                MessageControl.hangUp();
            }
        }, 1000);
    }

	function updateTimerDisplay() {
	       const minutes = Math.floor(callDuration / 60).toString().padStart(2, '0');
	       const seconds = (callDuration % 60).toString().padStart(2, '0');
	       log(`Call duration: ${minutes}:${seconds}`);
	   }

	   // Stop call timer
	           function stopCallTimer() {
	               clearInterval(callTimer);
	           log("Call duration: 00:00");
	           }

const peerConnectionConfig = {
	    'iceServers': [
	        {'urls': 'stun:stun.l.google.com:19302'}
	    ]
	};

	
	
  let peerConnection;
  let localStream;
  let remoteStream;

  let init = async () => {
      localStream = await navigator.mediaDevices.getUserMedia({video:true, audio:true})
	  //localStream = await navigator.mediaDevices.getUserMedia({video:false, audio:true})
      remoteStream = new MediaStream()
	  //to controll
      //document.getElementById('user-1').srcObject = localStream
      //document.getElementById('user-2').srcObject = remoteStream
	  createPeerConnection();
     // localStream.getTracks().forEach((track) => {
      //    peerConnection.addTrack(track, localStream);
      //});

      peerConnection.ontrack = (event) => {
          event.streams[0].getTracks().forEach((track) => {
          remoteStream.addTrack(track);
          });
      };
  }

  
  init();
    let _createOffer = async (el) => {

  	createPeerConnection();
  	
  	var el=document.getElementById("msg");
  	var fromId=el.getAttribute('fromId' );
  	var toId=el.getAttribute('toId');
  	//talog("fromId:"+fromId +"toId:"+toId);
          peerConnection.onicecandidate = async (event) => {
              //Event that fires off when a new offer ICE candidate is created
              if(event.candidate){
                  const _offer= JSON.stringify(peerConnection.localDescription)
  				/*
  				var encodedOffer = btoa(_offer);
  				callRPC("createOffer",fromId,toId,encodedOffer).then((result) => {	
  				log("createOffer res" +result);
  				document.getElementById("message").value=""; 
  				 });
                     */									
  				
              }
          };

          const offer = await peerConnection.createOffer();
  		
  		const _offer= JSON.stringify(offer)
  		var encodedOffer = btoa(_offer);
  						
  						callRPC("createOffer",fromId,toId,encodedOffer).then((result) => 
  											{	
  												log("createOffer res" +result);
  												document.getElementById("message").value=""; 
  											});
  		
  		
  		startCallTimer();
  		
  		
          await peerConnection.setLocalDescription(offer);
      }

      
  	let _createAnswer = async (el) => {
  		createPeerConnection();
  //	    let offer = JSON.parse(document.getElementById('offer-sdp').value)
       	
          var toId=el.getAttribute('callFrom' );
          var fromId="";//el.getAttribute('toId');

  		var offerSDP=el.getAttribute('data-offerSDP' );
  		
  		var offerSDPDecode = atob(offerSDP);		
  		let offer = JSON.parse(offerSDPDecode);
  		
  		
   
  			    peerConnection.onicecandidate = async (event) => {
  	        //Event that fires off when a new answer ICE candidate is created
  	        if(event.candidate){
  	           // console.log('Adding answer candidate...:', event.candidate)
  	           // document.getElementById('answer-sdp').value = JSON.stringify(peerConnection.localDescription)
  			   const _offer= JSON.stringify(peerConnection.localDescription)
  			   			
  			   var encodedOffer = btoa(_offer);
  			   
  			   			callRPC("createAnswer",fromId,toId,encodedOffer).then((result) => 
  			   								{	
  			   									log(result);//RouteControl.render(result);
  			   								//	document.getElementById("message").value=""; 
  			   								});
  	        }
  	    };

  	    await peerConnection.setRemoteDescription(offer);

  	    let answer = await peerConnection.createAnswer();
  	    await peerConnection.setLocalDescription(answer); 
  	}

  	let _addAnswer = async (el) => {
  	    console.log('Add answer triggerd')
  		createPeerConnection();
  	    //let answer = JSON.parse(document.getElementById('answer-sdp').value)
  		
  		var dav=el.getAttribute('data-answer-sdp' );
  		var answerSDPDecode = atob(dav);	
  		
  		let answer = JSON.parse(answerSDPDecode);
  		
  		
  		
  	    console.log('answer:', answer)
  	    if (!peerConnection.currentRemoteDescription){
  	        peerConnection.setRemoteDescription(answer);
  	    }
  	}
    
    

  	
  	function createPeerConnection() {
  		
  		if(peerConnection)
             return;		
  		
  	            peerConnection = new RTCPeerConnection({
  	                iceServers: [
  	                    { urls: 'stun:stun.l.google.com:19302' }
  	                ]
  	            });
  	            
  	            // Add mixed audio stream if music is playing
  	            
  	                // Add just the local audio/video
  	                localStream.getTracks().forEach(track => {
  	                    peerConnection.addTrack(track, localStream);
  	                });
  	            
  	            
  	            // Handle remote stream
  	            peerConnection.ontrack = event => {
  					
  					//todo
  					var remoteVideo=document.getElementById("user-2");	
  					
  	                remoteVideo.srcObject = event.streams[0];
  	                console.log('Received remote stream');
  	            };
  	            
  	            // Handle ICE candidates
  	            peerConnection.onicecandidate = event => {
  	                if (event.candidate) {
  					//TODO	
  	                  //  signaling.send({type: 'candidate', candidate: event.candidate    });
  	                }
  	            };
  	            
  	            // Handle connection state changes
  	            peerConnection.onconnectionstatechange = () => {
  	                console.log('Connection state:', peerConnection.connectionState);
  	                if (peerConnection.connectionState === 'disconnected' || 
  	                    peerConnection.connectionState === 'failed') {
  	                    //TODO
  							MessageControl.hangUp();
  	                }
  	            };
  	        }  
    
    let hangUp =  (el) => 
    {
  	var fromId=el.getAttribute('fromId' );
  	
  	if (peerConnection) {
  	   peerConnection.close();
  	   peerConnection = null;
  	  }
  	  
  	  stopCallTimer();
  	  var toId=el.getAttribute('callFrom' );
  	  callRPC("hangUp",fromId).then((result) => 
  		{	
  		log(result);//RouteControl.render(result);
  	  	});
  	  
  	}
