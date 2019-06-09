/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.speculateclient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author brandelli
 */
public class SpeculateClient {
    public static com.mycompany.speculatewebservice.SpeculateWS port; 
    
    public static void main(String[] args) throws IOException{
        com.mycompany.speculatewebservice.SpeculateWS_Service service = new com.mycompany.speculatewebservice.SpeculateWS_Service();
        port = service.getSpeculateWSPort();

        executaTeste("Speculate-2000",false);

    }
    
    private static void executaTesteParalelo(String[] rad) throws IOException {
        int numTestes = rad.length;
        Thread[] threads = new Thread[numTestes];
        for (int i=0;i<numTestes;++i) {
            String r = rad[i];
            threads[i] = new Thread() {
                @Override
                public void run() {
                    try {
                        executaTeste(r,false);
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }
                }
            };
        }
        for (int i=0;i<numTestes;++i)
            threads[i].start();
        for (int i=0;i<numTestes;++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.err.println("Falha ao esperar por teste ("+rad[i]+").");
                e.printStackTrace(System.err);
            }
        }
    }

    private static void executaTeste(final String rad,final boolean contagem) throws IOException {
        String inFile = rad+".in";
        String outFile = rad+".out";
        Scanner in;
        try (PrintWriter out = new PrintWriter(outFile)) {
            in = new Scanner(new File(inFile));
            int numOp = in.nextInt();
            for (int i=0;i<numOp;++i) {
                if (contagem)
                    System.out.print("\r"+rad+": "+(i+1)+"/"+numOp);
                int op = in.nextInt();
                String parametros = in.next();
                String param[] = parametros.split(":",-1);
                switch(op) {
                    case 0:
                        if (param.length!=4)
                            erro(inFile,i+1);
                        else{
                            String p1 = param[0];
                            int p1ID = Integer.parseInt(param[1]);
                            String p2 = param[2];
                            int p2ID = Integer.parseInt(param[3]);
                            out.println(port.preRegistro(p1, p1ID, p2, p2ID));
                        }
                            
                        break;
                    case 1:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.registraJogador(param[0]));
                        break;
                    case 2:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.encerraPartida(Integer.parseInt(param[0])));
                        break;
                    case 3:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.temPartida(Integer.parseInt(param[0])));
                        break;
                    case 4:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemOponente(Integer.parseInt(param[0])));
                        break;
                    case 5:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.ehMinhaVez(Integer.parseInt(param[0])));
                        break;
                    case 6:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemNumBolas(Integer.parseInt(param[0])));
                        break;
                    case 7:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemNumBolasOponente(Integer.parseInt(param[0])));
                        break;
                    case 8:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemTabuleiro(Integer.parseInt(param[0])));
                        break;
                    case 9:
                        if (param.length!=2)
                            erro(inFile,i+1);
                        else
                            out.println(port.defineJogadas(Integer.parseInt(param[0]),Integer.parseInt(param[1])));
                        break;
                    case 10:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.jogaDado(Integer.parseInt(param[0])));
                        break;
                    default:
                        erro(inFile,i+1);
                }
            }   if (contagem)
                System.out.println("... terminado!");
            else
                System.out.println(rad+": "+numOp+"/"+numOp+"... terminado!");
        }
        in.close();
    }
    private static void erro(String arq,int operacao) {
        System.err.println("Entrada invalida: erro na operacao "+operacao+" do arquivo "+arq);
        System.exit(1);
    }
}
