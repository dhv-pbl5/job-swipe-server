CREATE TABLE public.constants
(
    constant_id uuid NOT NULL,
    constant_name character varying(1000) NOT NULL,
    constant_type character varying(1000) NOT NULL UNIQUE,
    note jsonb,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (constant_id)
);

ALTER TABLE IF EXISTS public.constants
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.accounts
(
    account_id uuid NOT NULL,
    account_status boolean NOT NULL DEFAULT TRUE,
    address character varying(1000) NOT NULL,
    avatar character varying(1000),
    email character varying(1000) NOT NULL,
    password character varying(1000) NOT NULL,
    phone_number character varying(1000) NOT NULL,
    refresh_token character varying(1000),
    system_role uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    deleted_at timestamp with time zone,
    PRIMARY KEY (account_id),
    FOREIGN KEY (system_role)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.accounts
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.companies
(
    account_id uuid NOT NULL,
    company_name character varying(1000) NOT NULL,
    company_url character varying(1000) NOT NULL,
    established_date timestamp with time zone NOT NULL,
    description text,
    others jsonb,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (account_id),
    FOREIGN KEY (account_id)
        REFERENCES public.accounts (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.companies
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.users
(
    account_id uuid NOT NULL,
    date_of_birth timestamp with time zone NOT NULL,
    first_name character varying(1000) NOT NULL,
    gender boolean NOT NULL,
    last_name character varying(1000) NOT NULL,
    others jsonb,
    social_media_link character varying(1000)[],
    summary_introduction text,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (account_id),
    FOREIGN KEY (account_id)
        REFERENCES public.accounts (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.users
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.application_positions
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    apply_position uuid NOT NULL,
    salary_range uuid NOT NULL,
    status boolean NOT NULL DEFAULT true,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    note character varying(10000),
    PRIMARY KEY (id),
    FOREIGN KEY (account_id)
        REFERENCES public.accounts (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (apply_position)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (salary_range)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.application_positions
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.application_skills
(
    id uuid NOT NULL,
    application_position_id uuid NOT NULL,
    skill_id uuid NOT NULL,
    note character varying(10000),
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (application_position_id)
        REFERENCES public.application_positions (id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (skill_id)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.application_skills
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.user_educations
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    cpa numeric(100, 10) NOT NULL,
    majority character varying(1000),
    note character varying(1000),
    study_end_time timestamp with time zone,
    study_place character varying(1000) NOT NULL,
    study_start_time timestamp with time zone NOT NULL,
    is_university boolean NOT NULL DEFAULT true,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.user_educations
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.user_experiences
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    experience_end_time timestamp with time zone,
    experience_start_time timestamp with time zone NOT NULL,
    experience_type uuid NOT NULL,
    note character varying(1000),
    position character varying(1000) NOT NULL,
    work_place character varying(1000) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (experience_type)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.user_experiences
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.user_awards
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    certificate_name character varying(1000) NOT NULL,
    certificate_time timestamp with time zone NOT NULL,
    note character varying(1000),
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.user_awards
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.matches
(
    id uuid NOT NULL,
    company_id uuid NOT NULL,
    company_matched boolean,
    matched_time timestamp with time zone NOT NULL,
    user_id uuid NOT NULL,
    user_matched boolean,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id)
        REFERENCES public.companies (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (user_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.matches
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.conversations
(
    id uuid NOT NULL,
    company_id uuid NOT NULL,
    user_id uuid NOT NULL,
    active_status boolean NOT NULL DEFAULT TRUE,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id)
        REFERENCES public.companies (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (user_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.conversations
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.messages
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    content character varying(10000),
    conversation_id uuid NOT NULL,
    read_status boolean NOT NULL DEFAULT FALSE,
    url_file character varying(1000),
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id)
        REFERENCES public.accounts (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (conversation_id)
        REFERENCES public.conversations (id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.messages
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.notifications
(
    id uuid NOT NULL,
    content character varying(1000) NOT NULL,
    notification_type uuid NOT NULL,
    read_status boolean NOT NULL DEFAULT FALSE,
    object_id uuid NOT NULL,
    receiver_id uuid NOT NULL,
    sender_id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (notification_type)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (receiver_id)
        REFERENCES public.accounts (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (sender_id)
        REFERENCES public.accounts (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.notifications
    OWNER to "qh47Qsmu19JJRuMq";

CREATE TABLE public.languages
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    language_id uuid NOT NULL,
    language_score character varying(1000) NOT NULL,
    language_certificate_date timestamp with time zone,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (language_id)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (account_id)
        REFERENCES public.accounts (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.languages
    OWNER to "qh47Qsmu19JJRuMq";

-- CREATE DATA FOR ALL TABLES

-- Constants table
INSERT INTO constants (constant_id, constant_name, constant_type, note, created_at, updated_at) VALUES
('c34fd40c-63e4-46f6-b690-526bc7c8f3aa', 'Admin', '0110000', NULL, '2024-06-15 17:45:55.713543+00', NULL),
('3a977d27-e39a-447c-b407-06df407b7c46', 'User', '0111001', NULL, '2024-06-15 17:45:55.713650+00', NULL),
('27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', 'Company', '0111002', NULL, '2024-06-15 17:45:55.713691+00', NULL),
('f763ac88-a13c-40da-8ec4-ce6ee3d482ce', 'Work', '0400000', NULL, '2024-06-15 17:45:55.713730+00', NULL),
('ef8f6368-f288-4aa5-b4d9-b6ff20d532bc', 'Hobbies & Activities', '0400001', NULL, '2024-06-15 17:45:55.713766+00', NULL),
('772a1cf6-c85d-40fe-a185-f9e015002dec', 'Developer', '0200000', NULL, '2024-06-15 17:45:55.713794+00', NULL),
('c314b5a0-56ac-45e7-af85-716c07a59194', 'Designer', '0200001', NULL, '2024-06-15 17:45:55.713822+00', NULL),
('2acd03e9-254c-4f98-8906-663ecb20b7e3', 'Project Manager', '0200002', NULL, '2024-06-15 17:45:55.713863+00', NULL),
('f146a22d-2293-41f5-a787-b636e4f7dbd2', 'Tester', '0200003', NULL, '2024-06-15 17:45:55.713901+00', NULL),
('2026d8f1-065e-48b9-b52e-f6ba3eda5e24', 'Accountant', '0200004', NULL, '2024-06-15 17:45:55.713930+00', NULL),
('a50018b7-be19-4197-9dd6-37a510c8c377', 'Python', '0300000', NULL, '2024-06-15 17:45:55.713958+00', NULL),
('2e33cb99-e408-42ce-8446-34b2988220ab', 'Java', '0300001', NULL, '2024-06-15 17:45:55.713984+00', NULL),
('5cf45cdd-b4ef-4337-b968-28ae298b7f0f', 'C++', '0300002', NULL, '2024-06-15 17:45:55.714008+00', NULL),
('aa46a5dc-13bd-43b5-a908-0b9a7399c1ea', 'JavaScript', '0300003', NULL, '2024-06-15 17:45:55.714032+00', NULL),
('5e183034-db7a-4bdb-9e2b-0b96a0f51ca1', 'SQL', '0300004', NULL, '2024-06-15 17:45:55.714054+00', NULL),
('4955b694-fc44-4022-8b61-3e8a2c4a9619', 'HTML', '0300005', NULL, '2024-06-15 17:45:55.714075+00', NULL),
('731a3556-fb6d-4534-8f07-071b523e8863', 'CSS', '0300006', NULL, '2024-06-15 17:45:55.714100+00', NULL),
('1c87705a-71fb-44a3-b6a7-3e54ad539f64', 'React', '0300007', NULL, '2024-06-15 17:45:55.714120+00', NULL),
('27087464-75ef-41ae-9392-6119b9e21de3', 'Vue', '0300008', NULL, '2024-06-15 17:45:55.714143+00', NULL),
('677c56f6-784d-4521-ae17-0efc29407074', 'Thông báo cho {sender} về {receiver}', '0500000', NULL, '2024-06-15 17:45:55.714167+00', NULL),
('b06bc63b-c0f0-472d-ba1f-543043b043a8', '{sender} và {receiver} đã kết nối với nhau', '0500001', NULL, '2024-06-15 17:45:55.714189+00', NULL),
('106f8795-d327-4bc8-ae2b-f4f316a7eb6f', '{sender} đã gửi yêu cầu kết nối đến {receiver}', '0500002', NULL, '2024-06-15 17:45:55.714212+00', NULL),
('a1039e3a-53dd-40a0-9f6e-2b864248a6a1', '{sender} đã từ chối yêu cầu kết nối từ {receiver}', '0500003', NULL, '2024-06-15 17:45:55.714255+00', NULL),
('a1dc878e-7078-46fc-9ed0-2d8f9cd6fa22', '{sender} đã tạo cuộc trò chuyện với {receiver}', '0500004', NULL, '2024-06-15 17:45:55.714287+00', NULL),
('49e0187f-6cc1-421e-b181-9892ab59af75', '{sender} vừa gửi một tin nhắn mới đến {receiver}', '0500005', NULL, '2024-06-15 17:45:55.714379+00', NULL),
('d9f7d8fc-f558-4a9a-bd72-61fbaf2cf98b', '{sender} đã đọc tin nhắn từ {receiver}', '0500006', NULL, '2024-06-15 17:45:55.714419+00', NULL),
('ffd3a428-5d57-46ef-b795-cd8c2592da8f', 'Quản trị viên vô hiệu hóa tài khoản', '0500007', NULL, '2024-06-15 17:45:55.714442+00', NULL),
('a0c2155f-313f-4871-a807-cce716c6ea4b', 'Quản trị viên kích hoạt tài khoản', '0500008', NULL, '2024-06-15 17:45:55.714463+00', NULL),
('0bc37020-6220-420c-a98c-effe23234820', '< 8,000,000 VND', '0700000', NULL, '2024-06-15 17:45:55.714486+00', NULL),
('23cd8f2d-8512-41ad-b928-79f7a0297446', '8,000,000 VND ~ 12,000,000 VND', '0700001', NULL, '2024-06-15 17:45:55.714508+00', NULL),
('b10053c8-e1e5-4818-be24-a9eba12b9a52', '12,000,000 VND ~ 25,000,000 VND', '0700002', NULL, '2024-06-15 17:45:55.714531+00', NULL),
('0fea4a6d-6784-40e2-b459-0487baeeee1a', '22,000,000 VND ~ 38,000,000 VND', '0700003', NULL, '2024-06-15 17:45:55.714551+00', NULL),
('6cd341da-2599-46bb-be8a-ee262d944e04', '35,000,000 VND ~ 55,000,000 VND', '0700004', NULL, '2024-06-15 17:45:55.714570+00', NULL),
('9fb4e644-8c5d-4fd9-8f73-087dcd888034', '40,000,000 VND ~ 60,000,000 VND', '0700005', NULL, '2024-06-15 17:45:55.714591+00', NULL),
('e293d215-99e1-493b-b703-6e31e7f1c9ce', '~ 60,000,000+ VND', '0700006', NULL, '2024-06-15 17:45:55.714618+00', NULL),
('f8e34acb-d367-45ba-b059-2c931bb89b78', '~ 70,000,000+ VND', '0700007', NULL, '2024-06-15 17:45:55.714639+00', NULL),
('07faec06-15b7-42a6-b3a7-1c6c3686604c', 'IELTS', '0600000', '{"values": null, "validate": {"max": 9, "min": 2.5, "divisible": 0.5, "required_points": true}}', '2024-06-15 17:45:55.714662+00', NULL),
('535a93c3-6bd7-4bc0-a30a-f438aab32eb0', 'TOEIC', '0600001', '{"values": null, "validate": {"max": 990, "min": 100, "divisible": 5, "required_points": true}}', '2024-06-15 17:45:55.714685+00', NULL),
('bc3f6552-677e-4661-a8f9-25358b2e74bb', 'JLPT', '0600002', '{"values": ["N1", "N2", "N3", "N4", "N5"], "validate": {"max": null, "min": null, "divisible": null, "required_points": null}}', '2024-06-15 17:45:55.714708+00', NULL);

-- Accounts + Companies table
INSERT INTO accounts (account_id, account_status, address, avatar, email, password, phone_number, refresh_token, system_role, created_at, updated_at, deleted_at) VALUES
('ab5457c7-48e8-47dd-a082-ba79624059cc', 'true', 'Đ. Nam Kỳ Khởi Nghĩa, Khu đô thị FPT City, Ngũ Hành Sơn, Đà Nẵng 550000', 'https://daihoc.fpt.edu.vn/wp-content/uploads/2022/05/FPT-Software-da-nang-2-768x489.webp', 'fptdanang@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 935 884 368', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('4024d713-c1bb-4a04-8de5-fbe6f74a07a9', 'true', '216-218 Nguyễn Phước Lan, Hoà Xuân, Cẩm Lệ, Đà Nẵng', 'https://scontent.fdad3-3.fna.fbcdn.net/v/t39.30808-6/298096463_5147543645343572_4193331858075255634_n.jpg?_nc_cat=109&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeErLQB52UBIcI5nM2fBWSvBCwC8ZCFRfzsLALxkIVF_OzN4pMKdaGtGxN0Xj0qXqs1XWeCpS6zk-syXMkbbKs5A&_nc_ohc=vtWXoBzSepoQ7kNvgFC2kf8&_nc_zt=23&_nc_ht=scontent.fdad3-3.fna&oh=00_AYDthAut4yCUH6v5HJ0T9nwzv3JeAWUGgAWssmm8jvxL8Q&oe=66742B36', 'enclave@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 914 100 399', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('e13f71e1-310d-40ff-8ada-b0153a7af374', 'true', '38 Yên Bái, Hải Châu 1, Hải Châu, Đà Nẵng 550000', 'https://www.aptech-danang.edu.vn/_next/image?url=https%3A%2F%2Foffice.softech.vn%2FModules%2FASPNETVN.PORTAL.Modules.CMS%2FUploads%2F41ab44b8-de86-4a38-bc19-45cb688f8b4d%2F359765890-591098813201781-321889156980075478-n.jpg&w=1080&q=75', 'softech@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 (0236) 3 779 777', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('1f01fb26-8141-422d-8dca-f7587f41812c', 'true', '103 Đống Đa, Thạch Thang, Hải Châu, Đà Nẵng 550000', 'https://www.danang43.vn/wp-content/uploads/2022/04/asiasoft1.png', 'asiaSoft@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '0236 3644 723', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('f36a9b52-f3cd-40b6-9632-8c70960c8a93', 'true', '182-184 Nguyễn Tri Phương, Thạc Gián, Thanh Khê, Đà Nẵng 550000', 'https://cdn.bap-software.net/2023/08/MicrosoftTeams-image-8.jpg', 'bap@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '0236 6565 115', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('5c63f82a-8948-473e-9686-0077a2f22042', 'true', 'Đ. 2 Tháng 9, Hoà Cường Bắc, Hải Châu, Đà Nẵng 550000', 'https://salt.topdev.vn/oGevHn28xYm7F4P5AHbLh6HCuZ2LbdnWTzpRoLTA-8c/fit/828/1000/ce/1/aHR0cHM6Ly9hc3NldHMudG9wZGV2LnZuL2ltYWdlcy8yMDI0LzA1LzE1L1RvcERldi1tMWpBMkFXeXB1SUlFTDdoLTE3MTU3NjM0ODcuanBn', 'datahouse@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '0777 876 588', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('37b1bf2d-6a8f-46a1-b83b-a9f3b7dc97e5', 'true', '65 Hải Phòng, Thạch Thang, Hải Châu, Đà Nẵng 550000', 'https://scontent.fdad3-5.fna.fbcdn.net/v/t39.30808-6/445248269_445653454877276_4645968178818809231_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeE6sdCLtYXcAx7KJqRouZ4ZFyQfFkI5fXEXJB8WQjl9cV4nAqG-xO8_FBvECmHTES4fd4PtwpkWMqMBm7YwOCx_&_nc_ohc=65Z215o7ew0Q7kNvgHIz8_5&_nc_zt=23&_nc_ht=scontent.fdad3-5.fna&oh=00_AYDrDwclpLK5wjMC1mAHG2kHCwVdnblCzs7Q4Sclb12bvA&oe=667430C2', 'hr_vietnam@east-consulting-china.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '0317580857', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('ee38efc5-fdb3-43e4-95b9-17e965c4d53f', 'true', '27 Chế Viết Tấn, Khu ĐTST, Cẩm Lệ, Đà Nẵng 550000', 'https://media.enlabsoftware.com/wp-content/uploads/2022/06/17144835/Enlab-workspace-company.png', 'info@enlabsoftware.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 364 360 601', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('1eb3d316-494e-4e9a-8bcb-77574122eb1d', 'true', 'Lô 28 Nguyễn Sinh Sắc, Hoà Minh, Liên Chiểu, Đà Nẵng 50606', 'https://scontent.fdad3-1.fna.fbcdn.net/v/t39.30808-6/421215906_1256332535291795_3657774664706998616_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeHG2w4sCxNmPN6JeWb1HAnH44qWIzKUchvjipYjMpRyGwquJwg3H4PeZwGDZGG_ZOZnHfbKOEh2rXIyO16g03NP&_nc_ohc=O_h01PEoi3UQ7kNvgH2lDfr&_nc_zt=23&_nc_ht=scontent.fdad3-1.fna&oh=00_AYBjqXaBE6rodhHnevzL3Ofamtb1FZp-wUjV0W1W0k0drg&oe=66744C09', 'admin@24h.dev', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '0385 928 019', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('0e50220e-fcb8-42d1-b08a-0e04a3bf7ee8', 'true', 'Tầng 3 268 đường 30/4, tòa nhà TP, phường Hòa Cường Bắc, Đà Nẵng 550000', 'https://scontent.fdad3-1.fna.fbcdn.net/v/t39.30808-6/439311143_937317804850961_4510840546511427633_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeGNCotSMwOZLDiPue0QJEi7c6MPz3USDahzow_PdRINqPiqA0NlWZllPEQR-apDKA18XtQd1p2YaDEZc__nufAD&_nc_ohc=UUHLSazosPwQ7kNvgGpZD75&_nc_zt=23&_nc_ht=scontent.fdad3-1.fna&oh=00_AYChOa0VXXu0TBTlSNxu6LVvheSOJnbvPxkCzcShjtPOKQ&oe=66744ED7', 'info@ncc.asia', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '(+84) 2466874606', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('2da94a6b-7a52-489f-8846-cd41015e41d6', 'true', '49A Phan Bội Châu, Thạch Thang, Hải Châu, Đà Nẵng 550000', 'https://scontent.fdad3-3.fna.fbcdn.net/v/t39.30808-6/429534827_975817230683983_5710116258277744743_n.jpg?stp=dst-jpg_s960x960&_nc_cat=109&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeGfIzM5gw5UeNwH9NzlvYRiKJ6j4nlzRTEonqPieXNFMaqWXFvRCELwoyRUeryJf3rPj5NQoLMyZE39QjEDE4ul&_nc_ohc=GlIh0-OeDYQQ7kNvgEOTXsZ&_nc_zt=23&_nc_ht=scontent.fdad3-3.fna&oh=00_AYAXP-IhL1DiEAOjJAMrUBse_Wf5GmOFetFtWviSUZzFrA&oe=667437F8', 'nals_recruiter@nal.vn', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '0236 3562 123', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('00685807-aef2-47de-930a-4814e2fc33fe', 'true', '448 Hoàng Diệu, Bình Thuận, Hải Châu, Đà Nẵng 550000', 'https://scontent.fdad3-1.fna.fbcdn.net/v/t39.30808-6/425768199_396333349715298_8259473804183254212_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeHH693YzaT6vDTb8FfDgGecr5JLuiGKUGevkku6IYpQZ1rlNZ1koqm0tq9VxpV60RYpnT171vkaXJi-Px-xw7JM&_nc_ohc=P8RVTpNmSowQ7kNvgGBglsT&_nc_zt=23&_nc_ht=scontent.fdad3-1.fna&oh=00_AYBreDhd5pkFoiDwsmQYXHfC7sG0zcmF-27LhchJHeZYcA&oe=66743A21', 'contact@teragroup.io', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '076 320 1051', NULL, '27958f1c-41bf-4ba9-83d1-f3a4c1b3c7e6', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL);


INSERT INTO companies(account_id, company_name, company_url, established_date, description, others, created_at, updated_at) VALUES
('ab5457c7-48e8-47dd-a082-ba79624059cc', 'FPT Software Đà Nẵng', 'fptdanang.pro', '2005-08-13', 'Mở đầu danh sách các công ty IT đà nẵng đó chính là ông lớn công nghệ hàng đầu Việt Nam - tập đoàn FPT. Trải qua hơn 30 năm sóng gió trên thị trường Việt Nam, FPT nói chung và FPT Software Đà Nẵng đã và đang dẫn đầu trong các lĩnh vực về điện toán đám mây, di động, BI và phân tích, cộng tác, ERP, QA,... trong các ngành như CNTT, tài chính, y tế, viễn thông,...
FPT là đơn vị được bình chọn là Nhà tuyển dụng được ưa thích, nằm trong TOP các công ty có môi trường làm việc và phúc lợi nhân sự tốt nhất tại Việt Nam.', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('4024d713-c1bb-4a04-8de5-fbe6f74a07a9', 'Enclave', 'enclave.vn', '2007-1-1', 'Công ty Enclave Đà Nẵng hoạt động chủ yếu trong lĩnh vực gia công phần mềm (ITO), kỹ thuật phần mềm cho các thị trường tại California, Michigan, New Hampshire, Florida, Nhật Bản, Úc, Singapore, Malaysia, Các Tiểu vương quốc Ả Rập Thống nhất,...', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('e13f71e1-310d-40ff-8ada-b0153a7af374', 'Softech', 'softech.vn', '2000-1-1', 'Softech là công ty công nghệ phần mềm cung cấp dịch vụ công nghệ tại Đà Nẵng và nhiều tỉnh thành khác. Công ty Softech cũng là nơi đào tạo nguồn nhân lực công ty IT chất lượng cao, quy tụ nhiều nhân sự trí tuệ, nhiệt huyết.', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('1f01fb26-8141-422d-8dca-f7587f41812c', 'AsiaSoft', 'asiasoft.com.vn', '2019-2-1', 'AsiaSoft là công ty cung cấp giải pháp quản lý tổng thể và phần mềm quản lý doanh nghiệp được nhiều khách hàng đánh giá cao. Hiện nay công ty hoạt động tại 3 cơ sở Hà Nội, Đà Nẵng, TPHCM với các dịch vụ chính: Giải pháp phần mềm quản lý doanh nghiệp trong nhiều lĩnh vực: Du lịch, BĐS, giao vận,...', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('f36a9b52-f3cd-40b6-9632-8c70960c8a93', 'BAP Software', 'bap-software.net/vi', '2016-3-1', 'BAP Software là một trong số các doanh nghiệp A-IoT tốt nhất tại Việt Nam, cung cấp rất nhiều dịch vụ và giải pháp công nghệ trong nhiều lĩnh vực', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('5c63f82a-8948-473e-9686-0077a2f22042', 'DataHouse', 'https://www.linkedin.com/company/datahouseasia/', '2015-1-1', 'Thành lập 2015 tại Sanghai', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('37b1bf2d-6a8f-46a1-b83b-a9f3b7dc97e5', '𝐄𝐀𝐒𝐓 𝐀𝐔𝐓𝐎𝐌𝐎𝐓𝐈𝐕𝐄 𝐄𝐋𝐄𝐂𝐓𝐑𝐎𝐍𝐈𝐂𝐒', 'https://hr1tech.com/vi/company/cong-ty-tnhh-east-automotive-electronics-vietnam-35124.html', '2015-1-1', 'EAST là công ty kỹ thuật và tư vấn chuyên nghiệp trong ngành công nghiệp ô tô, thành lập năm 2015 tại Thượng Hải. Với hơn 200 kỹ sư và chuyên gia tư vấn, EAST hoạt động trên phạm vi toàn cầu với các văn phòng tại Thượng Hải, Trường Sa, Quảng Châu, Đà Nẵng (Việt Nam) và Munich (Đức). Chuyên môn cốt lõi của EAST là cung cấp các dịch vụ về kỹ thuật phần mềm điện tử ô tô, tư vấn và đánh giá cho các nhà sản xuất thiết bị gốc (OEM) và các công ty Tier-1 trong ngành ô tô.', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('ee38efc5-fdb3-43e4-95b9-17e965c4d53f', 'Cổ Phần Enlab Software', 'https://enlabsoftware.com/', '2014-5-1', 'Enlab Software là một công ty chuyên về phát triển phần mềm nước ngoài, cung cấp các ứng dụng doanh nghiệp với chất lượng hàng đầu trong mỗi lần giao hàng Agile. Với sự minh bạch và chính trực hoàn toàn. Tại Enlab, chúng tôi cố gắng xây dựng một công ty nơi những con người tuyệt vời như bạn có thể làm việc tốt nhất, được truyền cảm hứng bởi các đồng nghiệp chuyên nghiệp nhưng ấm áp. Đó là nơi bạn có thể thúc đẩy sự phát triển nghề nghiệp của mình và xây dựng một cuộc sống cá nhân lành mạnh. Cuối cùng, chúng tôi tạo ra một mối quan hệ lâu dài nơi Nhân văn, Chính trực và Xuất sắc được coi trọng.', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('1eb3d316-494e-4e9a-8bcb-77574122eb1d', '24HDEV', 'https://danangnet.vn/cong-ty-tnhh-24hdev.html', '2019-11-15', 'Tên doanh nghiệp: CÔNG TY TNHH 24HDEV. Công ty trách nhiệm hữu hạn một thành viên', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('0e50220e-fcb8-42d1-b08a-0e04a3bf7ee8', '𝐍𝐂𝐂 𝐃𝐀 𝐍𝐀𝐍𝐆', 'https://career.ncc.asia/', '1889-5-24', 'Các vị trí Thực tập sinh mà nhà NCC đang tìm kiếm sẽ có trên danh sách dưới đây, ưu tiên các ứng viên làm được trên 28h/w:', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('2da94a6b-7a52-489f-8846-cd41015e41d6', '𝗡𝗔𝗟 𝗦𝗼𝗹𝘂𝘁𝗶𝗼𝗻𝘀', 'nals.vn', '2015-10-1', 'chúng mình là công ty công nghệ đã có 9 năm hoạt động trong lĩnh vực offshore outsourcing cho thị trường Nhật Bản. Tham gia vào dự án với các Đối tác, Khách hàng Nhật Bản sẽ là cơ hội để các bạn sinh viên được thực chiến và nâng cao năng lực tiếng Nhật của bản thân.', NULL, '2024-06-15 17:45:55.714662+00', NULL),
('00685807-aef2-47de-930a-4814e2fc33fe', 'Tera Group', 'https://teragroup.io/', '2022-1-1', 'In today’s digital landscape, technology is transforming businesses and shaping our future in many different ways. The marketplace is constantly evolving, venturing into more complex venues and directions.', NULL, '2024-06-15 17:45:55.714662+00', NULL);

-- Accounts + Users table
INSERT INTO accounts (account_id, account_status, address, avatar, email, password, phone_number, refresh_token, system_role, created_at, updated_at, deleted_at)
VALUES
('9426b672-77f3-4ac5-9067-7b555d363335', 'true', '123 Đường ABC, Quận XYZ, Thành phố HCM', NULL, 'minh.nguyen@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 123 456 789', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('56b2265d-c1a0-46a1-af46-a330f6b81269', 'true', '456 Đường DEF, Quận UVW, Thành phố Hà Nội', NULL, 'lan.vu@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 987 654 321', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-07-10 14:30:00', '2023-07-10 14:30:00', NULL),
('facc38c4-7da7-4665-900e-b0c0287b02fb', 'true', '789 Đường GHI, Quận RST, Thành phố Đà Nẵng', NULL, 'hung.le@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 567 123 890', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-08-05 18:15:00', '2023-08-05 18:15:00', NULL),
('d3ec5d0f-5526-43ea-98be-44b7d88cc236', 'true', '012 Đường JKL, Quận MNO, Thành phố Cần Thơ', NULL, 'hang.pham@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 234 567 901', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-09-01 12:45:00', '2023-09-01 12:45:00', NULL),
('fcc5255f-36f6-45de-a4b4-0ac80e2e397f', 'true', '345 Đường PQR, Quận STU, Thành phố Hải Phòng', NULL, 'nam.tran@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 678 901 234', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-10-12 16:30:00', '2023-10-12 16:30:00', NULL),
('c9a19a03-474d-4956-a6a3-66c15a1ff822', 'true', '678 Đường VWX, Quận YZ, Thành phố Vũng Tàu', NULL, 'thao.vo@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 890 123 456', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-11-08 11:15:00', '2023-11-08 11:15:00', NULL),
('55929009-c6bb-42fd-abc3-06705a667b48', 'true', '901 Đường XYZ, Quận ABC, Thành phố Nha Trang', NULL, 'tuan.dang@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 901 234 567', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-12-01 15:00:00', '2023-12-01 15:00:00', NULL),
('84d44356-aa42-4b60-ade2-e819c5af683a', 'true', '234 Đường DEF, Quận UVW, Thành phố Phú Quốc', NULL, 'mai.bui@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 345 678 901', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-01-05 09:45:00', '2024-01-05 09:45:00', NULL),
('bd613efb-2a82-4416-9e2c-2e4d3fb2670a', 'true', '567 Đường GHI, Quận RST, Thành phố Hà Giang', NULL, 'dung.do@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 567 890 123', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-02-10 13:30:00', '2024-02-10 13:30:00', NULL),
('fbb3467d-6ebc-4116-8a8e-743fa9073d5c', 'true', '890 Đường JKL, Quận MNO, Thành phố Buôn Ma Thuột', NULL, 'quynh.ly@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 678 901 234', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-03-15 17:15:00', '2024-03-15 17:15:00', NULL),
('002ebfa0-d3c2-411a-a787-4ea1afe2511c', 'true', '123 Đường PQR, Quận STU, Thành phố Hồ Chí Minh', NULL, 'khoa.phan@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 789 012 345', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-04-08 11:45:00', '2024-04-08 11:45:00', NULL),
('09c30f1a-9fd0-476d-9765-bb89986cd1ca', 'true', '456 Đường VWX, Quận YZ, Thành phố Đà Lạt', NULL, 'ngan.truong@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 890 123 456', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-05-01 15:30:00', '2024-05-01 15:30:00', NULL),
('2552fc06-ae7d-486c-a035-c937340560da', 'true', '789 Đường XYZ, Quận ABC, Thành phố Hải Dương', NULL, 'hieu.dinh@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 901 234 567', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-06-12 10:00:00', '2024-06-12 10:00:00', NULL),
('6fc29ac4-9d12-4677-b7e9-b83396463e65', 'true', '012 Đường DEF, Quận UVW, Thành phố Quy Nhơn', NULL, 'phuong.cao@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 123 456 789', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-07-05 14:30:00', '2024-07-05 14:30:00', NULL),
('5d621cc4-921f-4cad-adef-198d03cfb1ab', 'true', '345 Đường GHI, Quận RST, Thành phố Vũng Tàu', NULL, 'thang.vo@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 987 654 321', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-08-01 18:15:00', '2024-08-01 18:15:00', NULL),
('cdc6a256-43e5-4b23-bc14-054a51e0d0c3', 'true', '678 Đường JKL, Quận MNO, Thành phố Hà Tĩnh', NULL, 'thu.tran@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 567 123 890', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-09-10 12:45:00', '2024-09-10 12:45:00', NULL),
('d4b06d0a-f421-4de2-be38-2434d559baee', 'true', '901 Đường PQR, Quận STU, Thành phố Cà Mau', NULL, 'quoc.le@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 234 567 890', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-09-10 12:45:00', '2024-09-10 12:45:00', NULL),
('03ffa722-0d26-44b1-b800-78907d6a3c57', 'true', '234 Đường VWX, Quận YZ, Thành phố Đà Nẵng', NULL, 'thuy.phan@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 901 234 567', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-11-15 11:15:00', '2024-11-15 11:15:00', NULL),
('7572cf8d-d08b-4b4b-910b-5d052fe0c6f7', 'true', '567 Đường XYZ, Quận ABC, Thành phố Hải Phòng', NULL, 'huy.vo@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84 901 234 567', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-12-10 15:00:00', '2024-12-10 15:00:00', NULL),
('86f7a492-0282-475b-82c1-293b25197882', 'true', 'Số 123 Đường ABC, Quận A, Thành phố Hà Nội', NULL, 'linh.tran@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84123456789', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-06-15 10:00:00', '2023-06-15 10:00:00', NULL),
('a39e0855-497c-467a-b8c5-4c5882968594', 'true', 'Số 456 Đường XYZ, Quận B, Thành phố Hồ Chí Minh', NULL, 'kien.nguyen@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84987654321', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-07-10 14:30:00', '2023-07-10 14:30:00', NULL),
('3f10c869-2045-4f9d-829b-28868c6a8e31', 'true', 'Số 789 Đường PQR, Quận C, Thành phố Đà Nẵng', NULL, 'hoa.le@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+841122334455', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-08-05 18:15:00', '2023-08-05 18:15:00', NULL),
('d45fb02b-1285-40c6-a672-5e0679c421a6', 'true', 'Số 321 Đường JKL, Quận D, Thành phố Hải Phòng', NULL, 'duy.pham@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+849988776655', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-09-01 12:45:00', '2023-09-01 12:45:00', NULL),
('18c89c4b-4762-4921-820b-6d537f35e884', 'true', 'Số 654 Đường MNO, Quận E, Thành phố Cần Thơ', NULL, 'an.vu@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+841122334455', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-10-12 16:30:00', '2023-10-12 16:30:00', NULL),
('4a740f7f-627a-4927-a8b9-830e293545f1', 'true', 'Số 987 Đường UVW, Quận F, Thành phố Huế', NULL, 'thinh.dang@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+849988776655', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-11-08 11:15:00', '2023-11-08 11:15:00', NULL),
('970e6683-875b-456a-9363-6a86728c21fb', 'true', 'Số 111 Đường RST, Quận G, Thành phố Nha Trang', NULL, 'trang.bui@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84123456789', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2023-12-01 15:00:00', '2023-12-01 15:00:00', NULL),
('710c42f8-1769-49b7-8068-2a5835976b09', 'true', 'Số 222 Đường LMN, Quận H, Thành phố Vũng Tàu', NULL, 'phong.do@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84987654321', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-01-05 09:45:00', '2024-01-05 09:45:00', NULL),
('1a8f7c6b-5923-487a-959f-5a648d190674', 'true', 'Số 333 Đường EFG, Quận I, Thành phố Đồng Nai', NULL, 'vy.ly@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+841122334455', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-02-10 13:30:00', '2024-02-10 13:30:00', NULL),
('9477380a-9e65-4841-b59c-471426956d2a', 'true', 'Số 444 Đường HIJ, Quận J, Thành phố Quy Nhơn', NULL, 'hai.hoang@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+849988776655', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-03-15 17:15:00', '2024-03-15 17:15:00', NULL),
('684c2f35-2184-4b89-a330-83746c2490e5', 'true', 'Số 555 Đường XYZ, Quận K, Thành phố Hải Dương', NULL, 'ngoc.tran@gmail.com', '$2a$10$sJMcIgyhLxQBur0rfOT3YOmneAFbEgavR6MizUHqj1BsB1qWZxLGm', '+84123456789', NULL, '3a977d27-e39a-447c-b407-06df407b7c46', '2024-04-08 11:45:00', '2024-04-08 11:45:00', NULL);

INSERT INTO users (account_id, date_of_birth, first_name, gender, last_name, others, social_media_link, summary_introduction, created_at, updated_at) VALUES
('9426b672-77f3-4ac5-9067-7b555d363335', '1990-05-12', 'Minh', 'true', 'Nguyễn', null, '{facebook.com,instagram.com}', 'Tôi là một người yêu thích du lịch và khám phá văn hóa. Tôi luôn muốn trải nghiệm những điều mới mẻ và chia sẻ những câu chuyện của mình với mọi người.', '2023-06-15 10:00:00', '2023-06-15 10:00:00'),
('56b2265d-c1a0-46a1-af46-a330f6b81269', '1985-10-28', 'Lan', 'false', 'Vũ', null, '{twitter.com,youtube.com}', 'Tôi đam mê nấu ăn và sáng tạo những món ngon từ nguyên liệu tự nhiên. Tôi muốn chia sẻ niềm vui và kỹ năng nấu ăn của mình với cộng đồng.', '2023-07-10 14:30:00', '2023-07-10 14:30:00'),
('facc38c4-7da7-4665-900e-b0c0287b02fb', '1995-02-18', 'Hùng', 'true', 'Lê', null, '{linkedin.com}', 'Tôi là một lập trình viên web, đam mê công nghệ và tìm kiếm những giải pháp sáng tạo cho các vấn đề.', '2023-08-05 18:15:00', '2023-08-05 18:15:00'),
('d3ec5d0f-5526-43ea-98be-44b7d88cc236', '2000-07-04', 'Hằng', 'false', 'Phạm', null, '{}', 'Tôi là một người yêu thích nghệ thuật, đặc biệt là âm nhạc và hội họa. Tôi luôn muốn tìm kiếm và chia sẻ những giá trị nghệ thuật đẹp.', '2023-09-01 12:45:00', '2023-09-01 12:45:00'),
('fcc5255f-36f6-45de-a4b4-0ac80e2e397f', '1998-11-25', 'Nam', 'true', 'Trần', null, '{pinterest.com,tumblr.com}', 'Tôi là một người yêu thích thể thao, đặc biệt là bóng đá. Tôi luôn muốn theo dõi và cập nhật thông tin về các giải đấu bóng đá trên thế giới.', '2023-10-12 16:30:00', '2023-10-12 16:30:00'),
('c9a19a03-474d-4956-a6a3-66c15a1ff822', '1988-03-15', 'Thảo', 'false', 'Võ', null, '{tiktok.com}', 'Tôi là một người yêu thích thời trang và làm đẹp. Tôi muốn chia sẻ những kinh nghiệm và bí quyết làm đẹp của mình với mọi người.', '2023-11-08 11:15:00', '2023-11-08 11:15:00'),
('55929009-c6bb-42fd-abc3-06705a667b48', '2002-09-08', 'Tuấn', 'true', 'Đặng', null, '{}', 'Tôi là một người yêu thích văn học và lịch sử. Tôi muốn chia sẻ những kiến thức và cảm nhận của mình về những tác phẩm văn học và sự kiện lịch sử.', '2023-12-01 15:00:00', '2023-12-01 15:00:00'),
('84d44356-aa42-4b60-ade2-e819c5af683a', '1992-04-22', 'Mai', 'false', 'Bùi', null, '{snapchat.com}', 'Tôi là một người yêu thích thiên nhiên và động vật. Tôi muốn chia sẻ những hình ảnh và câu chuyện đẹp về thiên nhiên và động vật.', '2024-01-05 09:45:00', '2024-01-05 09:45:00'),
('bd613efb-2a82-4416-9e2c-2e4d3fb2670a', '1987-06-18', 'Dũng', 'true', 'Đỗ', null, '{reddit.com}', 'Tôi là một người yêu thích công nghệ và khoa học. Tôi muốn chia sẻ những kiến thức và thông tin mới nhất về công nghệ và khoa học.', '2024-02-10 13:30:00', '2024-02-10 13:30:00'),
('fbb3467d-6ebc-4116-8a8e-743fa9073d5c', '2001-01-30', 'Quỳnh', 'false', 'Lý', null, '{soundcloud.com}', 'Tôi là một người yêu thích âm nhạc và sáng tác nhạc. Tôi muốn chia sẻ những sáng tác và cảm xúc của mình qua âm nhạc.', '2024-03-15 17:15:00', '2024-03-15 17:15:00'),
('002ebfa0-d3c2-411a-a787-4ea1afe2511c', '1996-08-14', 'Khoa', 'true', 'Phan', null, '{}', 'Tôi là một người yêu thích thể thao và hoạt động ngoài trời. Tôi muốn chia sẻ những kinh nghiệm và kiến thức của mình về thể thao và du lịch.', '2024-04-08 11:45:00', '2024-04-08 11:45:00'),
('09c30f1a-9fd0-476d-9765-bb89986cd1ca', '1989-12-05', 'Ngân', 'false', 'Trương', null, '{twitch.com}', 'Tôi là một game thủ, đam mê các tựa game online và muốn chia sẻ những kinh nghiệm chơi game của mình với mọi người.', '2024-05-01 15:30:00', '2024-05-01 15:30:00'),
('2552fc06-ae7d-486c-a035-c937340560da', '2003-03-21', 'Hiếu', 'true', 'Đinh', null, '{medium.com}', 'Tôi là một người yêu thích viết lách và chia sẻ những suy nghĩ, cảm xúc của mình. Tôi muốn kết nối với mọi người thông qua những câu chuyện và bài viết của mình.', '2024-06-12 10:00:00', '2024-06-12 10:00:00'),
('6fc29ac4-9d12-4677-b7e9-b83396463e65', '1994-07-09', 'Phương', 'false', 'Cao', null, '{etsy.com}', 'Tôi là một người yêu thích thủ công và sáng tạo. Tôi muốn chia sẻ những sản phẩm thủ công độc đáo và sáng tạo của mình với mọi người.', '2024-07-05 14:30:00', '2024-07-05 14:30:00'),
('5d621cc4-921f-4cad-adef-198d03cfb1ab', '2000-04-16', 'Thắng', 'true', 'Võ', null, '{github.com}', 'Tôi là một lập trình viên, đam mê công nghệ và tìm kiếm những giải pháp mới cho các vấn đề.', '2024-08-01 18:15:00', '2024-08-01 18:15:00'),
('cdc6a256-43e5-4b23-bc14-054a51e0d0c3', '1997-10-03', 'Thu', 'false', 'Trần', null, '{vimeo.com}', 'Tôi là một người yêu thích nhiếp ảnh và video. Tôi muốn chia sẻ những tác phẩm nhiếp ảnh và video đẹp của mình với mọi người.', '2024-09-10 12:45:00', '2024-09-10 12:45:00'),
('d4b06d0a-f421-4de2-be38-2434d559baee', '1986-02-28', 'Quốc', 'true', 'Lê', null, '{dribbble.com}', 'Tôi là một nhà thiết kế đồ họa, đam mê sáng tạo và tạo ra những sản phẩm thiết kế độc đáo.', '2024-10-05 16:30:00', '2024-10-05 16:30:00'),
('03ffa722-0d26-44b1-b800-78907d6a3c57', '2002-05-19', 'Thùy', 'false', 'Phan', null, '{behance.net}', 'Tôi là một người yêu thích nghệ thuật và sáng tạo. Tôi muốn chia sẻ những tác phẩm nghệ thuật và thiết kế của mình với mọi người.', '2024-11-15 11:15:00', '2024-11-15 11:15:00'),
('7572cf8d-d08b-4b4b-910b-5d052fe0c6f7', '1991-08-06', 'Huy', 'true', 'Võ', null, '{spotify.com}', 'Tôi là một người yêu thích âm nhạc và khám phá những bản nhạc mới. Tôi muốn chia sẻ những bản nhạc yêu thích của mình với mọi người.', '2024-12-10 15:00:00', '2024-12-10 15:00:00'),
('86f7a492-0282-475b-82c1-293b25197882', '1992-08-14', 'Linh', 'false', 'Trần', NULL, '{facebook.com,instagram.com}', 'Là một người yêu thích sự tĩnh lặng, tôi tìm thấy niềm vui trong việc đọc sách và nấu ăn. Đọc sách mở ra cho tôi những chân trời mới, cho tôi được sống trong muôn vàn cuộc đời và khám phá những điều kỳ diệu của thế giới. Còn nấu ăn lại là cách tôi thể hiện sự sáng tạo và chăm sóc bản thân cũng như những người xung quanh. Niềm đam mê với ẩm thực thôi thúc tôi không ngừng tìm tòi, học hỏi để tạo ra những món ăn ngon và đẹp mắt.', '2023-06-15 10:00:00', '2023-06-15 10:00:00'),
('a39e0855-497c-467a-b8c5-4c5882968594', '1987-03-22', 'Kiên', 'true', 'Nguyễn', NULL, '{twitter.com,youtube.com}', 'Với tư cách là một kỹ sư phần mềm, tôi đam mê việc biến những ý tưởng sáng tạo thành hiện thực thông qua những dòng code. Mỗi dự án mới là một thử thách đầy hứng khởi, thôi thúc tôi không ngừng học hỏi, trau dồi kỹ năng để tạo ra những sản phẩm công nghệ đột phá và hữu ích. Tôi tin rằng công nghệ có sức mạnh thay đổi thế giới và tôi muốn góp phần tạo nên sự thay đổi tích cực đó.', '2023-07-10 14:30:00', '2023-07-10 14:30:00'),
('3f10c869-2045-4f9d-829b-28868c6a8e31', '1998-11-05', 'Hòa', 'false', 'Lê', NULL, '{linkedin.com}', 'Du lịch và ẩm thực là hai niềm đam mê bất tận của tôi.  Mỗi chuyến đi là cơ hội để tôi được đắm mình trong những nền văn hóa đặc sắc, khám phá những vùng đất mới lạ và thưởng thức những món ăn độc đáo. Hương vị của từng món ăn, cách bài trí tinh tế, không gian ấm cúng của nhà hàng, tất cả hòa quyện tạo nên một trải nghiệm ẩm thực tuyệt vời, đánh thức mọi giác quan của tôi.', '2023-08-05 18:15:00', '2023-08-05 18:15:00'),
('d45fb02b-1285-40c6-a672-5e0679c421a6', '2001-04-12', 'Duy', 'true', 'Phạm', NULL, '{}', 'Âm nhạc là ngôn ngữ của tâm hồn, là dòng chảy bất tận của cảm xúc.  Tôi yêu âm nhạc bởi nó có sức mạnh kết nối con người, xoa dịu nỗi đau và khơi dậy những niềm vui trong cuộc sống. Từ những giai điệu du dương đến những bản nhạc sôi động, âm nhạc luôn hiện diện trong cuộc sống của tôi như một người bạn đồng hành không thể thiếu.', '2023-09-01 12:45:00', '2023-09-01 12:45:00'),
('18c89c4b-4762-4921-820b-6d537f35e884', '1995-09-28', 'An', 'false', 'Vũ', NULL, '{pinterest.com,tumblr.com}', 'Vẽ tranh và chơi thể thao là hai niềm đam mê đối lập nhưng lại bổ sung cho nhau một cách hoàn hảo trong con người tôi. Vẽ tranh cho tôi sự tỉ mỉ, kiên nhẫn và khả năng quan sát tinh tế, trong khi đó chơi thể thao rèn luyện cho tôi sự dẻo dai, sức bền và tinh thần chiến thắng. Cả hai hoạt động này đều mang đến cho tôi niềm vui, sự sảng khoái và giúp tôi cân bằng cuộc sống.', '2023-10-12 16:30:00', '2023-10-12 16:30:00'),
('4a740f7f-627a-4927-a8b9-830e293545f1', '1989-02-18', 'Thịnh', 'true', 'Đặng', NULL, '{tiktok.com}', 'Sống trong thời đại công nghệ 4.0, tôi bị cuốn hút bởi sức mạnh kỳ diệu của công nghệ, bởi khả năng kết nối con người, thay đổi thế giới và tạo ra những giá trị mới. Tôi luôn cập nhật những xu hướng công nghệ mới nhất, tìm hiểu và ứng dụng chúng vào cuộc sống hàng ngày. Đối với tôi, công nghệ không chỉ là công cụ mà còn là niềm đam mê, là động lực để tôi không ngừng học hỏi và phát triển.', '2023-11-08 11:15:00', '2023-11-08 11:15:00'),
('970e6683-875b-456a-9363-6a86728c21fb', '2003-07-04', 'Trang', 'false', 'Bùi', NULL, '{}', 'Xem phim và nghe nhạc là hai hình thức giải trí giúp tôi thư giãn sau những giờ học tập và làm việc căng thẳng.  Phim ảnh đưa tôi vào những câu chuyện đầy màu sắc, cho tôi được sống trong những thế giới khác nhau, trải nghiệm những cung bậc cảm xúc khác nhau. Âm nhạc lại là liều thuốc tinh thần giúp tôi xoa dịu căng thẳng, khơi dậy cảm xúc và tiếp thêm năng lượng cho ngày mới.', '2023-12-01 15:00:00', '2023-12-01 15:00:00'),
('710c42f8-1769-49b7-8068-2a5835976b09', '1990-12-25', 'Phong', 'true', 'Đỗ', NULL, '{snapchat.com}', 'Bóng đá không chỉ là môn thể thao vua mà còn là niềm đam mê mãnh liệt của tôi. Từ khi còn nhỏ, tôi đã bị thu hút bởi trái bóng tròn, bởi những pha đi bóng kỹ thuật, những bàn thắng đẹp mắt và tinh thần fair-play cao thượng trên sân cỏ. Bóng đá mang đến cho tôi niềm vui, sự sảng khoái và giúp tôi kết nối với bạn bè, người thân có chung niềm đam mê.', '2024-01-05 09:45:00', '2024-01-05 09:45:00'),
('1a8f7c6b-5923-487a-959f-5a648d190674', '1997-05-14', 'Vy', 'false', 'Lý', NULL, '{reddit.com}', 'Được khám phá thế giới, trải nghiệm những nền văn hóa mới, gặp gỡ những con người mới là điều tôi luôn khao khát. Mỗi chuyến du lịch là một hành trình đầy thú vị, giúp tôi mở mang kiến thức, trau dồi kinh nghiệm sống và lưu giữ những kỉ niệm đáng nhớ.  Tôi tin rằng du lịch là một phần không thể thiếu trong cuộc sống, giúp chúng ta trưởng thành và sống trọn vẹn hơn.', '2024-02-10 13:30:00', '2024-02-10 13:30:00'),
('9477380a-9e65-4841-b59c-471426956d2a', '2000-10-28', 'Hải', 'true', 'Hoàng', NULL, '{soundcloud.com}', 'Với tôi, nhiếp ảnh không chỉ là việc ghi lại khoảnh khắc mà còn là nghệ thuật lưu giữ cảm xúc, kể chuyện bằng hình ảnh. Mỗi bức ảnh tôi chụp đều chứa đựng một câu chuyện, một thông điệp mà tôi muốn truyền tải đến người xem.  Nhiếp ảnh cho tôi cái nhìn tinh tế hơn về cuộc sống, giúp tôi nhìn thấy vẻ đẹp tiềm ẩn trong những điều bình dị nhất.', '2024-03-15 17:15:00', '2024-03-15 17:15:00'),
('684c2f35-2184-4b89-a330-83746c2490e5', '1993-03-08', 'Ngọc', 'false', 'Trần', NULL, '{twitch.com}', 'Học ngoại ngữ và đọc sách mở ra cho tôi cánh cửa đến với thế giới, giúp tôi kết nối với những nền văn hóa khác nhau và mở mang kiến thức.  Tôi yêu thích việc đắm mình trong những trang sách, khám phá những câu chuyện hấp dẫn, học hỏi những điều mới mẻ từ những nền văn minh khác nhau.', '2024-04-08 11:45:00', '2024-04-08 11:45:00');