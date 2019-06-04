/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.classes;

public class Jogador {
	private int id;
	private String nome;
	private int bolas;
	
	public Jogador(int id, String nome) {
		this.id = id;
		this.nome = nome;
		this.bolas = 15;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public int getBolas() {
		return this.bolas;
	}
	
	public int descartaBola() {
		this.bolas--;
		return bolas;
	}
	
	public int recebeBola() {
		this.bolas++;
		return bolas;
	}
}