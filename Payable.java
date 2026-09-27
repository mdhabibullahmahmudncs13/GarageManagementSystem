/*
 * ============================================================================
 * PAYABLE INTERFACE
 * Pillar 1: ABSTRACTION - Defines a contract without implementation
 * ============================================================================
 */
public interface Payable {
    double calculateFee();
    String getPaymentMethod();
}
