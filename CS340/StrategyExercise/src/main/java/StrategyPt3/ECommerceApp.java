package StrategyPt3;

public class ECommerceApp {
    SalesTaxCalculator taxCalculator;
    PaymentMethod payMethod;
    ShippingMethod shipMethod;

    public ECommerceApp(SalesTaxCalculator taxCalculator, PaymentMethod payMethod, ShippingMethod shipMethod) {
        this.taxCalculator = taxCalculator;
        this.payMethod = payMethod;
        this.shipMethod = shipMethod;
    }

    public void processOrder(Object order) {
        double tax = taxCalculator.calculateTax();
        payMethod.pay();
        shipMethod.processShipping();
    }
}
