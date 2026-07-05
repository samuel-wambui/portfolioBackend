begin;

alter table profiles
    alter column full_name drop not null,
    alter column title drop not null,
    alter column hero_text drop not null,
    alter column summary drop not null,
    alter column email drop not null,
    alter column phone drop not null,
    alter column location drop not null,
    alter column github_url drop not null,
    alter column linkedin_url drop not null,
    alter column portfolio_url drop not null,
    alter column projects_completed drop not null,
    alter column years_experience drop not null,
    alter column learning_label drop not null,
    alter column collaboration_label drop not null;

commit;
