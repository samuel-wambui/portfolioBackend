drop table if exists
    portfolio_knowledge_chunks,
    project_screenshots,
    project_technologies,
    project_challenges,
    project_results,
    project_lessons_learned,
    projects,
    skills,
    profile_roles,
    profile_specializations,
    profile_focus_items,
    experience_technologies,
    experience_highlights,
    experience_items,
    education_items,
    certifications,
    profiles,
    blog_tags,
    blog_posts,
    contact_messages,
    admin_users
cascade;

create extension if not exists vector;

create table profiles (
    id bigserial primary key,
    portfolio_id varchar(32) not null default 'PORT001',
    full_name varchar(255),
    title varchar(255),
    hero_text text,
    summary text,
    email varchar(255),
    phone varchar(255),
    location varchar(255),
    github_url varchar(255),
    linkedin_url varchar(255),
    portfolio_url varchar(255),
    cv_url varchar(255),
    photo_url varchar(255),
    projects_completed integer,
    years_experience integer,
    learning_label varchar(255),
    collaboration_label varchar(255)
);

create table profile_roles (
    profile_id bigint not null references profiles(id) on delete cascade,
    sort_order integer not null,
    role varchar(255) not null,
    primary key (profile_id, sort_order)
);

create table profile_specializations (
    profile_id bigint not null references profiles(id) on delete cascade,
    sort_order integer not null,
    specialization varchar(255) not null,
    primary key (profile_id, sort_order)
);

create table profile_focus_items (
    profile_id bigint not null references profiles(id) on delete cascade,
    sort_order integer not null,
    focus_item varchar(255) not null,
    primary key (profile_id, sort_order)
);

create table projects (
    id bigserial primary key,
    portfolio_id varchar(32) not null default 'PORT001',
    title varchar(255) not null,
    problem text not null,
    architecture text not null,
    display_order integer,
    github_url varchar(255),
    live_demo_url varchar(255),
    deleted boolean not null default false
);

create table project_screenshots (
    project_id bigint not null references projects(id) on delete cascade,
    sort_order integer not null,
    screenshot varchar(512) not null,
    primary key (project_id, sort_order)
);

create table project_technologies (
    project_id bigint not null references projects(id) on delete cascade,
    sort_order integer not null,
    technology varchar(255) not null,
    primary key (project_id, sort_order)
);

create table project_challenges (
    project_id bigint not null references projects(id) on delete cascade,
    sort_order integer not null,
    challenge text not null,
    primary key (project_id, sort_order)
);

create table project_results (
    project_id bigint not null references projects(id) on delete cascade,
    sort_order integer not null,
    result text not null,
    primary key (project_id, sort_order)
);

create table project_lessons_learned (
    project_id bigint not null references projects(id) on delete cascade,
    sort_order integer not null,
    lesson text not null,
    primary key (project_id, sort_order)
);

create table skills (
    id bigserial primary key,
    portfolio_id varchar(32) not null default 'PORT001',
    name varchar(255) not null,
    category varchar(255) not null,
    level varchar(255) not null,
    deleted boolean not null default false
);

create table experience_items (
    id bigserial primary key,
    portfolio_id varchar(32) not null default 'PORT001',
    company varchar(255) not null,
    role varchar(255) not null,
    start_date varchar(255) not null,
    end_date varchar(255) not null default '',
    current boolean not null,
    description text not null,
    display_order integer,
    deleted boolean not null default false
);

create table experience_technologies (
    experience_id bigint not null references experience_items(id) on delete cascade,
    sort_order integer not null,
    technology varchar(255) not null,
    primary key (experience_id, sort_order)
);

create table education_items (
    id bigserial primary key,
    portfolio_id varchar(32) not null default 'PORT001',
    institution varchar(255) not null,
    course varchar(255) not null,
    grade varchar(255) not null,
    start_date varchar(255) not null,
    end_date varchar(255) not null default '',
    description text not null,
    display_order integer,
    deleted boolean not null default false
);

