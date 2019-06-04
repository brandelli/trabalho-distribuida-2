/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.classes;

public class Partida {
	private Jogador jogador1 = null;
	private Jogador jogador2 = null;
	private Jogador proximaJogada = null;
	private Jogador vencedor = null;
	private Boolean desistencia = false;
	private Tabuleiro tabuleiro = null;
	private int id;
	private Boolean partidaTerminada = false;
	private int jogadas = 0;

	public Partida(int id) {
		this.id = id;
	}
	
	public void setJogador1(Jogador jogador) {
		this.jogador1 = jogador;
	}
	
	public void setJogador2(Jogador jogador) {
		this.jogador2 = jogador;
	}
	
	public Jogador getJogador1() {
		return this.jogador1;
	}
	
	public Jogador getJogador2() {
		return this.jogador2;
	}
	
	public void setProximaJogada(Jogador proximo) {
		this.proximaJogada = proximo;
	}
	
	public Jogador getProximaJogada() {
		return this.proximaJogada;
	}
	
	public void setVencedor(Jogador vencedor) {
		this.vencedor = vencedor;
	}
	
	public Jogador getVencedor() {
		return this.vencedor;
	}
	
	private void jogadorDesistente() {
		// faz a verifica��o de qual jogador desistiu da partida
		int id = proximaJogada.getId();
		if(id == jogador1.getId()) {
			this.vencedor = jogador2;
			this.proximaJogada = jogador2;
		} else {
			this.vencedor = jogador1;
			this.proximaJogada = jogador1;
		}
	}
	
	public void setDesistencia(Boolean desistencia) {
		this.jogadorDesistente();
		this.desistencia = desistencia;
	}
	
	public Boolean getDesistencia() {
		return this.desistencia;
	}
	
	public Tabuleiro getTabuleiro() {
		return this.tabuleiro;
	}
	
	public void criaTabuleiro() {
		this.tabuleiro = new Tabuleiro();
	}
	
	public int jogaDados() {
		int dado = Dado.jogaDado();
		boolean efeito = this.tabuleiro.recebeJogada(dado);
		// verifica o efeito da jogada no tabuleiro
		// true -> jogador recebe uma bola, porque a casa j� estava ocupada
		if(efeito) {
			proximaJogada.recebeBola();
		// false -> jogador se desfaz da bola, porque a casa estava vazia 
		} else {
			proximaJogada.descartaBola();
		}
		this.jogadas--;
		this.verificaFimTurno();
		return dado;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Boolean getPartidaTerminada() {
		return this.partidaTerminada;
	}
	
	public void terminarPartida() {
		this.partidaTerminada = true;
	}
	
	public void setJogadas(int jogadas) {
		this.jogadas = jogadas;
	}
	
	public int getJogadas() {
		return this.jogadas;
	}
	
	private void verificaFimTurno() {
		// verifica se as jogadas do jogador atual acabaram
		if(this.jogadas == 0) {
			// se n�o restou nenhuma bola ele � o vencedor
			if(this.proximaJogada.getBolas() == 0) {
				this.vencedor = this.proximaJogada;
			}
			this.trocaProximoJogador();
		}
	}
	
	private void trocaProximoJogador() {
		// muda o turno para o proximo jogador
		int id = this.proximaJogada.getId();
		if(jogador1.getId() == id) {
			this.proximaJogada = jogador2;
		} else {
			this.proximaJogada = jogador1;
		}
	}
	
}