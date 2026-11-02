create table users (
    id                uuid primary key,
    email             varchar(320),
    nickname          varchar(100),
    profile_image_url varchar(2048),
    status            varchar(20) not null,
    created_at        timestamptz not null
);

create table social_accounts (
    id               bigint generated always as identity primary key,
    user_id          uuid         not null references users (id),
    provider         varchar(20)  not null,
    provider_user_id varchar(255) not null,
    created_at       timestamptz  not null,
    unique (provider, provider_user_id)
);

create index on social_accounts (user_id);
