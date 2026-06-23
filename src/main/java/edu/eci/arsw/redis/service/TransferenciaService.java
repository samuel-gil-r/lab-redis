package edu.eci.arsw.redis.service;

import edu.eci.arsw.redis.config.RedisConexion;
import edu.eci.arsw.redis.model.TransferenciaCreada;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XAddParams;

public class TransferenciaService {

    private static final String STREAM = "banco.transferencias";
    private static final String[] GRUPOS = {"fraude-group", "notif-group", "auditoria-group"};

    public void inicializar() {
        Jedis jedis = RedisConexion.getInstancia();
        for (String grupo : GRUPOS) {
            try {
                jedis.xgroupCreate(STREAM, grupo, StreamEntryID.LAST_ENTRY, true);
            } catch (Exception e) {
                // grupo ya existe
            }
        }
    }

    public StreamEntryID publicar(TransferenciaCreada evento) {
        Jedis jedis = RedisConexion.getInstancia();
        return jedis.xadd(STREAM, XAddParams.xAddParams(), evento.toMap());
    }

    public long totalEventos() {
        return RedisConexion.getInstancia().xlen(STREAM);
    }
}
