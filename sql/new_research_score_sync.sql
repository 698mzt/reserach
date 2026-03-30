/*
哈尔滨信息工程学院新论文科研分标准同步脚本
根据字典配置调整数据库对应关系
日期: 2026-03-29
*/

-- 清空现有数据
TRUNCATE TABLE `sci_paper_cfg`;

-- SCI分区I区 (字典值: 1)
INSERT INTO `sci_paper_cfg` VALUES (1, '1', '1', 2000);
INSERT INTO `sci_paper_cfg` VALUES (2, '1', '2', 760);
INSERT INTO `sci_paper_cfg` VALUES (3, '1', '3', 400);
INSERT INTO `sci_paper_cfg` VALUES (4, '1', '4', 200);

-- SCI分区II区 (字典值: 2)
INSERT INTO `sci_paper_cfg` VALUES (5, '2', '1', 1000);
INSERT INTO `sci_paper_cfg` VALUES (6, '2', '2', 380);
INSERT INTO `sci_paper_cfg` VALUES (7, '2', '3', 200);
INSERT INTO `sci_paper_cfg` VALUES (8, '2', '4', 100);

-- SCI分区III区 (字典值: 3)
INSERT INTO `sci_paper_cfg` VALUES (9, '3', '1', 800);
INSERT INTO `sci_paper_cfg` VALUES (10, '3', '2', 320);
INSERT INTO `sci_paper_cfg` VALUES (11, '3', '3', 160);
INSERT INTO `sci_paper_cfg` VALUES (12, '3', '4', 80);

-- SCI分区IV区 (字典值: 4)
INSERT INTO `sci_paper_cfg` VALUES (13, '4', '1', 480);
INSERT INTO `sci_paper_cfg` VALUES (14, '4', '2', 200);
INSERT INTO `sci_paper_cfg` VALUES (15, '4', '3', 100);
INSERT INTO `sci_paper_cfg` VALUES (16, '4', '4', 50);

-- EI期刊论文 (字典值: 5)
INSERT INTO `sci_paper_cfg` VALUES (17, '5', '1', 400);
INSERT INTO `sci_paper_cfg` VALUES (18, '5', '2', 180);
INSERT INTO `sci_paper_cfg` VALUES (19, '5', '3', 80);
INSERT INTO `sci_paper_cfg` VALUES (20, '5', '4', 40);

-- 核心期刊文章（北大、南大核心；社科类） (字典值: 6)
INSERT INTO `sci_paper_cfg` VALUES (21, '6', '1', 280);
INSERT INTO `sci_paper_cfg` VALUES (22, '6', '2', 120);
INSERT INTO `sci_paper_cfg` VALUES (23, '6', '3', 60);
INSERT INTO `sci_paper_cfg` VALUES (24, '6', '4', 30);

-- 三网收录论文 (字典值: 7)
INSERT INTO `sci_paper_cfg` VALUES (25, '7', '1', 200);
INSERT INTO `sci_paper_cfg` VALUES (26, '7', '2', 80);
INSERT INTO `sci_paper_cfg` VALUES (27, '7', '3', 0);
INSERT INTO `sci_paper_cfg` VALUES (28, '7', '4', 0);

-- 普通期刊 (字典值: 8)
INSERT INTO `sci_paper_cfg` VALUES (29, '8', '1', 50);
INSERT INTO `sci_paper_cfg` VALUES (30, '8', '2', 0);
INSERT INTO `sci_paper_cfg` VALUES (31, '8', '3', 0);
INSERT INTO `sci_paper_cfg` VALUES (32, '8', '4', 0);

-- 校办期刊 (字典值: 9)
INSERT INTO `sci_paper_cfg` VALUES (33, '9', '1', 10);
INSERT INTO `sci_paper_cfg` VALUES (34, '9', '2', 0);
INSERT INTO `sci_paper_cfg` VALUES (35, '9', '3', 0);
INSERT INTO `sci_paper_cfg` VALUES (36, '9', '4', 0);

-- 知网收录论文 (字典值: 10)
INSERT INTO `sci_paper_cfg` VALUES (37, '10', '1', 200);
INSERT INTO `sci_paper_cfg` VALUES (38, '10', '2', 80);
INSERT INTO `sci_paper_cfg` VALUES (39, '10', '3', 0);
INSERT INTO `sci_paper_cfg` VALUES (40, '10', '4', 0);

-- 维普收录论文 (字典值: 11)
INSERT INTO `sci_paper_cfg` VALUES (41, '11', '1', 200);
INSERT INTO `sci_paper_cfg` VALUES (42, '11', '2', 80);
INSERT INTO `sci_paper_cfg` VALUES (43, '11', '3', 0);
INSERT INTO `sci_paper_cfg` VALUES (44, '11', '4', 0);

-- 万方收录论文 (字典值: 12)
INSERT INTO `sci_paper_cfg` VALUES (45, '12', '1', 200);
INSERT INTO `sci_paper_cfg` VALUES (46, '12', '2', 80);
INSERT INTO `sci_paper_cfg` VALUES (47, '12', '3', 0);
INSERT INTO `sci_paper_cfg` VALUES (48, '12', '4', 0);

-- 验证数据
SELECT * FROM `sci_paper_cfg` ORDER BY `order`, `user_order`;