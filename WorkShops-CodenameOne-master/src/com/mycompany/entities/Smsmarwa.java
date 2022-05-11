/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yummygout.entities;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
/**
 *
 * @author marwa
 */
public class Smsmarwa {

    public static final String ACCOUNT_SID = "AC2da37ed7d6aae2c8c19a7ef160cb826e";
    public static final String AUTH_TOKEN = "0f3ad4c4f0b33a801c065eefa66f55c8";
   
    public static void send1(String messagecontent){
        int num=28805090;
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+21693863919"),
                new com.twilio.type.PhoneNumber("+19896629571"),messagecontent).create();
        System.out.println(message.getSid());
    }
    
}