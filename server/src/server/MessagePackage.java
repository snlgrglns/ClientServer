/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author SANIL
 */
public class MessagePackage {
    public String message;
    public int msgType;
    
    public MessagePackage(int msgType, String message){
        this.message = message;
        this.msgType = msgType;
    }
    
    public String toString(){
        return  msgType + "[messageSplitToken]" + message;
    }
}