create table certifications (
    id bigserial primary key,
    portfolio_id varchar(32) not null default 'PORT001',
    name varchar(255) not null,
    issuer varchar(255) not null,
    date_issued varchar(255) not null,
    credential_url varchar(255),
    deleted boolean not null default false
);

create table blog_posts (
    id bigserial primary key,
    portfolio_id varchar(32) not null default 'PORT001',
    slug varchar(255) not null unique,
    title varchar(255) not null,
    excerpt text not null,
    published_at varchar(255) not null,
    read_time varchar(255) not null,
    body text not null,
    deleted boolean not null default false
);

create table blog_tags (
    blog_post_id bigint not null references blog_posts(id) on delete cascade,
    sort_order integer not null,
    tag varchar(255) not null,
    primary key (blog_post_id, sort_order)
);

create table leadership_impact_items (
    id bigserial primary key,
    portfolio_id varchar(32) not null default 'PORT001',
    category varchar(255) not null,
    title varchar(255) not null,
    description text not null,
    impact text not null,
    metric_value varchar(255) not null,
    metric_label varchar(255) not null,
    display_order integer,
    deleted boolean not null default false
);

create table leadership_impact_tags (
    leadership_impact_id bigint not null references leadership_impact_items(id) on delete cascade,
    sort_order integer not null,
    tag varchar(255) not null,
    primary key (leadership_impact_id, sort_order)
);

create table portfolio_knowledge_chunks (
    id bigserial primary key,
    portfolio_id varchar(32) not null,
    source_type varchar(80) not null,
    source_id varchar(80) not null,
    chunk_key varchar(120) not null,
    title varchar(255),
    section varchar(120),
    content text not null,
    metadata jsonb not null default '{}',
    embedding vector(1536),
    embedding_model varchar(120),
    content_hash varchar(64) not null,
    deleted boolean not null default false,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    embedded_at timestamp with time zone,
    unique (portfolio_id, source_type, source_id, chunk_key)
);

create index idx_portfolio_knowledge_lookup
on portfolio_knowledge_chunks (portfolio_id, source_type, deleted);

create index idx_portfolio_knowledge_metadata
on portfolio_knowledge_chunks using gin (metadata);

create index idx_portfolio_knowledge_embedding
on portfolio_knowledge_chunks
using hnsw (embedding vector_cosine_ops)
where deleted = false;

create or replace view portfolio_agent_facts as
select
    p.portfolio_id,
    p.full_name,
    p.title,
    (
        select count(*)
        from certifications c
        where c.portfolio_id = p.portfolio_id
          and c.deleted = false
    ) as certification_count,
    (
        select count(*)
        from projects pr
        where pr.portfolio_id = p.portfolio_id
          and pr.deleted = false
    ) as project_count,
    (
        select count(*)
        from experience_items e
        where e.portfolio_id = p.portfolio_id
          and e.deleted = false
    ) as experience_count,
    (
        select count(*)
        from education_items ed
        where ed.portfolio_id = p.portfolio_id
          and ed.deleted = false
    ) as education_count,
    (
        select count(*)
        from skills s
        where s.portfolio_id = p.portfolio_id
          and s.deleted = false
    ) as skill_count,
    (
        select count(*)
        from leadership_impact_items li
        where li.portfolio_id = p.portfolio_id
          and li.deleted = false
    ) as leadership_impact_count,
    (
        select count(*)
        from blog_posts b
        where b.portfolio_id = p.portfolio_id
          and b.deleted = false
    ) as blog_post_count
from profiles p;

create table contact_messages (
    id bigserial primary key,
    name varchar(255) not null,
    email varchar(255) not null,
    subject varchar(255) not null,
    message text not null,
    created_at timestamp with time zone not null
);

create table admin_users (
    id bigserial primary key,
    username varchar(80) not null unique,
    password varchar(255) not null,
    first_name varchar(120) not null,
    last_name varchar(120) not null,
    email varchar(180) not null unique,
    role varchar(40) not null,
    enabled boolean not null,
    locked boolean not null,
    logged_in boolean not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    last_login_at timestamp with time zone
);
