begin;

truncate table
    profiles,
    profile_roles,
    profile_specializations,
    profile_focus_items,
    project_screenshots,
    project_technologies,
    project_challenges,
    project_results,
    project_lessons_learned,
    projects,
    skills,
    experience_technologies,
    experience_items,
    education_items,
    certifications,
    blog_tags,
    blog_posts,
    leadership_impact_tags,
    leadership_impact_items
restart identity cascade;

insert into profiles (
    id,
    portfolio_id,
    full_name,
    title,
    hero_text,
    summary,
    email,
    phone,
    location,
    github_url,
    linkedin_url,
    portfolio_url,
    cv_url,
    photo_url,
    projects_completed,
    years_experience,
    learning_label,
    collaboration_label
) values (
    1,
    'PORT001',
    'Samuel Ngari',
    'Automation & AI Engineer',
    E'Building AI-powered systems that automate business operations and accelerate Africa''s digital transformation.',
    E'I build intelligent systems that solve real-world problems, automate complex processes, and create measurable impact.\n\nMy work combines software engineering, automation, and artificial intelligence for practical business transformation.',
    'samuelngari13@gmail.com',
    '+254 746 703 511',
    'Nairobi, Kenya',
    'https://github.com/Ngarisamuel',
    'https://www.linkedin.com/',
    'http://localhost:3000',
    null,
    '/images/samuel-ngari-profile.png',
    20,
    3,
    'Building Intelligent Systems',
    'Open to Opportunities'
);

insert into profile_roles (profile_id, sort_order, role) values
(1, 0, 'Automation & AI Engineer'),
(1, 1, 'Enterprise Software Builder'),
(1, 2, 'Problem Solver');

insert into profile_specializations (profile_id, sort_order, specialization) values
(1, 0, 'Enterprise Banking'),
(1, 1, 'Artificial Intelligence'),
(1, 2, 'Enterprise Software'),
(1, 3, 'Automation & Integration'),
(1, 4, 'Data & Observability');

insert into profile_focus_items (profile_id, sort_order, focus_item) values
(1, 0, 'Building Intelligent Systems'),
(1, 1, 'I turn complex ideas into scalable solutions that create real impact.'),
(1, 2, E'I''m open to exciting opportunities, collaborations, and projects that create real impact.'),
(1, 3, E'Let''s build something meaningful together.');

insert into projects (id, portfolio_id, title, problem, architecture, display_order, github_url, live_demo_url) values
(
    1,
    'PORT001',
    'Enterprise Banking',
    'Banking teams need reliable automation, monitoring, and customer support systems that reduce manual work while improving operational visibility.',
    'Backend services, workflow automation, analytics dashboards, and support tools connect banking operations with measurable customer and transaction outcomes.',
    1,
    'https://github.com/Ngarisamuel',
    null
),
(
    2,
    'PORT001',
    'Artificial Intelligence',
    'Teams need AI assistants and language workflows that can understand documents, email, speech, customer sentiment, and repeated operational requests.',
    'RAG pipelines, AI agents, speech transcription, and sentiment analysis components turn unstructured content into useful operational intelligence.',
    2,
    'https://github.com/Ngarisamuel',
    null
),
(
    3,
    'PORT001',
    'Enterprise Software',
    'Organizations need practical software systems that fit real business workflows across agriculture, church management, livestock tracking, hospitality, and finance.',
    'Domain-focused applications combine clean data models, REST APIs, role-aware interfaces, and reporting tools for day-to-day operations.',
    3,
    'https://github.com/Ngarisamuel',
    null
),
(
    4,
    'PORT001',
    'Automation & Integration',
    'Manual handoffs across tools slow teams down and make customer support, notifications, and reporting harder to scale.',
    'n8n workflows, database integrations, IVR services, Telegram bots, and orchestration patterns connect systems into repeatable business processes.',
    4,
    'https://github.com/Ngarisamuel',
    null
),
(
    5,
    'PORT001',
    'Data & Observability',
    'Operations teams need live visibility into transactions, incidents, fraud patterns, and system health.',
    'Grafana dashboards, analytics layers, OAuth-protected views, and incident visualizations help teams monitor production systems in real time.',
    5,
    'https://github.com/Ngarisamuel',
    null
);

