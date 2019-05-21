package NerdyGadgets;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class CheckServers {
    private ArrayList<String> ipAddresses = new ArrayList<>();
    private String wanIpAddress;

    public CheckServers(){
        if(getConfig()){
            System.out.println("Checking local servers:");
            for (String ipAddress : ipAddresses){
                try{
                    InetAddress a = InetAddress.getByName(ipAddress);
                    if(a.isReachable(2000)){
                        System.out.println(ipAddress + " works!");
                    }else {
                        System.out.println(ipAddress + " is not reachable!");
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

            }

            try{
                if(InetAddress.getByName("your server url").isReachable(2000)){
                    System.out.println("Your network is reachable from the wan via " + wanIpAddress);
                }else {
                    System.out.println("Your network is not reachable via wan " + wanIpAddress);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }








    //Get the config file to make all the ip-addresses easy configurable
    public boolean getConfig(){
        File conf = new File("ipAddresses.config");

        Properties prop = new Properties();
        InputStream is = null;
        try{
            is = new FileInputStream("ipAddresses.config");
        }catch (FileNotFoundException e){
            List<String> lines = Arrays.asList("# Write all the ip addresses in this file behind 'ipAddresses=' separated by commas.", "ipAddresses=", "# The wan address is the address provided by your Internet Service Provider", "wanAddress=");
            try{
                conf.setExecutable(true);
                conf.setReadable(true);
                conf.setWritable(true);
                Files.write(Paths.get("ipAddresses.config"), lines, StandardOpenOption.CREATE);
                System.out.println("Please fill in the config file!");
            }catch (IOException ef){

                System.out.println(ef.getMessage());
            }

            return false;
        }

        try {
            prop.load(is);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        try{
            String allIpAddresses = prop.getProperty("ipAddresses");
            allIpAddresses = allIpAddresses.replaceAll(" ", "");
            String[] values = allIpAddresses.split(",");
            for(String s : values){
                ipAddresses.add(s);
            }

            wanIpAddress = prop.getProperty("wanAddress");


        }catch(Exception e){
            System.out.println("Make sure your ipAddresses.config has the right format");
            return false;

        }

        return true;

    }





}




