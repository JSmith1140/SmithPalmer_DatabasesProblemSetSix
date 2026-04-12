/* Database Principles
    Matthew Palmer, Jacob Smith
*/

/*This SQL Script is for the Dorm Dash Application, which is an application that is used for getting snacks and school supplies delivered and is intended for use by you, employees, and customers.*/
SET SQL_SAFE_UPDATES = 0;
DROP DATABASE IF EXISTS ProblemSetSix;
CREATE DATABASE ProblemSetSix;
USE ProblemSetSix;

-- Drop tables in reverse dependency order
DROP TABLE IF EXISTS delivery_items CASCADE;
DROP TABLE IF EXISTS deliveries CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS service_orders CASCADE;
DROP TABLE IF EXISTS services CASCADE;
DROP TABLE IF EXISTS inventory_items CASCADE;
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS dorms CASCADE;
DROP TABLE IF EXISTS campus_edges CASCADE;

-- Dorms
CREATE TABLE dorms (
    dorm_id INT AUTO_INCREMENT PRIMARY KEY,
    dorm_name VARCHAR(100) NOT NULL UNIQUE
);

-- Rooms
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    dorm_id INT NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    CONSTRAINT fk_rooms_dorm
        FOREIGN KEY (dorm_id)
        REFERENCES dorms(dorm_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT unique_room UNIQUE (dorm_id, room_number)
);

-- Students
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    room_id INT NOT NULL,
    CONSTRAINT fk_students_room
        FOREIGN KEY (room_id)
        REFERENCES rooms(room_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Employees
CREATE TABLE employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    delivery_start_time TIME NOT NULL,
    delivery_end_time TIME NOT NULL,
    CONSTRAINT check_delivery_window
        CHECK (delivery_end_time > delivery_start_time)
);

-- Inventory
CREATE TABLE inventory_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(8,2) NOT NULL CHECK (price >= 0),
    quantity_available INT NOT NULL CHECK (quantity_available >= 0)
);

-- Orders (one per student per day)
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    order_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_orders_student
        FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT unique_student_order_per_day
        UNIQUE (student_id, order_date)
);

