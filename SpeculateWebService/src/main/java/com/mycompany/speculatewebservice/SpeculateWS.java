/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.speculatewebservice;

import com.mycompany.classes.Jogador;
import com.mycompany.classes.Partida;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author brandelli
 */
@WebService(serviceName = "SpeculateWS")
public class SpeculateWS {
    private static final long serialVersionUID = 1234L;
    private static final int maxJogos = 500;
    private static final int maxJogadores = 2  * maxJogos;
    private static Map<String, Integer> preRegistro = new Hashtable<String, Integer>(maxJogadores);
    private static Map<Integer, Partida> dictPartidas = new Hashtable<Integer, Partida>(maxJogadores);
    private static Map<String, Jogador> jogadoresRegistrados = new Hashtable<String, Jogador>(maxJogadores);
    private static Map<Integer, Partida> partidasRegistradas = new Hashtable<Integer, Partida>(maxJogos);
    private static Jogador jogadorEmEspera = null;
    private static Semaphore semaforo = new Semaphore(1);
    private static int partidaID = 0;
    
    private Partida getPartida(int id) {
	return dictPartidas.get(id);
    }
    
    @WebMethod(operationName = "preRegistro")
    public int preRegistro(@WebParam(name = "p1Name") String p1Name, @WebParam(name = "p1ID") int p1ID,
            @WebParam(name = "p2Name") String p2Name, @WebParam(name = "p2ID") int p2ID) {
        preRegistro.put(p1Name, p1ID);
        preRegistro.put(p1Name, p2ID);
        return 0;
    }
    
    @WebMethod(operationName = "registraJogador")
    public int registraJogador(@WebParam(name = "name") String name) {
        int codigoDeRetorno = 0;
        try {
                semaforo.acquire();
                //caso o jogador j� esteja cadastrado
                if(jogadoresRegistrados.containsKey(name)) {
                        codigoDeRetorno = -1;
                //caso o n�mero de jogadores esteja no limite
                } else if(jogadoresRegistrados.size() == maxJogadores) {
                        codigoDeRetorno = -2;
                // caso o maximo de partidas suportadas tenha sido alcan�ado
                }else if(partidasRegistradas.size() == maxJogos) {
                        return -3;
                //caso esteja tudo OK para cadastrar o jogador
                } else {
                        int id = preRegistro.get(name);
                        preRegistro.remove(name);
                        Jogador jogador = new Jogador(id, name);
                        //caso tenha algum jogador esperando partida
                        if(jogadorEmEspera != null) {
                                Partida partida = dictPartidas.get(jogadorEmEspera.getId());
                                partida.setJogador1(jogadorEmEspera);
                                partida.setJogador2(jogador);
                                partida.criaDado();
                                partida.criaTabuleiro();
                                partida.setProximaJogada(jogadorEmEspera);
                                dictPartidas.put(id, partida);
                                jogadoresRegistrados.put(name, jogador);
                                System.out.println("Partida criada entre "+ jogadorEmEspera.getNome() +" e " + name);
                                jogadorEmEspera = null;
                        }else {
                                jogadorEmEspera = jogador;
                                jogadoresRegistrados.put(name, jogador);
                                dictPartidas.put(id, new Partida(partidaID++));
                        }
                        codigoDeRetorno = id;
                }
        } catch(Exception e) {
                System.out.println("Erro em registraJogador");
                e.printStackTrace();
        } finally {
                semaforo.release();
        }
        return codigoDeRetorno;
    }
    
    @WebMethod(operationName = "encerraPartida")
    public int encerraPartida(@WebParam(name = "id") int id) {
        int codigoDeRetorno = -1;
        try {
                semaforo.acquire();
                Partida partida = this.getPartida(id);
                // existe a partida deste jogador
                if(partida != null) {
                        // partida est� sendo encerrada antes do termino do jogo
                        if(partida.getVencedor() == null) {
                                partida.setDesistencia(true);
                        // partida esta sendo encerrada depois do vencedor ser definido
                        } else {
                                // caso um jogador j� tenha encerrado a sua sess�o, os requicios da partida s�o limpos
                                if(partida.getPartidaTerminada()) {
                                        partidasRegistradas.remove(partida.getId());
                                        System.out.println("Partida entre " + partida.getJogador1().getNome() + " e " + partida.getJogador2().getNome() + " finalizada");
                                // caso contrario, o primeiro cliente a fechar a partida indica na classe partida
                                } else {
                                        partida.terminarPartida();
                                }
                                // a liga��o entre jogador e partida � removida da estrutura
                                dictPartidas.remove(id);
                                String nome = "";
                                if(partida.getJogador1().getId() == id) {
                                        nome = partida.getJogador1().getNome();
                                } else {
                                        nome = partida.getJogador2().getNome();
                                }
                                // o registro do jogador � removido da estrutura
                                jogadoresRegistrados.remove(nome);
                        }
                }
        } catch (Exception e) {
                System.out.println("Erro em encerraPartida");
                e.printStackTrace();
        } finally {
                semaforo.release();
        }
        return codigoDeRetorno;
    }
    
    @WebMethod(operationName = "temPartida")
    public int temPartida(@WebParam(name = "id") int id) {
        Partida partida = this.getPartida(id);
        if(partida == null) {
                return -1;
        }

        Jogador jogador1 = partida.getJogador1();
        Jogador jogador2 = partida.getJogador2();

        if(jogador2 == null) {
                return 0;
        }

        if(jogador1.getId() == id) {
                return 1;
        }

        if(jogador2.getId() == id) {
                return 2;
        }

        return -2;
    }
    
