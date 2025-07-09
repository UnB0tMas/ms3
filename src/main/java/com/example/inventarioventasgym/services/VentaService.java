package com.example.inventarioventasgym.services;

import com.example.inventarioventasgym.dto.VentaCreateDTO;
import com.example.inventarioventasgym.dto.VentaResponseDTO;
import com.example.inventarioventasgym.models.ClienteLocal;
import com.example.inventarioventasgym.models.Producto;
import com.example.inventarioventasgym.models.Venta;
import com.example.inventarioventasgym.models.DetalleVenta;
import com.example.inventarioventasgym.repositories.ClienteLocalRepository;
import com.example.inventarioventasgym.repositories.ProductoRepository;
import com.example.inventarioventasgym.repositories.VentaRepository;
import com.example.inventarioventasgym.repositories.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final VentaRepository ventaRepo;
    private final ClienteLocalRepository clienteLocalRepo;
    private final ProductoRepository productoRepo;
    private final DetalleVentaRepository detalleVentaRepo;

    public VentaService(
            VentaRepository ventaRepo,
            ClienteLocalRepository clienteLocalRepo,
            ProductoRepository productoRepo,
            DetalleVentaRepository detalleVentaRepo
    ) {
        this.ventaRepo = ventaRepo;
        this.clienteLocalRepo = clienteLocalRepo;
        this.productoRepo = productoRepo;
        this.detalleVentaRepo = detalleVentaRepo;
    }

    public List<VentaResponseDTO> getAll() {
        return ventaRepo.findAll().stream().map(this::toResponseDTO).toList();
    }

    public Optional<VentaResponseDTO> getById(Integer id) {
        return ventaRepo.findById(id).map(this::toResponseDTO);
    }

    @Transactional
    public VentaResponseDTO create(VentaCreateDTO dto) {
        // 1. Validar cliente
        ClienteLocal cliente = clienteLocalRepo.findById(dto.getPersonaId())
                .orElseThrow(() -> new RuntimeException("El cliente/personaId " + dto.getPersonaId() + " no existe o aún no ha sido sincronizado"));

        // 2. Crear venta básica (sin detalles aún)
        Venta venta = new Venta();
        venta.setFechaVenta(dto.getFechaVenta());
        venta.setPersonaId(dto.getPersonaId());
        venta.setNombreCliente(cliente.getNombreCompleto());
        venta.setDocumentoCliente(cliente.getDocumento());
        venta.setIgv(dto.getIgv());
        venta.setMetodoPago(dto.getMetodoPago());
        venta.setTotal(dto.getTotal());
        venta.setEstado(true);

        Venta savedVenta = ventaRepo.save(venta);

        // 3. Procesar detalles (validar productos y stock, descontar stock)
        List<DetalleVenta> detalles = new ArrayList<>();
        for (VentaCreateDTO.DetalleVentaDTO det : dto.getDetalles()) {
            Producto producto = productoRepo.findById(det.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto " + det.getProductoId() + " no existe"));
            if (!producto.getEstado() || producto.getStock() < det.getCant()) {
                throw new RuntimeException("Producto sin stock suficiente: " + producto.getProductoId() + " (" + producto.getStock() + " disponible, se requiere " + det.getCant() + ")");
            }
            producto.setStock(producto.getStock() - det.getCant());
            productoRepo.save(producto);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setVenta(savedVenta);
            detalle.setCant(det.getCant());
            detalle.setEstado(true);
            detalles.add(detalle);
        }
        detalleVentaRepo.saveAll(detalles);

        return toResponseDTO(savedVenta);
    }

    @Transactional
    public VentaResponseDTO update(Integer id, VentaCreateDTO dto) {
        Venta venta = ventaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        ClienteLocal cliente = clienteLocalRepo.findById(dto.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Cliente no existe"));

        // 1. Update fields básicos
        venta.setFechaVenta(dto.getFechaVenta());
        venta.setPersonaId(dto.getPersonaId());
        venta.setNombreCliente(cliente.getNombreCompleto());
        venta.setDocumentoCliente(cliente.getDocumento());
        venta.setIgv(dto.getIgv());
        venta.setMetodoPago(dto.getMetodoPago());
        venta.setTotal(dto.getTotal());

        // 2. Eliminar detalles viejos (y restaurar stock)
        List<DetalleVenta> antiguos = detalleVentaRepo.findAll().stream()
                .filter(dv -> dv.getVenta().getVentaId().equals(venta.getVentaId()) && dv.getEstado()).toList();
        for (DetalleVenta det : antiguos) {
            Producto p = det.getProducto();
            p.setStock(p.getStock() + det.getCant());
            productoRepo.save(p);
            det.setEstado(false);
        }
        detalleVentaRepo.saveAll(antiguos);

        // 3. Crear nuevos detalles (y descontar stock)
        List<DetalleVenta> nuevos = new ArrayList<>();
        for (VentaCreateDTO.DetalleVentaDTO det : dto.getDetalles()) {
            Producto producto = productoRepo.findById(det.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto " + det.getProductoId() + " no existe"));
            if (!producto.getEstado() || producto.getStock() < det.getCant()) {
                throw new RuntimeException("Producto sin stock suficiente: " + producto.getProductoId());
            }
            producto.setStock(producto.getStock() - det.getCant());
            productoRepo.save(producto);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setVenta(venta);
            detalle.setCant(det.getCant());
            detalle.setEstado(true);
            nuevos.add(detalle);
        }
        detalleVentaRepo.saveAll(nuevos);

        ventaRepo.save(venta);

        return toResponseDTO(venta);
    }

    @Transactional
    public void delete(Integer id) {
        Venta venta = ventaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        venta.setEstado(false);
        ventaRepo.save(venta);

        // Restaurar stock de productos vendidos
        List<DetalleVenta> detalles = detalleVentaRepo.findAll().stream()
                .filter(dv -> dv.getVenta().getVentaId().equals(id) && dv.getEstado()).toList();
        for (DetalleVenta det : detalles) {
            Producto p = det.getProducto();
            p.setStock(p.getStock() + det.getCant());
            productoRepo.save(p);
            det.setEstado(false);
        }
        detalleVentaRepo.saveAll(detalles);
    }

    // -------------------------
    // Mapper: Venta → VentaResponseDTO
    // -------------------------
    private VentaResponseDTO toResponseDTO(Venta venta) {
        List<DetalleVenta> detalles = detalleVentaRepo.findAll().stream()
                .filter(dv -> dv.getVenta().getVentaId().equals(venta.getVentaId()) && dv.getEstado())
                .toList();
        List<VentaResponseDTO.DetalleVentaResponseDTO> detallesDTO = detalles.stream().map(det -> {
            return new VentaResponseDTO.DetalleVentaResponseDTO(
                    det.getDetalleVentaId(),
                    det.getProducto().getProductoId(),
                    det.getProducto().getNombre(),
                    det.getCant()
            );
        }).collect(Collectors.toList());
        return new VentaResponseDTO(
                venta.getVentaId(),
                venta.getFechaVenta(),
                venta.getPersonaId(),
                venta.getNombreCliente(),
                venta.getDocumentoCliente(),
                venta.getIgv(),
                venta.getMetodoPago(),
                venta.getTotal(),
                venta.getEstado(),
                detallesDTO
        );
    }
}
