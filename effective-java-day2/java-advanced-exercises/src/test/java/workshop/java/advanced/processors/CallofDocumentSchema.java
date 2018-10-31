package workshop.java.advanced.processors;

import workshop.java.advanced.processors.JsonProcessing.Json;

public final class CallofDocumentSchema extends Json<CallofDocumentSchema> {
    public final Created created = new Created("created");

    public final Product products = new Product("products");

    private CallofDocumentSchema(String fieldName) {
        super(fieldName);
    }

    public final class Created extends Json<Created> {
        private Created(String fieldName) {
            super(fieldName);
        }
    }

    public final class Product extends Json<Product> {
        public final RefNo refNo = new RefNo("refNo");

        public final Order order = new Order("order");

        private Product(String fieldName) {
            super(fieldName);
        }

        public final class RefNo extends Json<RefNo> {
            private RefNo(String fieldName) {
                super(fieldName);
            }
        }

        public final class Order extends Json<Order> {
            private Order(String fieldName) {
                super(fieldName);
            }
        }
    }
}
