INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER') ON CONFLICT (id) DO NOTHING;
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN') ON CONFLICT (id) DO NOTHING;
INSERT INTO users (user_id, username, password, role_id, created_at, updated_at)
VALUES (1, 'tanaka', '$2a$10$batyeg9e2J8MjCnoxGeqkOi3w.clZy4Y.30zHso26J13EISCrdlja', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO users (user_id, username, password, role_id, created_at, updated_at)
VALUES (2, 'suzuki', '$2a$10$mCofEEgeLRbEDndbGtQg8OTaT8A/7B5Xial6wq9gn6muLMI48luSi', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO insurance_products (product_id, product_name, product_description, created_at, updated_at)
VALUES (3, '医療保険', '入院・手術を保障する医療保険です。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (product_id) DO NOTHING;

INSERT INTO insurance_products (product_id, product_name, product_description, created_at, updated_at)
VALUES (1, 'がん保険', 'がんと診断されたら一時金をお支払いします。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (product_id) DO NOTHING;

INSERT INTO insurance_products (product_id, product_name, product_description, created_at, updated_at)
VALUES (2, '終身保険', '一生涯保障が続く貯蓄型保険です。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (product_id) DO NOTHING;


INSERT INTO medical_insurance_details (medical_insurance_detail_id, product_id, min_age, max_age, premium_male, premium_female, daily_hospitalization_benefit, payment_days, created_at, updated_at) VALUES
(2, 3, 0, 9, 1000, 1000, 5000, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 10, 19, 1100, 1100, 5000, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 3, 20, 29, 1200, 1200, 5000, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 3, 30, 39, 1300, 1300, 5000, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 3, 40, 49, 1400, 1400, 5000, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 3, 50, 59, 1900, 1600, 5000, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 3, 60, 69, 3000, 2500, 5000, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 3, 70, 79, 4500, 3500, 5000, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (medical_insurance_detail_id) DO NOTHING;

INSERT INTO cancer_insurance_details (
    cancer_insurance_detail_id, product_id, min_age, max_age,
    payment_monthly_male, payment_monthly_female,
    benefit_amount, payment_frequency, created_at, updated_at,
    premium_male, premium_female 
) VALUES
(2, 1, 0, 9, 600, 700, 500000, '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 600, 700), 
(3, 1, 10, 19, 700, 800, 500000, '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 700, 800),
(4, 1, 20, 29, 1000, 1300, 1000000, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1000, 1300),
(5, 1, 30, 39, 1400, 1600, 1000000, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1400, 1600),
(6, 1, 40, 49, 2000, 1800, 1000000, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2000, 1800),
(7, 1, 50, 59, 2900, 2300, 1000000, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2900, 2300),
(8, 1, 60, 69, 4000, 3000, 1000000, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4000, 3000),
(9, 1, 70, 79, 5500, 4000, 1000000, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5500, 4000)
ON CONFLICT (cancer_insurance_detail_id) DO NOTHING;

INSERT INTO whole_life_insurance_details (
    whole_life_insurance_detail_id, product_id, min_age, max_age,
    payment_monthly_male, payment_monthly_female,
    benefit_amount, created_at, updated_at,
    premium_male, premium_female 
) VALUES
(2, 2, 0, 9, 2000, 2000, '200', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2000, 2000),
(3, 2, 10, 19, 2000, 1800, '200', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2000, 1800),
(4, 2, 20, 29, 2500, 2000, '200', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2500, 2000),
(5, 2, 30, 39, 3000, 2500, '200', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3000, 2500),
(6, 2, 40, 49, 4000, 3500, '200', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4000, 3500),
(7, 2, 50, 59, 6300, 4700, '200', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6300, 4700),
(8, 2, 60, 69, 9000, 7000, '200', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9000, 7000),
(9, 2, 70, 79, 18000, 13000, '200', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 18000, 13000)
ON CONFLICT (whole_life_insurance_detail_id) DO NOTHING;