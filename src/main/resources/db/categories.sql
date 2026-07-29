INSERT INTO categories (name, description) VALUES
('Electronics', 'Devices, gadgets, and electronic accessories for everyday use.'),
('Computers', 'Laptops, desktops, computer components, and peripherals.'),
('Smartphones', 'Mobile phones, accessories, and wearable technology.'),
('Home & Kitchen', 'Furniture, appliances, cookware, and home improvement products.'),
('Fashion', 'Clothing, shoes, and accessories for men, women, and children.'),
('Beauty & Personal Care', 'Cosmetics, skincare, haircare, and personal hygiene products.'),
('Sports & Outdoors', 'Equipment, apparel, and accessories for sports and outdoor activities.'),
('Books', 'Printed books, eBooks, educational materials, and audiobooks.'),
('Toys & Games', 'Toys, board games, puzzles, and entertainment products for all ages.'),
('Automotive', 'Car parts, accessories, maintenance tools, and vehicle care products.'),
('Health', 'Health products, wellness items, and everyday medical supplies.'),
('Pet Supplies', 'Food, toys, accessories, and healthcare products for pets.'),
('Office Supplies', 'Office furniture, stationery, printers, and business essentials.'),
('Garden & Outdoor', 'Gardening tools, outdoor furniture, plants, and landscaping products.'),
('Groceries', 'Food, beverages, snacks, and household consumable products.')
ON CONFLICT (name) DO NOTHING;