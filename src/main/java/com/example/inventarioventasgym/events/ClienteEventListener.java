// src/main/java/com/example/inventarioventasgym/events/ClienteEventListener.java
package com.example.inventarioventasgym.events;

import com.example.inventarioventasgym.models.ClienteLocal;
import com.example.inventarioventasgym.repositories.ClienteLocalRepository;
import com.gym.events.dto.ClienteEventDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ClienteEventListener {

    private final ClienteLocalRepository repo;
    @Value("${cliente.events.topic:cliente-events}")
    private String topic;

    public ClienteEventListener(ClienteLocalRepository repo) {
        this.repo = repo;
    }

    @KafkaListener(
            topics = "${cliente.events.topic}",
            groupId = "inventario-ventas-consumer-group"
    )
    public void listenClienteEvent(ClienteEventDTO event) {
        if (event == null || event.getClienteId() == null) return;
        switch (event.getAccion()) {
            case "CREATED", "UPDATED" -> {
                ClienteLocal cliente = new ClienteLocal(
                        event.getClienteId(),
                        event.getNombreCompleto(),
                        event.getDocumento(),
                        event.getPesoInicio(),
                        event.getMembresiaId(),
                        true
                );
                repo.save(cliente);
            }
            case "DELETED" -> {
                repo.findById(event.getClienteId())
                        .ifPresent(c -> {
                            c.setEstado(false);
                            repo.save(c);
                        });
            }
        }
    }
}
