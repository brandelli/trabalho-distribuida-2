/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.speculateclient;

import java.io.IOException;

/**
 *
 * @author brandelli
 */
public class SpeculateClient {
    public static void main(String[] args) throws IOException{
        
        try { // Call Web Service Operation
            com.mycompany.speculatewebservice.SpeculateWS_Service service = new com.mycompany.speculatewebservice.SpeculateWS_Service();
            com.mycompany.speculatewebservice.SpeculateWS port = service.getSpeculateWSPort();
            // TODO initialize WS operation arguments here
            java.lang.String name = "Bruno";
            // TODO process result here
            int result = port.registraJogador(name);
            System.out.println("Result = "+result);
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }

    }
}
