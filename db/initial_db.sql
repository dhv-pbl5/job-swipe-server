-- constants
CREATE TABLE public.constants
(
    constant_id uuid NOT NULL,
    constant_type integer NOT NULL,
    constant_name character varying(1000) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (constant_id)
);

ALTER TABLE IF EXISTS public.constants
    OWNER to "qh47Qsmu19JJRuMq";

-- accounts
CREATE TABLE public.accounts
(
    account_id uuid NOT NULL,
    account_status boolean NOT NULL DEFAULT true,
    address character varying(1000) NOT NULL,
    avatar character varying(1000),
    email character varying(1000) NOT NULL,
    password character varying(1000) NOT NULL,
    phone_number character varying(1000) NOT NULL,
    system_role uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (account_id),
    CONSTRAINT system_role FOREIGN KEY (system_role)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS public.accounts
    OWNER to "qh47Qsmu19JJRuMq";

-- Companies
CREATE TABLE public.companies
(
    account_id uuid NOT NULL,
    company_name character varying(1000) NOT NULL,
    company_url character varying(1000) NOT NULL,
    established_date timestamp with time zone NOT NULL,
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (account_id),
    FOREIGN KEY (account_id)
        REFERENCES public.accounts (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.companies
    OWNER to "qh47Qsmu19JJRuMq";

-- users
CREATE TABLE public.users
(
    account_id uuid NOT NULL,
    first_name character varying(1000) NOT NULL,
    last_name character varying(1000) NOT NULL,
    gender boolean NOT NULL,
    date_of_birth timestamp with time zone NOT NULL,
    summary_introduction text,
    skills character varying(1000)[],
    social_media_link character varying(1000)[],
    other json[],
    created_at timestamp with time zone,
    PRIMARY KEY (account_id),
    FOREIGN KEY (account_id)
        REFERENCES public.accounts (account_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
);

ALTER TABLE IF EXISTS public.users
    OWNER to "qh47Qsmu19JJRuMq";

-- user_application_positions
CREATE TABLE public.user_application_positions
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    apply_position uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (apply_position)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.user_application_positions
    OWNER to "qh47Qsmu19JJRuMq";

-- user_educations
CREATE TABLE public.user_educations
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    study_place character varying(1000) NOT NULL,
    study_duration character varying(1000) NOT NULL,
    majority character varying(1000),
    cpa numeric(100, 10) NOT NULL,
    note character varying(1000),
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.user_educations
    OWNER to "qh47Qsmu19JJRuMq";

-- user_experiences
CREATE TABLE public.user_experiences
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    experience_type uuid NOT NULL,
    work_place character varying(1000) NOT NULL,
    work_duration character varying(1000) NOT NULL,
    "position" character varying(1000) NOT NULL,
    note character varying(1000),
    created_at timestamp with time zone NOT NULL,
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

-- user_awards
CREATE TABLE public.user_awards
(
    id uuid NOT NULL,
    account_id uuid NOT NULL,
    certificate_time timestamp with time zone NOT NULL,
    certificate_name character varying(1000) NOT NULL,
    note character varying(1000),
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.user_awards
    OWNER to "qh47Qsmu19JJRuMq";

-- matches
CREATE TABLE public.matches
(
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    company_id uuid NOT NULL,
    user_matched boolean NOT NULL DEFAULT false,
    company_matched boolean NOT NULL DEFAULT false,
    matched_time timestamp with time zone,
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (company_id)
        REFERENCES public.companies (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.matches
    OWNER to "qh47Qsmu19JJRuMq";

-- conversations
CREATE TABLE public.conversations
(
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    company_id uuid NOT NULL,
    last_message character varying(1000) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES public.users (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (company_id)
        REFERENCES public.companies (account_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.conversations
    OWNER to "qh47Qsmu19JJRuMq";

-- messages
CREATE TABLE public.messages
(
    id uuid NOT NULL,
    conversation_id uuid NOT NULL,
    content character varying(10000),
    read_status boolean NOT NULL DEFAULT false,
    url_file character varying(1000),
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (conversation_id)
        REFERENCES public.conversations (id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.messages
    OWNER to "qh47Qsmu19JJRuMq";

-- notifications
CREATE TABLE public.notifications
(
    id uuid NOT NULL,
    sender_id uuid NOT NULL,
    receiver_id uuid NOT NULL,
    content character varying(1000) NOT NULL,
    read_status boolean NOT NULL DEFAULT false,
    notification_type uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (notification_type)
        REFERENCES public.constants (constant_id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.notifications
    OWNER to "qh47Qsmu19JJRuMq";