insert into project_screenshots (project_id, sort_order, screenshot) values
(1, 0, '/images/portfolio-hero.png'),
(2, 0, '/images/portfolio-hero.png'),
(3, 0, '/images/portfolio-hero.png'),
(4, 0, '/images/portfolio-hero.png'),
(5, 0, '/images/portfolio-hero.png');

insert into project_technologies (project_id, sort_order, technology) values
(1, 0, 'Spring Boot'),
(1, 1, 'PostgreSQL'),
(1, 2, 'Grafana'),
(1, 3, 'n8n'),
(2, 0, 'OpenAI'),
(2, 1, 'RAG'),
(2, 2, 'Whisper'),
(2, 3, 'Sentiment Analysis'),
(3, 0, 'Java'),
(3, 1, 'React'),
(3, 2, 'REST APIs'),
(3, 3, 'PostgreSQL'),
(4, 0, 'n8n'),
(4, 1, 'Oracle'),
(4, 2, 'Twilio'),
(4, 3, 'Telegram Bot'),
(5, 0, 'Grafana'),
(5, 1, 'OAuth'),
(5, 2, 'Analytics'),
(5, 3, 'Incident Reporting');

insert into project_challenges (project_id, sort_order, challenge) values
(1, 0, 'Balancing automation with traceability in transaction and customer-support workflows.'),
(2, 0, 'Turning unstructured messages, documents, and speech into reliable business signals.'),
(3, 0, 'Designing enterprise systems around real operational processes instead of generic screens.'),
(4, 0, 'Connecting multiple tools without losing reliability, auditability, or maintainability.'),
(5, 0, 'Making live operational data understandable for both technical and business users.');

insert into project_results (project_id, sort_order, result) values
(1, 0, 'Built an automation-powered Transaction Monitoring Platform using Java Spring Boot, n8n, PostgreSQL, and Oracle.'),
(1, 1, 'Delivered 90+ n8n workflows for incident detection, case management, escalation, and reporting automation.'),
(1, 2, 'Built a Voice Transcription & Call Quality Analysis system using Whisper for call center quality evaluation.'),
(1, 3, 'Enhanced customer support automation across digital banking channels.'),
(2, 0, 'AI Agents & Personal Assistant'),
(2, 1, 'RAG-Powered Email Assistant'),
(2, 2, 'Sentiment Analysis'),
(2, 3, 'Whisper Speech Transcription'),
(3, 0, 'EquiFarm - Connecting Farmers to Market: Digitized the farm value chain, connecting farmers to markets, buyers, and services.'),
(3, 1, 'Kanisa App - Church Finance & Management: Managed tithing, offerings, church financing, membership registration, clergy management, and believer engagement.'),
(3, 2, 'Supply Chain Financing System: Enabled businesses to access financing against invoices, manage receivables, track loans, and reduce cash flow gaps.'),
(3, 3, 'AnimalTrack (Mifugo): Animal registration, tracking, and management system for farmers to improve livestock productivity.'),
(3, 4, 'Hotel Management System: Recipe cost tracking, inventory management, and profit optimization tools for hospitality businesses.'),
(4, 0, 'Developed a Customer Support Automation system including a Telegram bot integrated with OpenAI RAG for intelligent query resolution.'),
(4, 1, 'Integrated Twilio for IVR testing and call support during development.'),
(4, 2, 'Orchestrated workflow automation at scale across customer support, reporting, and incident processes.'),
(4, 3, 'Integrated Oracle and PostgreSQL systems for operational workflows.'),
(4, 4, 'Connected backend services, workflow tools, and support channels into repeatable business processes.'),
(5, 0, 'Built a Social Media Monitoring Platform integrating WhatsApp, Facebook, Instagram, and X, powered by AI for sentiment analysis, automated responses, and complaint escalation to call center.'),
(5, 1, 'Developed a portal using React, Spring Boot, and n8n with Grafana dashboards for visualizing social media and transaction monitoring insights.'),
(5, 2, 'Integrated OAuth & SSO with Grafana for secure monitoring and analytics.');

insert into project_lessons_learned (project_id, sort_order, lesson) values
(1, 0, 'Reliable systems must make exceptions visible before automation hides them.'),
(2, 0, 'AI is most useful when it is tied to a specific operational decision or action.'),
(3, 0, 'Enterprise software earns trust when it mirrors the vocabulary and constraints of its users.'),
(4, 0, 'Automation should be observable, recoverable, and easy to explain.'),
(5, 0, 'Dashboards need clear ownership, context, and next actions to create impact.');

