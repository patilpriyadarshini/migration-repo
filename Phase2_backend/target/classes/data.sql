INSERT INTO app_user (sec_usr_id, sec_usr_fname, sec_usr_lname, sec_usr_pwd, sec_usr_type) VALUES
('ADMIN001', 'John', 'Smith', 'admin123', 'A'),
('USER0001', 'Jane', 'Doe', 'user1234', 'U'),
('USER0002', 'Bob', 'Wilson', 'user5678', 'U');

INSERT INTO customer (cust_id, cust_first_name, cust_last_name, cust_dob_yyyy_mm_dd, cust_ssn, cust_fico_credit_score, cust_phone_num_1, cust_phone_num_2, cust_addr_line_1, cust_addr_line_2, cust_addr_line_3, cust_addr_state_cd, cust_addr_zip, cust_addr_country_cd) VALUES
(1001, 'Alice', 'Johnson', '1985-03-15', 123456789, 750, '555-0101', '555-0102', '123 Main St', 'Apt 4B', 'New York', 'NY', '10001', 'USA'),
(1002, 'Bob', 'Williams', '1978-07-22', 987654321, 680, '555-0201', NULL, '456 Oak Ave', NULL, 'Los Angeles', 'CA', '90210', 'USA'),
(1003, 'Carol', 'Brown', '1992-11-08', 456789123, 720, '555-0301', '555-0302', '789 Pine Rd', 'Suite 100', 'Chicago', 'IL', '60601', 'USA');

INSERT INTO account (acct_id, acct_cust_id, acct_active_status, acct_curr_bal, acct_credit_limit, acct_cash_credit_limit, acct_open_date, acct_expiraion_date, acct_reissue_date, acct_curr_cyc_credit, acct_curr_cyc_debit, acct_addr_zip, acct_group_id) VALUES
(12345678901, 1001, 'Y', 1500.00, 5000.00, 1000.00, '2020-01-15', '2027-01-15', '2023-01-15', 2500.00, 1000.00, '10001', 'GRP001'),
(12345678902, 1002, 'Y', 750.50, 3000.00, 500.00, '2019-06-10', '2027-06-10', '2022-06-10', 1200.00, 450.50, '90210', 'GRP002'),
(12345678903, 1003, 'Y', 0.00, 7500.00, 1500.00, '2021-03-20', '2027-03-20', '2024-03-20', 0.00, 0.00, '60601', 'GRP003');

INSERT INTO card (card_num, card_acct_id, card_embossed_name, card_active_status, card_expiraion_date) VALUES
('4111111111111111', 12345678901, 'Alice Johnson', 'Y', '12/2025'),
('4111111111111112', 12345678902, 'Bob Williams', 'Y', '06/2024'),
('4111111111111113', 12345678903, 'Carol Brown', 'Y', '03/2026'),
('4111111111111114', 12345678901, 'Alice Johnson Business', 'N', '12/2025');

INSERT INTO transaction_type (tran_type, tran_type_desc) VALUES
('01', 'Purchase'),
('02', 'Cash Advance'),
('03', 'Payment'),
('04', 'Fee'),
('05', 'Interest');

INSERT INTO transaction_category (tran_cat_cd, tran_cat_type_desc) VALUES
('01', 'Groceries'),
('02', 'Gas'),
('03', 'Restaurants'),
('04', 'Shopping'),
('05', 'Cash'),
('06', 'Payment'),
('07', 'Fee'),
('08', 'Interest');

INSERT INTO transaction (tran_id, tran_card_num, tran_type_cd, tran_cat_cd, tran_source, tran_desc, tran_amt, tran_orig_ts, tran_proc_ts, tran_merchant_id, tran_merchant_name, tran_merchant_city, tran_merchant_zip) VALUES
('T001', '4111111111111111', '01', 1, 'POS', 'GROCERY STORE PURCHASE', 85.50, '2024-01-15 10:30:00', '2024-01-15 10:30:00', 1001, 'SuperMart Grocery', 'New York', '10001'),
('T002', '4111111111111111', '01', 2, 'POS', 'GAS STATION PURCHASE', 45.00, '2024-01-16 08:15:00', '2024-01-16 08:15:00', 1002, 'QuickFill Gas', 'New York', '10002'),
('T003', '4111111111111112', '01', 3, 'POS', 'RESTAURANT PURCHASE', 67.25, '2024-01-17 19:45:00', '2024-01-17 19:45:00', 1003, 'Fine Dining Restaurant', 'Los Angeles', '90210'),
('T004', '4111111111111113', '03', 6, 'ONLINE', 'PAYMENT RECEIVED', -500.00, '2024-01-18 14:20:00', '2024-01-18 14:20:00', 1004, 'Online Payment System', 'Chicago', '60601');
