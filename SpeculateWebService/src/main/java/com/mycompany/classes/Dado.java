/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.classes;

import java.util.Random;
public class Dado {
    private static Random random = new Random();
    public static int jogaDado() {
            return random.nextInt(5) + 1;
            //return 6;
    }
}