insert into skills (id, portfolio_id, name, category, level, deleted) values
(1, 'PORT001', 'Java', 'Languages', 'Advanced', false),
(2, 'PORT001', 'Python', 'Languages', 'Strong', false),
(3, 'PORT001', 'JavaScript', 'Languages', 'Strong', false),
(4, 'PORT001', 'TypeScript', 'Languages', 'Strong', false),
(5, 'PORT001', 'SQL', 'Languages', 'Advanced', false),
(6, 'PORT001', 'Spring Boot', 'Backend', 'Advanced', false),
(7, 'PORT001', 'REST APIs', 'Backend', 'Advanced', false),
(8, 'PORT001', 'n8n', 'Backend', 'Advanced', false),
(9, 'PORT001', 'Node.js', 'Backend', 'Strong', false),
(10, 'PORT001', 'React', 'Frontend', 'Strong', false),
(11, 'PORT001', 'Angular', 'Frontend', 'Growing', false),
(12, 'PORT001', 'HTML', 'Frontend', 'Strong', false),
(13, 'PORT001', 'CSS', 'Frontend', 'Strong', false),
(14, 'PORT001', 'Tailwind CSS', 'Frontend', 'Strong', false),
(15, 'PORT001', 'PostgreSQL', 'Databases', 'Advanced', false),
(16, 'PORT001', 'MySQL', 'Databases', 'Strong', false),
(17, 'PORT001', 'Oracle', 'Databases', 'Strong', false),
(18, 'PORT001', 'Docker', 'DevOps & Tools', 'Strong', false),
(19, 'PORT001', 'Git', 'DevOps & Tools', 'Advanced', false),
(20, 'PORT001', 'GitHub Actions', 'DevOps & Tools', 'Strong', false),
(21, 'PORT001', 'Linux', 'DevOps & Tools', 'Strong', false),
(22, 'PORT001', 'OpenAI', 'AI & Data', 'Strong', false),
(23, 'PORT001', 'RAG', 'AI & Data', 'Strong', false),
(24, 'PORT001', 'Whisper', 'AI & Data', 'Strong', false),
(25, 'PORT001', 'Grafana', 'AI & Data', 'Strong', false),
(26, 'PORT001', 'OAuth', 'DevOps & Tools', 'Strong', false),
(27, 'PORT001', 'SSO', 'DevOps & Tools', 'Strong', false),
(28, 'PORT001', 'LangChain', 'AI & Data', 'Growing', false),
(29, 'PORT001', 'n8n AI Agents', 'AI & Data', 'Strong', false),
(30, 'PORT001', 'Prompt Engineering', 'AI & Data', 'Strong', false);

insert into experience_items (id, portfolio_id, company, role, start_date, end_date, current, description, display_order, deleted) values
(1, 'PORT001', 'Equity Bank', 'Customer Digitization', '2022-01-01', '2023-12-31', false, 'Worked on Equitel Lines mobile application, USSD, and web platforms driving customer digitization and improving self-service channels.', 1, false),
(2, 'PORT001', 'E&M TECH', 'Software Developer', '2024-01-01', '2024-12-31', false, 'Upskilled in modern technologies and delivered 5+ enterprise systems that solve real business problems across agriculture, finance, church management, livestock tracking, and hospitality.', 2, false),
(3, 'PORT001', 'Equity Bank', 'Automation & AI Engineering', '2025-01-01', '', true, 'Building intelligent automation, AI-powered monitoring, and customer support systems that enhance operational efficiency and customer experience.', 3, false);

insert into experience_technologies (experience_id, sort_order, technology) values
(1, 0, 'USSD'),
(1, 1, 'Web Platforms'),
(1, 2, 'Customer Digitization'),
(2, 0, 'Java'),
(2, 1, 'Spring Boot'),
(2, 2, 'React'),
(2, 3, 'PostgreSQL'),
(3, 0, 'AI Automation'),
(3, 1, 'n8n'),
(3, 2, 'Grafana'),
(3, 3, 'Customer Support Systems');

