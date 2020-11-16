create table user(
    id int NOT NULL AUTO_INCREMENT,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    phone_no varchar(13) NOT NULL,
    password varchar(1000) NOT NULL,
    status varchar(14) NOT NULL,
    role varchar(100) NOT NULL,
    home_or_shop_no varchar(20),
    building_or_shop_name varchar(20),
    landmark varchar(20),
    city varchar(20),
    state varchar(20),
    pincode varchar(20),
    PRIMARY KEY (id)
);

create table verification_token(
  id int NOT NULL AUTO_INCREMENT,
  token varchar(1000) NOT NULL,
  user_id int NOT NULL,
  created_date DATE NOT NULL,
  expiry_date DATE NOT NULL,
  PRIMARY KEY (id),
  foreign key (user_id) references user(id)
);

create table category(
id int NOT NULL AUTO_INCREMENT,
name varchar(100) NOT NULL,
PRIMARY KEY (id)
);

create table product(
id int NOT NULL AUTO_INCREMENT,
category_id int NOT NULL,
name varchar(100) NOT NULL,
price int NOT NULL,
discount int,
brand varchar(100) NOT NULL,
image varchar(255) NOT NULL,
PRIMARY KEY (id),
foreign key (category_id) references category(id)
);

create table inventory(
id int NOT NULL AUTO_INCREMENT,
product_id int NOT NULL,
inhouseqty int NOT NULL,
size varchar(100) NOT NULL,
PRIMARY KEY (id),
foreign key (product_id) references product(id)
);

create table cart(
id int NOT NULL AUTO_INCREMENT,
customer_id int NOT NULL,
total_price int NOT NULL,
PRIMARY KEY (id),
foreign key (customer_id) references user(id)
);

create table ProductsInCart(
id int NOT NULL AUTO_INCREMENT,
cart_id int NOT NULL,
product_id int NOT NULL,
size varchar(255) NOT NULL,
quantity int NOT NULL,
PRIMARY KEY (id),
foreign key (cart_id) references cart(id),
foreign key (product_id) references product(id)
);

create table ProductsBoughtByCustomer(
id int NOT NULL AUTO_INCREMENT,
product_id int NOT NULL,
customer_id int NOT NULL,
quantity int NOT NULL,
size varchar(10) NOT NULL,
PRIMARY KEY (id),
foreign key (product_id) references product(id),
foreign key (customer_id) references user(id)
);

create table feedback(
id int NOT NULL AUTO_INCREMENT,
feedback varchar(255) NOT NULL,
customer_id int NOT NULL,
PRIMARY KEY (id),
foreign key (customer_id) references user(id)
);

create table vendorProposal(
id int NOT NULL AUTO_INCREMENT,
vendor_id int NOT NULL,
product_name varchar(255) NOT NULL,
product_brand varchar(255) NOT NULL,
available_sizes varchar(255) NOT NULL,
price int NOT NULL,
PRIMARY KEY (id),
foreign key (vendor_id) references user(id)
);

create table vendorShopAddress(
id int NOT NULL AUTO_INCREMENT,
vendor_id int NOT NULL,
shop_name varchar(255) NOT NULL,
shop_no varchar(255) NOT NULL,
building_name varchar(255) NOT NULL,
landmark varchar(255) NOT NULL,
city varchar(255) NOT NULL,
state varchar(255) NOT NULL,
pincode varchar(255) NOT NULL,
PRIMARY KEY (id),
foreign key (vendor_id) references user(id)
);

create table orders(
id int NOT NULL AUTO_INCREMENT,
time_of_purchase varchar(255) NOT NULL,
total_amount int NOT NULL,
customer_id int NOT NULL,
recieved_from_store varchar(255) NOT NULL,
PRIMARY KEY (id),
foreign key (customer_id) references user(id)
);

create table faq(
id int NOT NULL AUTO_INCREMENT,
ques varchar (255),
ans varchar (255),
PRIMARY KEY (id)
);