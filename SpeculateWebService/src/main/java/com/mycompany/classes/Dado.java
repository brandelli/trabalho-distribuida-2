/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.classes;

import java.util.Random;
public class Dado {
    
    private Random random;
    
    public Dado(int seed1, int seed2){
        this.random = new Random(seed1 + seed2);
    }
    
    public int jogaDado() {
        return random.nextInt(6) + 1;
    }
}
