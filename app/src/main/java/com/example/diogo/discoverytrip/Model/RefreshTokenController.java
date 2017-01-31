package com.example.diogo.discoverytrip.Model;

/**
 * Created by Renato on 31/01/17.
 *
 * Thead que aciona o método de refresh do token ao se aproximar do tempo de vencimento do mesmo.
 * Essa thread é iniciada quando o login é efetuado.
 */
public class RefreshTokenController extends Thread{
    private long time;
    private String refreshToken;

    // Um tempo de segurança para não fazer a requisição em cima da hora e não ter tempo suficiente (5 min).
    private static final long SAFE_TIME = 300000;

    /**
     * A thread é instanciada com o tempo do ciclo de renovação do token.
     * @param time tempo em milisegundos para renocação do token.
     * @param refreshToken token de refresh inicial, depois do primeiro refresh
     *                     a thread trata os refreshTokens automaticamente
     */
    RefreshTokenController(long time, String refreshToken){
        this.time = time * 1000;
        this.refreshToken = refreshToken;
    }

    @Override
    public void start(){
        super.start();
        try {
            while(true) {
                sleep(time - SAFE_TIME);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}