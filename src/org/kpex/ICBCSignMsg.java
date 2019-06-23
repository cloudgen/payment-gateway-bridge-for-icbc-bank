package org.kpex;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import cn.com.infosec.icbc.ReturnValue;

public class ICBCSignMsg {
  private String propertyKeyFile = null;
  private String propertyPassword = null;
  private String propertyCertFile = null;
  private String propertyCertBase64 = null;
  private String propertyKeyBase64 = null;
  private String propertyMessage = null;
  private String propertySignedMessage = null;
  private String propertyMessageBase64 = null;
  private String merchantCertifcateBase64;
  private String merchantKeyBase64;
  private String message = null;
  private String messageBase64 = null;
  private String signedMessageBase64 = null;
  private byte[] merchantKey;
  private byte[] messageArray;
  public void readProperty(String config){
    Properties icbcConfigFile = new Properties();
    try {
      FileInputStream configInputStream = new FileInputStream(config);
      icbcConfigFile.load(configInputStream);
      this.propertyPassword = icbcConfigFile.getProperty("password");
      // files for input
      this.propertyCertFile = icbcConfigFile.getProperty("cert");
      this.propertyKeyFile = icbcConfigFile.getProperty("key");
      this.propertyMessage = icbcConfigFile.getProperty("message");
      // files for output
      this.propertyCertBase64 = icbcConfigFile.getProperty("cert_base64");
      this.propertyMessageBase64 = icbcConfigFile.getProperty("message_base64");
      this.propertyKeyBase64 = icbcConfigFile.getProperty("key_base64");
      this.propertySignedMessage = icbcConfigFile.getProperty("signed_message_base64");
    } catch(FileNotFoundException e){
      System.err.println("File Not Found!");
    } catch(IOException e){
      System.err.println("I/O Error!");
    } catch(Exception e){
    }
  }
  public String base64enc(byte[] src){
    byte[] temp = ReturnValue.base64enc(src);
    String result = new String(temp).toString();
    return result;
  }
  public void encryptCertFileToBase64(){
    FileInputStream in1 = null;
    try {
      in1 = new FileInputStream(this.propertyCertFile);
      byte[] merchantCertifcate = new byte[in1.available()];
      in1.read(merchantCertifcate);
      in1.close();
      this.merchantCertifcateBase64 =this.base64enc(merchantCertifcate);
      // Output to file
      PrintWriter out = new PrintWriter(this.propertyCertBase64);
      out.print(this.merchantCertifcateBase64);
      out.close();
    } catch(FileNotFoundException e){
      System.err.println(this.propertyCertFile+". File Not Found!");
    } catch(IOException e){
      System.err.println(this.propertyCertFile+". I/O Error!");
    } catch(Exception e){
    }
  }
  public void encryptKeyToBase64(){
    FileInputStream in2 = null;
    try {
      if(this.propertyKeyFile!=""){
        in2 = new FileInputStream(this.propertyKeyFile);
        this.merchantKey = new byte[in2.available()];
        in2.read(this.merchantKey);
        in2.close();
        this.merchantKeyBase64 =this.base64enc(this.merchantKey);
        // Output to file
        PrintWriter out = new PrintWriter(this.propertyKeyBase64);
        out.println(this.merchantKeyBase64);
        out.close();
      } else {
        System.err.println("The property 'key' has no value!");
      }
    } catch(FileNotFoundException e){
      System.err.println(this.propertyKeyFile+". File Not Found!");
    } catch(IOException e){
      System.err.println(this.propertyKeyFile+". I/O Error!");
    } catch(Exception e){
    }
  }
  public void readMessageFile(){
    try {
      if(this.propertyMessage != ""){
        FileInputStream msgFile = null;
        msgFile = new FileInputStream(this.propertyMessage);
        byte[] temp = new byte[msgFile.available()];
        msgFile.read(temp);
        msgFile.close();
        this.message = new String(temp).toString().trim();
        this.messageArray = this.message.getBytes();
        this.messageBase64 =this.base64enc(this.messageArray);
        // Output to file
        PrintWriter out = new PrintWriter(this.propertyMessageBase64);
        out.print(this.messageBase64);
        System.out.println(this.messageBase64);
        out.close();
      } else {
        System.err.println("The property 'message' has no value!");
      }
    } catch(FileNotFoundException e){
      System.err.println(this.propertyMessage+". File Not Found!");
    } catch(IOException e){
      System.err.println(this.propertyMessage+". I/O Error!");
    } catch(Exception e){
    }
  }
  public void signMessage(){
    char[] keyPass = this.propertyPassword.toCharArray();
    try {
      byte[] sign = ReturnValue.sign(this.messageArray, this.messageArray.length, this.merchantKey, keyPass);
      if(sign == null){
        System.err.println("Sign error");
      } else {
        this.signedMessageBase64 = this.base64enc(sign);
        // Output to file
        PrintWriter out = new PrintWriter(this.propertySignedMessage);
        out.println(this.signedMessageBase64);
        out.close();
      }
    }catch (Exception e){
      System.err.println("Error in Signing!");
    }
  }
  public ICBCSignMsg(String config){
    this.readProperty(config);
    this.encryptCertFileToBase64();
    this.encryptKeyToBase64();
    this.readMessageFile();
    this.signMessage();
  }
  public static void main(String args[]){
    if(args.length > 0) {
      ICBCSignMsg app = new ICBCSignMsg(args[0]);
    }
  }
}
