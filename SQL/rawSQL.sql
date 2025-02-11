CREATE TABLE users
(
    id        VARCHAR(255) PRIMARY KEY NOT NULL,
    firstname VARCHAR(50),
    lastname  VARCHAR(50),
    username  VARCHAR(50)              NOT NULL,
    email     VARCHAR(255),
    phone     VARCHAR(20),
    password  VARCHAR(255)             NOT NULL
);

CREATE TABLE roles
(
    id   VARCHAR(255) PRIMARY KEY NOT NULL,
    name VARCHAR(50)              NOT NULL
);

CREATE TABLE user_roles
(
    user_id VARCHAR(255) NOT NULL,
    role_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE categories
(
    id            VARCHAR(255) PRIMARY KEY NOT NULL,
    category_name VARCHAR(255)
);

CREATE TABLE products
(
    id          VARCHAR(255) PRIMARY KEY NOT NULL,
    name        VARCHAR(200),
    unit_price  DECIMAL,
    description VARCHAR(200),
    image_url   VARCHAR(255)
);

CREATE TABLE product_categories
(
    product_id  VARCHAR(255) NOT NULL,
    category_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE addresses
(
    id       VARCHAR(255) PRIMARY KEY NOT NULL,
    details  VARCHAR(255),
    zip_code VARCHAR(20)
);

CREATE TABLE user_addresses
(
    user_id    VARCHAR(255) NOT NULL,
    address_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, address_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (address_id) REFERENCES addresses (id)
);

CREATE TABLE cart_item
(
    id         VARCHAR(255) PRIMARY KEY NOT NULL,
    product_id VARCHAR(255),
    quantity   INT,
    date       DATETIME,
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE user_cart
(
    user_id      VARCHAR(255) NOT NULL,
    cart_item_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, cart_item_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (cart_item_id) REFERENCES cart_item (id)
);

CREATE TABLE orders
(
    id                  VARCHAR(255) PRIMARY KEY NOT NULL,
    user_id             VARCHAR(255),
    total_amount        DECIMAL(10, 2),
    order_date          DATETIME,
    status              VARCHAR(50),
    delivery_address_id VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (delivery_address_id) REFERENCES addresses (id)
);

CREATE TABLE order_items
(
    id         VARCHAR(255) PRIMARY KEY,
    order_id   VARCHAR(255),
    product_id VARCHAR(255),
    quantity   INT,
    unit_price DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);
