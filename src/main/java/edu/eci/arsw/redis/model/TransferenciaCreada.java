package edu.eci.arsw.redis.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransferenciaCreada {

    private final String eventId;
    private final String transferId;
    private final String from;
    private final String to;
    private final String amount;
    private final String currency;
    private final String createdAt;

    public TransferenciaCreada(String from, String to, long amount) {
        this.eventId    = "evt-" + UUID.randomUUID().toString().substring(0, 8);
        this.transferId = "tr-"  + UUID.randomUUID().toString().substring(0, 8);
        this.from       = from;
        this.to         = to;
        this.amount     = String.valueOf(amount);
        this.currency   = "COP";
        this.createdAt  = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("eventType",   "TransferenciaCreada");
        map.put("eventId",     eventId);
        map.put("transferId",  transferId);
        map.put("from",        from);
        map.put("to",          to);
        map.put("amount",      amount);
        map.put("currency",    currency);
        map.put("createdAt",   createdAt);
        return map;
    }

    @Override
    public String toString() {
        return String.format("eventId=%s | %s -> %s | $%s %s | %s",
                eventId, from, to, amount, currency, createdAt);
    }
}
