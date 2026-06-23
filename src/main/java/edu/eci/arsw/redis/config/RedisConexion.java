package edu.eci.arsw.redis.config;

import redis.clients.jedis.Jedis;

public class RedisConexion {

    private static final String HOST = "localhost";
    private static final int PORT = 6379;

    private static Jedis instancia;

    private RedisConexion() {}

    public static Jedis getInstancia() {
        if (instancia == null || !instancia.isConnected()) {
            instancia = new Jedis(HOST, PORT);
        }
        return instancia;
    }

    public static void cerrar() {
        if (instancia != null && instancia.isConnected()) {
            instancia.close();
        }
    }
}
