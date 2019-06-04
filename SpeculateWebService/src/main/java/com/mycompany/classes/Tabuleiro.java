/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.classes;

public class Tabuleiro {
	private Boolean[] casas = new Boolean[6];
	
	public Tabuleiro() {
		this.inicializaTabuleiro();
	}
	
	private void inicializaTabuleiro() {
		this.casas[0] = true;
		this.casas[1] = false;
		this.casas[2] = true;
		this.casas[3] = false;
		this.casas[4] = true;
		this.casas[5] = false;
	}
	
	public Boolean recebeJogada(int casa) {
		// usado para passar o valor do dado para um indice de array
		int casaArray = casa - 1;
		if(casaArray < 5) {
			this.casas[casaArray] = !this.casas[casaArray];
			return !this.casas[casaArray];
		}
		return false;
	}
	
	public String obtemEstadoDoTabuleiro() {
		String tabuleiro = "";
		for(int i=0;i<this.casas.length;i++) {
			if(!this.casas[i]) {
				tabuleiro = tabuleiro + (i + 1);
			} else {
				tabuleiro = tabuleiro + "*";
			}
		}
		return tabuleiro;
	}
}