-- Order Items (junction table)
CREATE TABLE order_items (
    order_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price_at_purchase DECIMAL(8,2) NOT NULL CHECK (price_at_purchase >= 0),
    PRIMARY KEY (order_id, item_id),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(order_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_order_items_item
        FOREIGN KEY (item_id)
        REFERENCES inventory_items(item_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Services (Dunks, Library, etc.)
CREATE TABLE services (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    service_type VARCHAR(50) NOT NULL,
    description TEXT
);

-- Service Orders
CREATE TABLE service_orders (
    service_order_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    service_id INT NOT NULL,
    order_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_service_orders_student
        FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_service_orders_service
        FOREIGN KEY (service_id)
        REFERENCES services(service_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Deliveries
CREATE TABLE deliveries (
    delivery_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL UNIQUE,
    employee_id INT NOT NULL,
    estimated_delivery_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_deliveries_order
        FOREIGN KEY (order_id)
        REFERENCES orders(order_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_deliveries_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(employee_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Campus Graph (Edges)
CREATE TABLE campus_edges (
    edge_id INT AUTO_INCREMENT PRIMARY KEY,
    from_dorm_id INT NOT NULL,
    to_dorm_id INT NOT NULL,
    travel_time INT NOT NULL CHECK (travel_time > 0),
    CONSTRAINT fk_edges_from
        FOREIGN KEY (from_dorm_id)
        REFERENCES dorms(dorm_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_edges_to
        FOREIGN KEY (to_dorm_id)
        REFERENCES dorms(dorm_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT unique_edge UNIQUE (from_dorm_id, to_dorm_id)
);

-- Delivery Items (for reporting what was delivered)
CREATE TABLE delivery_items (
    delivery_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (delivery_id, item_id),
    CONSTRAINT fk_delivery_items_delivery
        FOREIGN KEY (delivery_id)
        REFERENCES deliveries(delivery_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_delivery_items_item
        FOREIGN KEY (item_id)
        REFERENCES inventory_items(item_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =========================
-- Dorms
-- =========================
INSERT INTO dorms (dorm_id, dorm_name) VALUES
(1, 'Deegan Hall East'),
(2, "O'Brien Hall"),
(3, 'Ash Centre');

-- =========================
-- Rooms
-- =========================
INSERT INTO rooms (room_id, dorm_id, room_number) VALUES
(1, 1, '203A'),
(2, 1, '302A'),
(3, 2, '304A'),
(4, 3, '306B');

-- =========================
-- Students
-- =========================
INSERT INTO students (student_id, student_name, phone, email, room_id) VALUES
(1, 'Nicole Jefferson', '555-000-0001', 'njefferson@merrimack.edu', 1),
(2, 'Bob Samson', '555-000-0002', 'bsamson@merrimack.edu', 1),
(3, 'Dylan Bluford', '555-000-0003', 'dbluford@merrimack.edu', 2),
(4, 'Jessie Vince', '555-000-0004', 'jvince@merrimack.edu', 3),
(5, 'Samuel Nelson', '555-000-0005', 'snelson@merrimack.edu', 4);

-- =========================
-- Employees
-- =========================
INSERT INTO employees (employee_id, employee_name, phone, email, delivery_start_time, delivery_end_time) VALUES
(1, 'Matthew Palmer', '555-000-0006', 'palmerm@dormdash.com', '15:00', '17:00'),
(2, 'Jacob Smith', '555-000-0007', 'jsmith@dormdash.com', '16:00', '18:00');

-- =========================
-- Inventory Items
-- =========================
INSERT INTO inventory_items (item_id, item_name, category, price, quantity_available) VALUES
(1, "Lay's Classic Potato Chips", 'Snack', 6.19, 100),
(2, 'Quaker Chewy Chocolate Chip Granola Bar', 'Snack', 3.19, 150),
(3, 'Poland Spring Natural Spring Water', 'Beverage', 7.49, 200),
(4, 'Staples Composition Notebook', 'School Supply', 2.55, 75),
(5, 'Up&Up #2 Wood Pencils', 'School Supply', 2.59, 50);

-- =========================
-- Services
-- =========================
INSERT INTO services (service_id, service_type, description) VALUES
(1, 'Dunks Pickup', 'Pickup order from Dunkin'),
(2, 'Library Delivery', 'Deliver books from library'),
(3, 'Library Return', 'Return books to library');

-- =========================
-- Orders
-- =========================
INSERT INTO orders (order_id, student_id, order_date, status) VALUES
(1, 1, '2026-04-10', 'Placed'),
(2, 2, '2026-04-10', 'Placed'),
(3, 3, '2026-04-10', 'Placed'),
(4, 4, '2026-04-10', 'Placed');

-- =========================
-- Order Items
-- =========================
INSERT INTO order_items (order_id, item_id, quantity, price_at_purchase) VALUES
(1, 1, 2, 12.38),
(1, 3, 3, 22.47),
(2, 2, 5, 15.95),
(3, 4, 1, 2.55),
(3, 5, 1, 2.59),
(4, 1, 1, 6.19),
(4, 2, 2, 6.38);

-- =========================
-- Service Orders
-- =========================
INSERT INTO service_orders (service_order_id, student_id, service_id, order_date, status) VALUES
(1, 1, 1, '2026-04-11', 'Scheduled'),
(2, 2, 2, '2026-01-14', 'Completed'),
(3, 3, 3, '2026-05-01', 'Scheduled');

-- =========================
-- Deliveries
-- =========================
INSERT INTO deliveries (delivery_id, order_id, employee_id, estimated_delivery_time) VALUES
(1, 1, 1, '2026-04-10 16:30:00'),
(2, 2, 1, '2026-04-10 17:00:00'),
(3, 3, 2, '2026-04-10 17:30:00'),
(4, 4, 2, '2026-04-10 18:00:00');

-- =========================
-- Delivery Items
-- =========================
INSERT INTO delivery_items (delivery_id, item_id, quantity) VALUES
(1, 1, 2),
(1, 3, 3),
(2, 2, 5),
(3, 4, 1),
(3, 5, 1),
(4, 1, 1),
(4, 2, 2);

-- =========================
-- Campus Graph (Edges)
-- =========================
INSERT INTO campus_edges (edge_id, from_dorm_id, to_dorm_id, travel_time) VALUES
(1, 1, 2, 1),
(2, 2, 3, 5),
(3, 1, 3, 2),
(4, 2, 1, 1),
(5, 3, 2, 5),
(6, 3, 1, 2);

DELIMITER //

CREATE PROCEDURE add_or_update_inventory(
    IN p_name VARCHAR(100),
    IN p_category VARCHAR(50),
    IN p_quantity INT,
    IN p_price DECIMAL(8,2)
)
BEGIN
    IF EXISTS (SELECT 1 FROM inventory_items WHERE item_name = p_name) THEN
        UPDATE inventory_items
        SET quantity_available = quantity_available + p_quantity,
            price = p_price,
            category = p_category
        WHERE item_name = p_name;
    ELSE
        INSERT INTO inventory_items (item_name, category, price, quantity_available)
        VALUES (p_name, p_category, p_price, p_quantity);
    END IF;
END //

DELIMITER ;

