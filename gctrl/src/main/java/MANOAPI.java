import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author couedrao on 27/11/2019.
 * @project gctrl
 */
class MANOAPI {

	
	//To deploy a single gw as through vnf infos
    String deploy_gw(Map<String, String> vnfinfos) {
        String ip = "192.168.0.253";
        Main.logger(this.getClass().getSimpleName(), "Deploying VNF ...");
        String infos = "{" ;
        //printing
        for (Entry<String, String> e : vnfinfos.entrySet()) {
            Main.logger(this.getClass().getSimpleName(), "\t" + e.getKey() + " : " + e.getValue());
            infos += e.getKey() + " : " + e.getValue() + ",";
        }
        infos = infos.substring(0, infos.length() - 1);
        infos += "}" ;
        //TODO : en cours
        try{
        	System.out.println(infos.toString());
        	byte[] instructions = infos.getBytes("utf-8");
        	int length = instructions.length;
        	URL url = new URL("http://127.0.0.1:5001/restapi/compute/dc1/gwi2");
        	URLConnection con = url.openConnection();
	        HttpURLConnection http = (HttpURLConnection)con;
	        http.setRequestMethod("PUT");
        	http.setDoOutput(true);
        	http.setFixedLengthStreamingMode(length);
        	http.setRequestProperty("Content-Type", "application/json; utf-8");
        	http.setRequestProperty("Accept","application/json");
        	http.connect();
        	java.io.OutputStream os = http.getOutputStream();
            os.write(instructions);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (http.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            http.disconnect();
	        System.out.println("Sent rule :"+ infos + "\n Result :" + http.getResponseMessage());
        }catch(Exception e){
        	e.printStackTrace();
        }
        /*
        - déployer instance container sur dc
        - envoyer règles niveau switch ou vnf est un loadbalancer
*/
        return ip;
    }

    //To deploy multiple GW and Load Balancers
    List<String> deploy_multi_gws_and_lb(List<Map<String, String>> vnfsinfos) {
        List<String> ips = new ArrayList<>();
        //TODO

        for (Map<String, String> vnfsinfo : vnfsinfos) {
            ips.add(deploy_gw(vnfsinfo));
        }

        return ips;
    }
}
