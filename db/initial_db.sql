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
