/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 *
 * @author Павел
 */
public class Security {
    
    
    public static String getHashFromString(Object target)throws Exception
    {
    	String strForHash = target + "";

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(strForHash.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
        
    	System.out.println("Hex format : " + hexString.toString());
        
        return hexString.toString();
    }  
    
    public static String getPasswordFromFile(String fullFileName)
    {      
        String configPath = fullFileName;
 
        String filePath = configPath;
        
        StringBuilder strBuilder = new StringBuilder();    

        // 
	try {
		File fileDir = new File(filePath);

		BufferedReader in = new BufferedReader(
		   new InputStreamReader(
                      new FileInputStream(fileDir), "UTF8"));

		String str;

		while ((str = in.readLine()) != null) {
		    //System.out.println(str);
                    strBuilder.append(str);
		}

                in.close();
	    }
	    catch (UnsupportedEncodingException e)
	    {
                System.out.println(e.getMessage());
	    }
	    catch (IOException e)
	    {
                System.out.println(e.getMessage());
	    }
	    catch (Exception e)
	    {
                System.out.println(e.getMessage());
	    }
        
      
        
        return strBuilder.toString();
    }    
    
}