    @WebMethod(operationName = "obtemOponente")
    public String obtemOponente(@WebParam(name = "id") int id) {
        Partida partida = this.getPartida(id);
        if(partida != null) {
                Jogador jogador1 = partida.getJogador1();
                Jogador jogador2 = partida.getJogador2();

                if(jogador1.getId() == id) {
                        if(jogador2 != null) {
                                return jogador2.getNome();
                        }
                } else {
                        return jogador1.getNome();
                }
        }
        return "";
    }
    
    @WebMethod(operationName = "ehMinhaVez")
    public int ehMinhaVez(@WebParam(name = "id") int id) {
        Partida partida = this.getPartida(id);
		
        // jogador n�o encontrado em nunhuma partida
        if(partida == null) {
                return -1;
        }

        // caso n�o tenha 2 jogadores na partida
        if(partida.getJogador2() == null) {
                return -2;
        }

        // caso ainda n�o tenha vencedor na partida
        if(partida.getVencedor() == null) {
                // proxima jogada � do cliente que fez a chamada
                if(partida.getProximaJogada().getId() == id) {
                        return 1;
                }
                // proxima jogada � do oponente;
                return 0;
        // j� est� definido o vencedor
        } else {
                Jogador vencedor = partida.getVencedor();
                // caso o jogo tenha acabado devido a uma desistencia
                if(partida.getDesistencia() == true) {
                        // vencedor por WO � o cliente que fez a chamada
                        if(vencedor.getId() == id) {
                                return 5;
                        }
                        // perdedor por WO � o cliente qu efez a chamada
                        return 6;
                } else {
                        // vencedor � o cliente que fez a chamada
                        if(vencedor.getId() == id) {
                                return 2;
                        }
                        // perdedor � o cliente que fez a chamada
                        return 3;
                }
        }
    }
    
    private int obtemBolasDoJogador(int id, Boolean minhasBolas) {
        Partida partida = this.getPartida(id);
        // caso o jogador n�o esteja em nenhuma partida
        if(partida == null) {
                return -1;
        }

        Jogador jogador1 = partida.getJogador1();
        Jogador jogador2 = partida.getJogador2();

        // caso n�o tenha um segundo jogador cadastrado na partida
        if(jogador2 == null) {
                return -2;
        }

        // caso esteja querendo saber quantas bolas eu tenho
        if(minhasBolas) {
                if(jogador1.getId() == id) {
                        return jogador1.getBolas();
                }
                return jogador2.getBolas();
        // caso esteja querendo saber quantas bolas meu oponente tem
        } else {
                if(jogador1.getId() == id) {
                        return jogador2.getBolas();
                }
                return jogador1.getBolas();
        }

    }
    
    @WebMethod(operationName = "obtemNumBolas")
    public int obtemNumBolas(@WebParam(name = "id") int id) {
        return this.obtemBolasDoJogador(id, true);
    }
    
    @WebMethod(operationName = "obtemNumBolasOponente")
    public int obtemNumBolasOponente(@WebParam(name = "id") int id) {
        return this.obtemBolasDoJogador(id, false);
    }
    
    @WebMethod(operationName = "obtemTabuleiro")
    public String obtemTabuleiro(@WebParam(name = "id") int id) {
        Partida partida = this.getPartida(id);
        if(partida == null || partida.getTabuleiro() == null)
                return "";

        return partida.getTabuleiro().obtemEstadoDoTabuleiro();
    }
    
    @WebMethod(operationName = "defineJogadas")
    public int defineJogadas(@WebParam(name = "id") int id, @WebParam(name = "jogadas") int jogadas) {
        Partida partida = this.getPartida(id);

        //caso n�o tenha partida
        if(partida == null) {
                return -2;
        }

        //erro, acredito que seja n�o ter o jogador 2
        if(partida.getJogador2() == null) {
                return -1;
        }

        // caso n�o seja a vez do jogaodr
        if(partida.getProximaJogada().getId() != id) {
                return -3;
        }

        // caso seja a vez do jogador
        if(partida.getProximaJogada().getId() == id) {
                // � a vez do jogador mas o jogo j� foi encerrado
                if(partida.getVencedor() != null) {
                        return -4;
                }

                // jogada invalida
                if(jogadas < 1 || jogadas > partida.getProximaJogada().getBolas()) {
                        return -5;
                }

                partida.setJogadas(jogadas);

                // jogada valida
                return 1;
        }




        return -1;
    }
    
    @WebMethod(operationName = "jogaDado")
    public int jogaDado(@WebParam(name = "id") int id) {
        Partida partida = this.getPartida(id);

        // caso ainda n�o tenha partida
        if(partida == null) {
                return -2;
        }

        //erro, acredito que seja n�o ter o jogador 2
        if(partida.getJogador2() == null) {
                return -1;
        }

        // caso n�o seja a vez do jogaodr
        if(partida.getProximaJogada().getId() != id) {
                return -3;
        }

        if(partida.getProximaJogada().getId() == id) {
                // � a vez do jogador mas o jogo j� foi encerrado
                if(partida.getVencedor() != null) {
                        return -4;
                }

                return partida.jogaDados();
        }


        return -1;
    }
}