insert into education_items (id, portfolio_id, institution, course, grade, start_date, end_date, description, display_order, deleted) values
(1, 'PORT001', 'Project-based learning', 'Full-Stack Software Engineering', 'Ongoing', '2024', '', 'Focused on Java, Spring Boot, React, PostgreSQL, Docker, automation, and maintainable software design.', 1, false),
(2, 'PORT001', 'Professional learning', 'AI, Automation & Data Systems', 'Ongoing', '2025', '', 'Practical work with OpenAI, RAG, Whisper, Grafana, workflow automation, and production monitoring.', 2, false);

insert into certifications (id, portfolio_id, name, issuer, date_issued, credential_url, deleted) values
(1, 'PORT001', 'Java and Spring Boot Development', 'Professional Learning', '2026-06-28', null, false),
(2, 'PORT001', 'React and TypeScript Frontend Engineering', 'Professional Learning', '2026-06-28', null, false),
(3, 'PORT001', 'PostgreSQL, Automation, and Data Modeling', 'Professional Learning', '2026-06-28', null, false);

insert into blog_posts (id, portfolio_id, slug, title, excerpt, published_at, read_time, body, deleted) values
(
    1,
    'PORT001',
    'why-clean-api-responses-matter',
    'Why Clean API Responses Matter',
    'A consistent response wrapper makes frontend clients easier to build, test, and maintain.',
    '2026-06-28',
    '4 min read',
    'Clean API responses reduce guesswork. When every endpoint returns a message, code, and data field, the frontend can handle loading, success, empty, and error states through one predictable contract.',
    false
);

insert into blog_tags (blog_post_id, sort_order, tag) values
(1, 0, 'Spring Boot'),
(1, 1, 'API Design'),
(1, 2, 'Clean Code');

