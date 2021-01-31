import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;

//
//* @author couedrao on 25/11/2019.
//* @project gctrl
//
class Tester {

    private static final Knowledge k = new Knowledge();

    public static void main(String[] args) throws Exception {
        k.start();
        /*List<String> workflow_lists = k.get_worklow_lists();
        while (Main.run) {
            logger("You are in test mode! The following actions can be performed : ");
            for (int i = 0; i < workflow_lists.size(); i++) {
                logger("[" + i + "] :" + workflow_lists.get(i));
            }
            logger("Select any number in [0-" + (workflow_lists.size() - 1) + "] to continue");
            int input = new Scanner(System.in).nextInt();
            if (input < workflow_lists.size() )
                logger("Execution of Action : [" + workflow_lists.get(input) + "]");
            else logger("(-_-)");

            //Call Effectors
            TODO : 
        }*/
        
        Monitor mon = new Monitor();
        mon.get_data();
        MANOAPI mano = new MANOAPI();
        //mano.deploy_gw(k.getGwinfo());
        SDNCtrlAPI sdn = new SDNCtrlAPI();
        sdn.redirect_traffic(k.getOlddestip(),k.getNewdestip());
    }

    private static void logger(String msg) {
        if (true)
            System.out.println(msg);
    }
}
