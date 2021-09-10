CREATE TABLE IF NOT EXISTS product (
                         id bigserial primary key,
                         title varchar(60),
                         price numeric(7,2),
                         category varchar(20),
                         available int
);

CREATE TABLE IF NOT EXISTS shop_employee (
                               id bigserial primary key,
                               email varchar(255),
                               login varchar(25),
                               first_name varchar(25),
                               last_name varchar(25),
                               hash_password varchar(60),
                               role varchar(20)
);

CREATE TABLE IF NOT EXISTS customer (
                          id bigserial primary key,
                          email varchar(255),
                          login varchar(25),
                          hash_password varchar(60),
                          role varchar(20)
);

CREATE TABLE IF NOT EXISTS cart (
                      id bigserial primary key,
                      owner_id bigint references customer(id),
                      total_price numeric(8, 2),
                      active bool
);

CREATE TABLE IF NOT EXISTS cart_item (
                           id bigserial primary key,
                           cart_id bigint references cart(id),
                           product_id bigint references product(id),
                           amount int,
                           price numeric(7, 2)
);
