import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import org.omg.CORBA_2_3.portable.OutputStream;

/**
 * @author couedrao on 27/11/2019.
 * @project gctrl
 */
class SDNCtrlAPI {

    String redirect_traffic(String olddestip, String newdestip) {
        String status = "NOK";
        try{
	        Main.logger(this.getClass().getSimpleName(), "olddestip = " + olddestip + "; newdestip = " + newdestip);
	        //TODO : en cours
	        String rule = "{\"dpid\":2, \"priority\":111, \"match\":{\"in_port\":2, \"eth_type\":2048, \"ipv4_src\":[\"192.168.0.251\",\"192.168.0.250\"], " + 
	        				"\"ipv4_dst\":\""+olddestip+"\"}, \"actions\":[{\"type\":\"SET_FIELD\", \"field\":\"ipv4_dst\", \"value\":\""+newdestip+"\"}," +
	        						"{\"type\":\"SET_FIELD\", \"field\":\"eth_dst\", \"value\":\"00:00:00:00:00:26\"},{\"type\":\"OUTPUT\", \"port\":3}]}";
	        /*String rule = "{\"dpid\":2, \"idle_timeout\":3000,\"hard_timeout\" : 3000,\"priority\":11111,\"match\":{"+
	            	"\"in_port\":2,\"eth_type\":\"0x0800\",\"ipv4_src\":\"\",\"ipv4_192.168.0.251\":\""+olddestip+"\"},"+
	        		"\"actions\":[{\"type\":\"SET_FIELD\",\"field\":\"ipv4_dst\",\"value\":\""+ "192.168.0.253"+"\"},"+
	        		"{\"type\":\"OUTPUT\",\"port\":4}]}";*/
        	System.out.println("First Redirection rule : "+rule);
	        byte[] instructions = rule.getBytes("utf-8");
	        int length = instructions.length;
	        URL url = new URL("http://localhost:8080/stats/flowentry/add");
        	URLConnection con = url.openConnection();
	        HttpURLConnection http = (HttpURLConnection)con;
	        http.setRequestMethod("POST");
        	http.setDoOutput(true);
        	http.setFixedLengthStreamingMode(length);
        	http.setRequestProperty("Content-Type", "application/json; utf-8");
        	http.setRequestProperty("Accept","application/json");
        	http.connect();
        	java.io.OutputStream os = http.getOutputStream();
        	os.write(instructions,0,length);
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(
                    (http.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            http.disconnect();
	        //System.out.println("Sent rule :"+ rule.toString() + "\n Result :" + http.getResponseMessage());
	        // ---------------------------------------------------------------------------------
	        rule = "{\"dpid\":4, \"priority\":111, \"match\":{\"in_port\":3, \"eth_type\":2048, \"ipv4_src\":\"192.168.0.251\", " + 
    				"\"ipv4_dst\":\""+olddestip+"\"}, \"actions\":[{\"type\":\"SET_FIELD\", \"field\":\"ipv4_dst\", \"value\":\""+newdestip+"\"}," +
					"{\"type\":\"SET_FIELD\", \"field\":\"eth_dst\", \"value\":\"00:00:00:00:00:26\"},{\"type\":\"OUTPUT\", \"port\":2}]}";
	        instructions = rule.getBytes("utf-8");
	        length = instructions.length;
	        url = new URL("http://localhost:8080/stats/flowentry/modify");
        	con = url.openConnection();
	        http = (HttpURLConnection)con;
	        http.setRequestMethod("POST");
        	http.setDoOutput(true);
        	http.setFixedLengthStreamingMode(length);
        	http.setRequestProperty("Content-Type", "application/json; utf-8");
        	http.setRequestProperty("Accept","application/json");
        	http.connect();
        	os = http.getOutputStream();
        	os.write(instructions,0,length);
	        
	        br = new BufferedReader(new InputStreamReader(
                    (http.getInputStream())));

            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            http.disconnect();
         
	        status = "OK";
        }catch(Exception e){
        	e.printStackTrace();
        }
        return status;
    }

    String insert_a_loadbalancer(String oldgwip, String lbip, List<String> newgwsip) {
        String status = "KO";
        Main.logger(this.getClass().getSimpleName(), "oldgwip = " + oldgwip + "; lbip = " + lbip + "; newgwsip = " + newgwsip);
        //TODO
        try{
        	byte[] instructions = ("{\"switch\":\"00:00:00:00:00:00:00:02\", \"name\":\"f2\", \"priority\":\"36000\", \"in_port\":\"2\", " +
        			" \"ipv4_dst\":\""+oldgwip+"\", \"actions\":\"set_field=ipv4_dst->"+lbip+",output=3\"}").getBytes();
	        int length = instructions.length;
	        URL url = new URL("http://127.0.0.1:8080/wm/staticflowentrypusher/json");
        	URLConnection con = url.openConnection();
	        HttpURLConnection http = (HttpURLConnection)con;
	        http.setRequestMethod("POST");
        	http.setDoOutput(true);
        	http.setFixedLengthStreamingMode(length);
        	http.setRequestProperty("Content-Type", "application/json; utf-8");
        	http.setRequestProperty("Accept","application/json");
        	http.connect();
        	OutputStream os = (OutputStream) http.getOutputStream();
        	os.write(instructions,0,length);
	        status = "OK";
        }catch(Exception e){
        	e.printStackTrace();
        }
        	
        return status;
    }

    String remove_less_important_traffic(String importantsrcip) {
        String status = "OK";
        Main.logger(this.getClass().getSimpleName(), "importantsrcip = " + importantsrcip);
        //TODO
        return status;
    }


}
