DELETE FROM attendances WHERE profile_id = 11;

-- CASE 1: Chỉ chèn 3 buổi (DMN yêu cầu >= 5) [TỪ CHỐI]
INSERT INTO attendances (date, time_in, time_out, check_in_status, check_out_status, profile_id, created_at, updated_at)
VALUES 
('2025-12-01', '08:30:00', '17:30:00', 'CHECKED_IN_ON_TIME', 'CHECKED_OUT_ON_TIME', 11, NOW(), NOW()),
('2025-12-02', '08:30:00', '17:30:00', 'CHECKED_IN_ON_TIME', 'CHECKED_OUT_ON_TIME', 11, NOW(), NOW()),
('2025-12-03', '08:30:00', '17:30:00', 'CHECKED_IN_ON_TIME', 'CHECKED_OUT_ON_TIME', 11, NOW(), NOW());

-- CASE 2: Chèn 6 buổi, trong đó 4 buổi ra sớm (Ratio = 4/6 = 0.66 > 0.5) [TỪ CHỐI]
INSERT INTO attendances (date, time_in, time_out, check_in_status, check_out_status, profile_id, created_at, updated_at)
VALUES 
('2025-12-01', '08:30:00', '15:00:00', 'CHECKED_IN_ON_TIME', 'CHECKED_OUT_EARLY', 11, NOW(), NOW()),
('2025-12-02', '08:30:00', '15:00:00', 'CHECKED_IN_ON_TIME', 'CHECKED_OUT_EARLY', 11, NOW(), NOW()),
('2025-12-03', '08:30:00', '15:00:00', 'CHECKED_IN_ON_TIME', 'CHECKED_OUT_EARLY', 11, NOW(), NOW()),
('2025-12-04', '08:30:00', '15:00:00', 'CHECKED_IN_ON_TIME', 'CHECKED_OUT_EARLY', 11, NOW(), NOW()),
('2025-12-05', '08:30:00', '17:30:00', 'CHECKED_IN_ON_TIME', 'CHECKED_OUT_ON_TIME', 11, NOW(), NOW()),
('2025-12-06', '08:30:00', '17:30:00', 'CHECKED_IN_ON_TIME', 'CHECKED_OUT_ON_TIME', 11, NOW(), NOW());

-- CASE 3: Chèn 10 buổi, 7 buổi đúng giờ, 3 buổi trễ (onTime = 0.7, earlyOut = 0) [ĐỦ ĐIỀU KIỆN]
INSERT INTO attendances (date, time_in, time_out, check_in_status, check_out_status, profile_id, created_at, updated_at)
SELECT 
    DATE '2025-12-01' + gs, 
    CASE WHEN gs < 7 THEN TIME '08:30:00' ELSE TIME '09:30:00' END,
    '17:30:00',
    CASE WHEN gs < 7 THEN 'CHECKED_IN_ON_TIME' ELSE 'CHECKED_IN_LATE' END,
    'CHECKED_OUT_ON_TIME', 11, NOW(), NOW()
FROM generate_series(0, 9) AS gs;

-- CASE 4: Chèn 25 buổi, 13 buổi đúng giờ, 12 buổi trễ (onTime = 0.52 > 0.5) [ĐỦ ĐIỀU KIỆN]
INSERT INTO attendances (date, time_in, time_out, check_in_status, check_out_status, profile_id, created_at, updated_at)
SELECT 
    DATE '2025-11-01' + gs, 
    CASE WHEN gs < 13 THEN TIME '08:30:00' ELSE TIME '09:30:00' END,
    '17:30:00',
    CASE WHEN gs < 13 THEN 'CHECKED_IN_ON_TIME' ELSE 'CHECKED_IN_LATE' END,
    'CHECKED_OUT_ON_TIME', 11, NOW(), NOW()
FROM generate_series(0, 24) AS gs;

-- CASE 5: Chèn 10 buổi, nhưng chỉ có 2 buổi đúng giờ (onTime = 0.2 < 0.6) [TỪ CHỐI]
INSERT INTO attendances (date, time_in, time_out, check_in_status, check_out_status, profile_id, created_at, updated_at)
SELECT 
    DATE '2025-12-01' + gs, 
    CASE WHEN gs < 2 THEN TIME '08:30:00' ELSE TIME '09:30:00' END,
    '17:30:00',
    CASE WHEN gs < 2 THEN 'CHECKED_IN_ON_TIME' ELSE 'CHECKED_IN_LATE' END,
    'CHECKED_OUT_ON_TIME', 11, NOW(), NOW()
FROM generate_series(0, 9) AS gs;