insert into leadership_impact_items (
    id,
    portfolio_id,
    category,
    title,
    description,
    impact,
    metric_value,
    metric_label,
    display_order
) values
(
    1,
    'PORT001',
    'Engineering Philosophy',
    'My Engineering Philosophy',
    E'I believe software should solve real problems. Every system I build is designed with reliability, maintainability, automation, and measurable business impact in mind.\n\nMy goal is not simply to write code, but to create systems that reduce manual work, improve decision-making, and enable organizations to operate more efficiently.',
    '',
    '',
    '',
    1
),
(
    2,
    'PORT001',
    'Engineering Principle',
    'Impact First',
    '',
    '',
    '',
    '',
    2
),
(
    3,
    'PORT001',
    'Engineering Principle',
    'Automation Everywhere',
    '',
    '',
    '',
    '',
    3
),
(
    4,
    'PORT001',
    'Engineering Principle',
    'Quality Always',
    '',
    '',
    '',
    '',
    4
),
(
    5,
    'PORT001',
    'Engineering Principle',
    'People Centered',
    '',
    '',
    '',
    '',
    5
),
(
    6,
    'PORT001',
    'Impact Metric',
    'Systems Delivered',
    '',
    '',
    '20+',
    'Systems Delivered',
    6
),
(
    7,
    'PORT001',
    'Impact Metric',
    'Automation Workflows',
    '',
    '',
    '90+',
    'Automation Workflows',
    7
),
(
    8,
    'PORT001',
    'Impact Metric',
    'Transactions Monitored',
    '',
    '',
    'Millions',
    'Transactions Monitored',
    8
),
(
    9,
    'PORT001',
    'Impact Metric',
    'Systems In Production',
    '',
    '',
    '24/7',
    'Systems In Production',
    9
),
(
    10,
    'PORT001',
    'Introduction',
    'Leadership & Impact',
    'Throughout my academic, community, and professional journey, I have embraced leadership as an opportunity to serve, inspire, and create meaningful impact. From representing students in school to mentoring scholars and leading community initiatives, I have consistently sought to empower others while driving positive change.',
    '',
    '',
    '',
    10
),
(
    11,
    'PORT001',
    'Leadership Experience',
    'Pupil President',
    'Represented pupils, coordinated student activities, and served as a bridge between students and school administration.',
    '',
    '',
    'Primary School',
    11
),
(
    12,
    'PORT001',
    'Leadership Experience',
    'Vice Chairperson - Catholic Association',
    'Supported the planning and coordination of student activities, mentoring programs, and spiritual development initiatives.',
    '',
    '',
    'Kiaguthu Boys High School | Form 3',
    12
),
(
    13,
    'PORT001',
    'Leadership Experience',
    'Chairperson - Catholic Association',
    'Led the Catholic Association, coordinated meetings and events, mentored fellow students, and promoted teamwork, discipline, and servant leadership.',
    '',
    '',
    'Kiaguthu Boys High School | Form 4',
    13
),
(
    14,
    'PORT001',
    'Leadership Experience',
    'Youth Chairperson & Patron',
    'Led youth programs, organized community activities, mentored young members, and supported personal and spiritual development initiatives.',
    '',
    '',
    'Home Church',
    14
),
(
    15,
    'PORT001',
    'Mentorship',
    'Wings to Fly & Elimu Scholar Mentor',
    'Mentored 29 secondary school students from Nyandarua High School and Thome Boys High School under the Wings to Fly and Elimu scholarship programmes. Provided academic guidance, career mentorship, and personal development support, contributing to significant improvements in academic performance, with the 2024 cohort successfully attaining university entry qualifications.',
    'Continued volunteering beyond the official mentorship period to ensure sustained student support.',
    '2019-2025',
    'Mentorship period',
    20
),
(
    16,
    'PORT001',
    'Community Impact',
    'Students Mentored',
    '',
    '',
    '29+',
    'Students Mentored',
    30
),
(
    17,
    'PORT001',
    'Community Impact',
    'Customers Digitized',
    '',
    '',
    '4,000+',
    'Customers Digitized',
    31
),
(
    18,
    'PORT001',
    'Community Impact',
    'Software Projects',
    '',
    '',
    '20+',
    'Software Projects',
    32
),
(
    19,
    'PORT001',
    'Community Impact',
    'Automation Workflows',
    '',
    '',
    '90+',
    'Automation Workflows',
    33
),
(
    20,
    'PORT001',
    'Community Impact',
    'Enterprise Systems Delivered',
    '',
    '',
    '5',
    'Enterprise Systems Delivered',
    34
),
(
    21,
    'PORT001',
    'Leadership Philosophy',
    'Leadership Philosophy',
    'I believe leadership is about creating opportunities, empowering others, and using technology to solve real-world problems. My goal is to combine technical innovation with mentorship to contribute to Africa''s digital transformation while helping passionate individuals from all academic backgrounds build successful careers in technology.',
    '',
    '',
    '',
    40
),
(
    22,
    'PORT001',
    'Impact Metric',
    'Real Impact',
    '',
    '',
    'Real Impact',
    'Across Banking & Communities',
    9
),
(
    23,
    'PORT001',
    'Core Strength',
    'Problem Solving',
    'I break down complex problems and design practical solutions.',
    '',
    '',
    '',
    50
),
(
    24,
    'PORT001',
    'Core Strength',
    'Automation First',
    'I automate repetitive tasks and build systems that scale.',
    '',
    '',
    '',
    51
),
(
    25,
    'PORT001',
    'Core Strength',
    'AI-Driven Thinking',
    'I leverage AI to create intelligent, adaptive, and efficient systems.',
    '',
    '',
    '',
    52
),
(
    26,
    'PORT001',
    'Core Strength',
    'End-to-End Ownership',
    'From idea to deployment, I own the full development cycle.',
    '',
    '',
    '',
    53
),
(
    27,
    'PORT001',
    'Core Strength',
    'Impact Focused',
    'I measure success by the business outcomes I deliver.',
    '',
    '',
    '',
    54
),
(
    28,
    'PORT001',
    'Experience Approach',
    'My Approach',
    'I focus on building scalable systems, automating complex processes, and delivering measurable impact through clean architecture and intelligent workflows.',
    '',
    '',
    '',
    55
);

insert into leadership_impact_tags (leadership_impact_id, sort_order, tag) values
(1, 0, 'Reliability'),
(1, 1, 'Maintainability'),
(1, 2, 'Automation'),
(2, 0, 'Business impact'),
(3, 0, 'Workflow automation'),
(4, 0, 'Quality engineering'),
(5, 0, 'User outcomes'),
(11, 0, 'Student representation'),
(11, 1, 'School leadership'),
(12, 0, 'Mentorship'),
(12, 1, 'Spiritual development'),
(13, 0, 'Servant leadership'),
(13, 1, 'Teamwork'),
(14, 0, 'Youth programs'),
(14, 1, 'Community service'),
(15, 0, 'Wings to Fly'),
(15, 1, 'Elimu Scholarship'),
(15, 2, 'Career mentorship');

commit;
