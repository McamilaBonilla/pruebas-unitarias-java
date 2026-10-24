package co.edu.notas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * ENTREGA DEL TALLER
 * Nombre completo: Maria Camila Bonilla
 * 1. Bug encontrado (método y línea de CarritoCompras.java):
 *    agregarProducto, línea 24: decía "cantidad < 0" y dejaba agregar productos con cantidad 0.
 *    La regla R1 exige cantidad mayor a 0, se corrigió a "cantidad <= 0".
 * 2. Prueba que lo detectó (nombre del método):
 *    agregarProducto_cantidadCero_lanzaExcepcion
 */
class CarritoComprasTest {

    private CarritoCompras carrito;

    @BeforeEach
    void setUp() { carrito = new CarritoCompras(); }

    @Test
    @DisplayName("Subtotal = precio × cantidad de cada producto")
    void calcularSubtotal_variosProductos_sumaCorrecta() {
        // Arrange
        carrito.agregarProducto("Cuaderno", 8_000, 3);
        carrito.agregarProducto("Esfero", 2_000, 5);
        // Act
        double subtotal = carrito.calcularSubtotal();
        // Assert
        assertEquals(34_000, subtotal, 0.001);
    }

    // Parte A: R1 y R2

    @Test
    @DisplayName("Carrito vacío tiene subtotal 0")
    void calcularSubtotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularSubtotal(), 0.001);
    }

    @Test
    @DisplayName("Carrito vacío tiene total 0")
    void calcularTotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Agregar un producto válido aumenta la cantidad de ítems")
    void agregarProducto_productoValido_aumentaItems() {
        carrito.agregarProducto("Regla", 3_000, 1);
        assertEquals(1, carrito.cantidadDeItems());
    }

    @Test
    @DisplayName("Rechaza un producto con nombre vacío")
    void agregarProducto_nombreVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("  ", 1_000, 1));
    }

    @Test
    @DisplayName("Rechaza un producto con nombre nulo")
    void agregarProducto_nombreNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto(null, 1_000, 1));
    }

    @Test
    @DisplayName("Rechaza un producto con precio 0")
    void agregarProducto_precioCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Lápiz", 0, 1));
    }

    @Test
    @DisplayName("Rechaza un producto con precio negativo")
    void agregarProducto_precioNegativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Lápiz", -500, 1));
    }

    @Test
    @DisplayName("Rechaza un producto con cantidad 0")
    void agregarProducto_cantidadCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Lápiz", 1_000, 0));
    }

    @Test
    @DisplayName("Rechaza un producto con cantidad negativa")
    void agregarProducto_cantidadNegativa_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Lápiz", 1_000, -1));
    }

    // Parte B: R3 a R6

    @Test
    @DisplayName("Sin cupón no hay descuento")
    void calcularDescuento_sinCupon_retornaCero() {
        carrito.agregarProducto("Morral", 80_000, 1);
        assertEquals(0, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("SENA10 descuenta el 10 % del subtotal")
    void calcularDescuento_cuponSena10_retornaDiezPorCiento() {
        carrito.agregarProducto("Libro", 100_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(10_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO descuenta $5.000 con subtotal de $50.000")
    void calcularDescuento_bienvenidoSubtotal50000_retorna5000() {
        carrito.agregarProducto("Calculadora", 50_000, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(5_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO no descuenta con subtotal de $49.999")
    void calcularDescuento_bienvenidoSubtotal49999_retornaCero() {
        carrito.agregarProducto("Calculadora", 49_999, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(0, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("Rechaza un cupón inválido")
    void aplicarCupon_cuponInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.aplicarCupon("DESCUENTO50"));
    }

    @Test
    @DisplayName("Total con envío pagado: libro de $100.000 → $131.000")
    void calcularTotal_menorA150000_cobraEnvio() {
        carrito.agregarProducto("Libro", 100_000, 1);
        assertEquals(131_000, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con envío gratis: $200.000 → $238.000")
    void calcularTotal_mayorA150000_envioGratis() {
        carrito.agregarProducto("Tablet", 200_000, 1);
        assertEquals(238_000, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con SENA10: monitor de $500.000 → $535.500")
    void calcularTotal_conCuponSena10_aplicaDescuentoEIva() {
        carrito.agregarProducto("Monitor", 500_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(535_500, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con BIENVENIDO: $50.000 → $65.550")
    void calcularTotal_conCuponBienvenido_aplicaDescuentoIvaYEnvio() {
        carrito.agregarProducto("Calculadora", 50_000, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(65_550, carrito.calcularTotal(), 0.001);
    }
}
