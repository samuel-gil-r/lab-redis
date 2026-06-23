package edu.eci.arsw.redis.producer;

import edu.eci.arsw.redis.config.RedisConexion;
import edu.eci.arsw.redis.model.TransferenciaCreada;
import edu.eci.arsw.redis.service.TransferenciaService;
import redis.clients.jedis.StreamEntryID;

import java.util.Scanner;

public class ProductorTransferencias {

    public static void main(String[] args) {
        TransferenciaService service = new TransferenciaService();
        service.inicializar();

        System.out.println("=== Productor de Transferencias ===");
        System.out.println("Stream: banco.transferencias | Grupos: fraude, notif, auditoria");
        System.out.println("Escribe 'salir' para terminar.\n");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Cuenta origen : ");
                String from = scanner.nextLine().trim();
                if (from.equalsIgnoreCase("salir")) break;

                System.out.print("Cuenta destino: ");
                String to = scanner.nextLine().trim();
                if (to.equalsIgnoreCase("salir")) break;

                System.out.print("Monto (COP)   : ");
                String montoStr = scanner.nextLine().trim();
                if (montoStr.equalsIgnoreCase("salir")) break;

                long monto;
                try {
                    monto = Long.parseLong(montoStr);
                } catch (NumberFormatException e) {
                    System.out.println("Monto inválido.\n");
                    continue;
                }

                TransferenciaCreada evento = new TransferenciaCreada(from, to, monto);
                StreamEntryID id = service.publicar(evento);

                System.out.println("Publicado -> " + evento);
                System.out.println("Redis ID  -> " + id);
                System.out.println("Total en stream: " + service.totalEventos() + "\n");
            }
        }

        RedisConexion.cerrar();
        System.out.println("Productor cerrado.");
    }
}
