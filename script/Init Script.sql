SELECT * FROM profiles
SELECT * FROM users
SELECT * FROM daily_logs
select * from attendances

truncate daily_logs
truncate attendances





INSERT INTO daily_logs (
    main_task,
    result,
    work_date,
    start_time,
    end_time,
    location,
    profile_id,
    created_at,
    updated_at
) VALUES
('Tìm hiểu tổng quan hệ thống FPTIS Platform', 
 'Đã nắm được kiến trúc tổng thể và các module chính', 
 '2025-11-01', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Cài đặt môi trường phát triển Spring Boot', 
 'Chạy thành công project backend trên local', 
 '2025-11-02', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Phân tích module authentication và authorization', 
 'Hiểu luồng login, JWT và phân quyền', 
 '2025-11-03', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Thiết kế entity User, Role, Permission', 
 'Hoàn thiện mapping JPA và quan hệ many-to-many', 
 '2025-11-04', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Viết script SQL khởi tạo role và permission', 
 'Insert thành công ADMIN, MEMBER và các permission', 
 '2025-11-05', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Tích hợp JwtAuthenticationConverter tùy chỉnh', 
 'Map đúng role và permission từ JWT', 
 '2025-11-06', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Fix lỗi ConcurrentModificationException trong Hibernate', 
 'Loại bỏ @Data, override equals/hashCode đúng chuẩn', 
 '2025-11-07', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Xây dựng API tạo Daily Log', 
 'API hoạt động đúng, validate dữ liệu ổn định', 
 '2025-11-08', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Xây dựng API xem danh sách Daily Log', 
 'Phân trang và filter theo ngày làm việc', 
 '2025-11-09', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Áp dụng phân quyền cho API Daily Log', 
 'Chỉ role phù hợp mới truy cập được API', 
 '2025-11-10', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Viết unit test cho service Daily Log', 
 'Test pass, coverage đạt yêu cầu', 
 '2025-11-11', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Refactor code theo clean architecture', 
 'Code dễ đọc, tách rõ layer controller/service/repository', 
 '2025-11-12', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Viết tài liệu API cho module Daily Log', 
 'Hoàn thành mô tả endpoint và request/response', 
 '2025-11-13', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Hỗ trợ fix bug cho module authentication', 
 'Khắc phục lỗi phân quyền sai', 
 '2025-11-14', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Review code cùng mentor', 
 'Nhận góp ý và cải thiện chất lượng code', 
 '2025-11-15', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Tối ưu query JPA cho Daily Log', 
 'Giảm số lượng query, cải thiện hiệu năng', 
 '2025-11-16', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Chuẩn bị demo chức năng Daily Log', 
 'Demo thành công cho team', 
 '2025-11-17', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Fix bug sau demo', 
 'Sửa các lỗi nhỏ được feedback', 
 '2025-11-18', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Hoàn thiện module Daily Log', 
 'Đóng task, sẵn sàng deploy', 
 '2025-11-19', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW()),

('Tổng kết quá trình thực tập', 
 'Rút ra kinh nghiệm và bài học kỹ thuật', 
 '2025-11-20', '08:30', '17:30',
 'Chi nhánh TP.HCM - Lô B3, Đường Sáng Tạo, Khu E-Office, Khu chế xuất Tân Thuận, Phường Tân Thuận, Thành phố Hồ Chí Minh, Việt Nam',
 11, NOW(), NOW());


 INSERT INTO daily_logs (
    main_task,
    result,
    work_date,
    start_time,
    end_time,
    location,
    profile_id,
    created_at,
    updated_at
)
SELECT
    'Task #' || gs,
    'Result #' || gs,
    DATE '2025-01-01' + (gs % 365),
    TIME '08:30',
    TIME '17:30',
    'FPT IS HCM',
    11,
    NOW(),
    NOW()
FROM generate_series(1, 90000) AS gs;


