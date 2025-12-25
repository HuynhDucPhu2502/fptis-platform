-- =============================================
-- TỪ KHÓA
-- =============================================

-- ACT: Activiti (tên BPM engine gốc, giữ lại để tương thích)
-- ACT_GE: General – dữ liệu dùng chung (binary, properties)
-- ACT_RE: Repository – process definition, deployment
-- ACT_RU: Runtime – các process instance đang chạy
-- ACT_HI: History – log, audit
-- ACT_ID: Identity – user, group

-- =============================================
-- BẢNG LIÊN QUAN PROCESS
-- =============================================

-- Thông tin định nghĩa process (metadata)
SELECT * FROM ACT_RE_PROCDEF;

-- Thông tin deployment
SELECT * FROM ACT_RE_DEPLOYMENT;

-- Dữ liệu nhị phân của deployment
SELECT * FROM ACT_GE_BYTEARRAY;

-- =============================================
-- DROP TẤT CẢ TABLE THUỘC VỀ CAMUNDA
-- =============================================
DO $$
DECLARE
r RECORD;
BEGIN
FOR r IN (
        SELECT tablename
        FROM pg_tables
        WHERE schemaname = 'public'
        AND (tablename LIKE 'act_re_%'
             OR tablename LIKE 'act_ru_%'
             OR tablename LIKE 'act_hi_%'
             OR tablename LIKE 'act_ge_%'
             OR tablename LIKE 'act_id_%')
    ) LOOP
        RAISE NOTICE 'Dropping table: %', r.tablename;
EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
END LOOP;

    RAISE NOTICE 'Camunda tables cleanup completed.';
END $$;



