class MessageControl extends ol.control.Control {
					  /**
					   * @param {Object} [opt_options] Control options.
					   */
					  constructor(opt_options) {
					    const options = opt_options || {};

					    const button = document.createElement('button');
						button.setAttribute("id", "b11");
					    button.innerHTML ='#';// 'Login';
						
						
						const element = document.createElement('span');
						element.setAttribute("id", "d1");
					    element.className = 'border w3-border-red  ';
						
						element.style="background-color: lightblue; position: absolute;border: solid 1px black;top: 90px;left: 560px;min-width: 20em;   min-height:50px;";
						
						let template=`
					<div class="ccontainer">
					     <div class="card" id="log-in-card1">
					        <header class="card-header">
					           header1
					        </header>
					        <div id="LoginControlContainer1">
					           <div class="card-content" style="padding: 0.3rem;">
					              <div class="content">
					                 <div id="userName" class="field">
					                  
					                    <div class="control">
					                       <input id="histrory" class="input" type="" placeholder="fdasfds">
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
		
											
					}
