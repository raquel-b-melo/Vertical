
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id INT NOT NULL,
    purchase_date DATE NOT NULL,
    total DECIMAL(19, 2) NOT NULL,
    user_id_fk UUID NOT NULL,
    CONSTRAINT fk_user_order
        FOREIGN KEY(user_id_fk)
        REFERENCES users(id)
        ON DELETE CASCADE
);


CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id INT NOT NULL,
    product_value DECIMAL(12, 2) NOT NULL,
    order_id_fk UUID NOT NULL,
    CONSTRAINT fk_order_product
        FOREIGN KEY(order_id_fk)
        REFERENCES orders(id)
        ON DELETE CASCADE
);

-- Índices para otimizar as buscas
CREATE INDEX idx_orders_user_id_fk ON orders(user_id_fk); -- buscar pedidos de um usuário
CREATE INDEX idx_orders_order_id ON orders(order_id); -- buscar por ID do pedido
CREATE INDEX idx_orders_purchase_date ON orders(purchase_date); -- buscar por intervalo de data de compra