drop table if exists `user`;
create table `user`
(
    `id`         int(11) not null auto_increment,
    `username`   varchar(24) default null,
    `password`   varchar(16) default null,
    `email`      varchar(64) default null,
    `phone`      varchar(16) default null,
    `sid`        varchar(24) default null,
    `remindType` int(2)      default 0,
    `openId`     varchar(64) default null,
    primary key (`id`) using btree
) engine = InnoDB
  default charset = utf8;

INSERT INTO remind.user (id, username, password, email, phone, sid, remindType, openId)
VALUES (1, '敖鸥', 'hg5200820', '371575373@qq.com', '13752849314', '20222104066', 1, 'o3wY26G50Db3ShQFLf0VFYcnwgtQ');
INSERT INTO remind.user (id, username, password, email, phone, sid, remindType, openId)
VALUES (3, '艾俊辰', 'hg5200820', '545430362@qq.com', '13752849314', '20222104066', 1, 'o3wY26DFPjuvlEViOiSq5onuFEjI');
INSERT INTO remind.user (id, username, password, email, phone, sid, remindType, openId)
VALUES (4, '敖鸥', 'hg5200820', '2771751687@qq.com', '13752849314', '20222104066', 1, 'oMpP15gGpjSd4qEy9ETCSDaYIUCM');


drop table if exists `course`;
create table `course`
(
    `id`         int(11) not null auto_increment,
    `username`   varchar(24) default null,
    `courseName` varchar(64) default null,
    `location`   varchar(24) default null,
    `teacher`    varchar(64) default null,
    `startAt`    date,
    `total`      int(11),
    `time`       varchar(46),
    primary key (`id`) using btree
) engine = InnoDB
  default charset = utf8;

insert into `course`
values (1, '敖鸥', '算法分析与设计', '公教楼-450', '范洪博', '2022-08-29', 18, '19:00-21:25');
insert into `course`
values (2, '敖鸥', '数据仓库与数据挖掘', '公教楼-252', '游进国', '2022-08-30', 12, '19:00-21:25');
insert into `course`
values (3, '敖鸥', '现代计算机体系结构', '公教楼-136', '王海瑞', '2022-09-03', 9, '8:00-11:25');
insert into `course`
values (4, '敖鸥', '学科前沿讲座及研究方法', '公教楼-129', '姜瑛,毛存礼,付晓东', '2022-09-01', 6, '15:10-17:45');
insert into `course`
values (5, '敖鸥', '人工智能', '公教楼-450', '张云', '2022-09-02', 18, '8:00-9:35');
insert into `course`
values (6, '敖鸥', '专业英语', '信自楼-502', '张晶', '2022-09-02', 12, '16:10-17:45');
insert into `course`
values (7, '敖鸥', '综合英语', '公教楼-247', '闫锋', '2022-08-30', 18, '13:30-15:05');
insert into `course`
values (8, '敖鸥', '英语听说', '公教楼-132', '罗哲', '2022-09-12', 8, '13:30-15:05');
insert into `course`
values (9, '敖鸥', '新时代中国特色社会主义理论与实践', '公教楼-350', '周雅难', '2022-09-02', 18, '13:30-15:05');

insert into `course`
values (0, '艾俊辰', '算法分析与设计', '公教楼-450', '范洪博', '2022-08-29', 18, '19:00-21:25');
insert into `course`
values (0, '艾俊辰', '数据仓库与数据挖掘', '公教楼-252', '游进国', '2022-08-30', 12, '19:00-21:25');
insert into `course`
values (0, '艾俊辰', '现代计算机体系结构', '公教楼-136', '王海瑞', '2022-09-03', 9, '8:00-11:25');
insert into `course`
values (0, '艾俊辰', '学科前沿讲座及研究方法', '公教楼-129', '姜瑛,毛存礼,付晓东', '2022-09-01', 6, '15:10-17:45');
insert into `course`
values (0, '艾俊辰', '人工智能', '公教楼-450', '张云', '2022-09-02', 18, '8:00-9:35');
insert into `course`
values (0, '艾俊辰', '专业英语', '信自楼-502', '张晶', '2022-09-02', 12, '16:10-17:45');
insert into `course`
values (0, '艾俊辰', '综合英语', '公教楼-247', '闫锋', '2022-08-30', 18, '13:30-15:05');
insert into `course`
values (0, '艾俊辰', '英语听说', '公教楼-132', '罗哲', '2022-09-12', 8, '13:30-15:05');
insert into `course`
values (0, '艾俊辰', '新时代中国特色社会主义理论与实践', '公教楼-350', '周雅难', '2022-09-02', 18, '13:30-15:05');