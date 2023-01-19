package it.cnr.isti.labsedc.concern.rest.monitoring;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import it.cnr.isti.labsedc.concern.ConcernApp;
import it.cnr.isti.labsedc.concern.consumer.Consumer;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("monitoring/biecointerface/loadrules")

public class LoadRules {
		
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	
    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getIt() {
        return homePage();
        
    }
    
    
    private String homePage() {
	
    	return"<!DOCTYPE html>\n"
    			+ "<html>\n"
    			+ "<head lang=\"en\">\n"
    			+ "    <meta charset=\"UTF-8\"/>\n"
    			+ "    <title>Load Rules or ruleset to monitor</title>\n"
    			+ "    <style type=\"text/css\">\n"
    			+ "    div {\n"
    			+ "        display: flex;\n"
    			+ "        flex-direction: column;\n"
    			+ "        align-items: center;\n"
    			+ "    }\n"
    			+ "    input {\n"
    			+ "        margin-top: 10px;\n"
    			+ "    }\n"
    			+ "     \n"
    			+ "    textarea {\n"
    			+ "        margin-top: 15px;\n"
    			+ "        width: 70%;\n"
    			+ "    }\n"
    			+ "</style>\n"
    			+ "     <script type = \"text/javascript\">\n"
    			+"function executePost() {\n"
    			+ "    var xhttp = new XMLHttpRequest();\n"
    			+ "    xhttp.onreadystatechange = function() {\n"
    			+ "         if (this.readystate == XMLHttpRequest.DONE && this.status == 200) {\n"
    			+ "        alert(xhttp.statusText);"
    			+ "         }\n"
    			+ "    };\n"
    			+ "    var textarea = document.getElementById('ruletextarea').value;\n"
    			+ "    xhttp.open(\"POST\", \"http://127.0.0.1:8181/monitoring/biecointerface/loadrules\", true);\n"
    			+ "    xhttp.setRequestHeader(\"Content-type\", \"application/json\");\n"
    			+ "    xhttp.send(JSON.stringify({"
    			+ "    \"jobID\": \"1234\",\n"
    			+ "    \"timestamp\": \"2023-01-18 08:29:30\",\n"
    			+ "    \"messageType\": \"loadRules\",\n"
    			+ "    \"sourceID\": \"4\",\n"
    			+ "    \"event\": textarea,\n" 
    			+ "    \"crc\": 1234565\n"
    			+ "    }));\n"
    			+ "}"    			
    			+ "    </script>\n"
    			+ " \n"
    			+ "<body>    <center>\n"
    			+ "        <h1 style=\"color: green;\">\n"
    			+ "          Concern Rules Loading\n"
    			+ "        </h1>\n"
    			+ "        <div>\n"
    			+ "            <input type=\"file\">\n"
    			+ "            <textarea cols=\"30\" rows=\"20\"\n"
    			+ "                      placeholder=\"rules loaded will appear here\" id=\"ruletextarea\">\n"
    			+ "            </textarea>\n"
    			+ "            <br />\n"
    			+ "            <button onclick=\"executePost()\">Sent loaded rules to the Monitor</button>\n"
    			+ "        </div>\n"
    			+ "    </center>\n"
    			+ "        <script type=\"text/javascript\">\n"
    			+ " let input = document.querySelector('input')\n"
    			+ "let textarea = document.querySelector('textarea')\n"
    			+ "input.addEventListener('change', () => {\n"
    			+ "	let files = input.files;\n"
    			+ "\n"
    			+ "	if (files.length == 0) return;\n"
    			+ "	const file = files[0];\n"
    			+ "	let reader = new FileReader();\n"
    			+ "	reader.onload = (e) => {\n"
    			+ "		const file = e.target.result;\n"
    			+ "		const lines = file.split(/\\r\\n|\\n/);\n"
    			+ "		textarea.value = lines.join('\\n');\n"
    			+ "	};\n"
    			+ "	reader.onerror = (e) => alert(e.target.error.name);\n"
    			+ "	reader.readAsText(file);\n"
    			+ "});\n"
    			+ "    </script>\n"
    			+ "    \n"
    			+ "</body>\n"
    			+ "</html>\n";
    }
    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
	public Response biecointerface(
			String message,
			@Context HttpHeaders headers) {

    	JSONObject bodyMessage = new JSONObject(message);
    	if (((String)bodyMessage.get("messageType")).compareTo("loadRules") == 0 ) {
			if (this.MonitoringStartIfNotStarted()) {
	    		if (loadRule(
	    				((JSONObject) bodyMessage)
	    					.get("event").toString()
    					))
	 			return Response.status(200).entity("Rule/s sent correctly").build();	
			}	    	
    	}
	return Response.status(401).entity("error").build();
	}
    
    private boolean loadRule(String rule) {
        	Consumer internalConsumer = new Consumer();
        	return internalConsumer.run(rule);
        }    
       
    private boolean MonitoringStartIfNotStarted() {	
		try {
			if (ConcernApp.isRunning()) {
				return true;
			}
			else {
				ConcernApp.getInstance();

				while(!ConcernApp.isRunning()) {
					Thread.sleep(500);
				}
			}
		} catch(InterruptedException asd) {
			
		}
		return true;
	}
}

