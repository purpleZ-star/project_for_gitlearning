-- 城市空气质量监测与可视化系统数据库初始化脚本

CREATE DATABASE IF NOT EXISTS air_quality_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE air_quality_db;

-- 关键：设置连接字符集为 utf8mb4，解决 Windows 下中文乱码/截断问题
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection = utf8mb4;

-- 关闭外键检查，避免删除顺序导致的错误
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(64) NOT NULL COMMENT '密码(MD5加密)',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    role INT DEFAULT 0 COMMENT '角色: 0-普通用户 1-管理员',
    status INT DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户表';

-- ----------------------------
-- 城市表
-- ----------------------------
DROP TABLE IF EXISTS city;
CREATE TABLE city (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    city_name VARCHAR(50) NOT NULL COMMENT '城市名称',
    province VARCHAR(50) COMMENT '所属省份',
    longitude DOUBLE COMMENT '经度',
    latitude DOUBLE COMMENT '纬度',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='城市表';

-- ----------------------------
-- 监测站点表
-- ----------------------------
DROP TABLE IF EXISTS monitor_station;
CREATE TABLE monitor_station (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    station_name VARCHAR(100) NOT NULL COMMENT '站点名称',
    station_code VARCHAR(50) COMMENT '站点编码',
    city_id BIGINT COMMENT '所属城市ID',
    address VARCHAR(200) COMMENT '详细地址',
    longitude DOUBLE COMMENT '经度',
    latitude DOUBLE COMMENT '纬度',
    status INT DEFAULT 1 COMMENT '状态: 0-停用 1-运行中',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (city_id) REFERENCES city(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='监测站点表';

-- ----------------------------
-- 空气质量数据表
-- ----------------------------
DROP TABLE IF EXISTS air_quality_data;
CREATE TABLE air_quality_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    city_id BIGINT NOT NULL COMMENT '城市ID',
    aqi INT COMMENT 'AQI指数',
    quality_level VARCHAR(20) COMMENT '空气质量等级',
    primary_pollutant VARCHAR(20) COMMENT '首要污染物',
    pm25 DOUBLE COMMENT 'PM2.5浓度',
    pm10 DOUBLE COMMENT 'PM10浓度',
    so2 DOUBLE COMMENT 'SO2浓度',
    no2 DOUBLE COMMENT 'NO2浓度',
    co DOUBLE COMMENT 'CO浓度',
    o3 DOUBLE COMMENT 'O3浓度',
    temperature DOUBLE COMMENT '温度',
    humidity DOUBLE COMMENT '湿度',
    wind_speed DOUBLE COMMENT '风速',
    wind_direction VARCHAR(10) COMMENT '风向',
    monitor_time DATETIME NOT NULL COMMENT '监测时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_city_id (city_id),
    INDEX idx_monitor_time (monitor_time),
    INDEX idx_city_time (city_id, monitor_time),
    FOREIGN KEY (city_id) REFERENCES city(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='空气质量数据表';

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 初始化管理员用户 (密码: admin123 的MD5值)
-- ----------------------------
INSERT INTO sys_user (username, password, real_name, role, status) VALUES
('admin', 'f6fdffe48c908deb0f4c3bd36c032e72', '系统管理员', 1, 1),
('user1', 'f6fdffe48c908deb0f4c3bd36c032e72', '普通用户', 0, 1);

-- ----------------------------
-- 初始化城市数据
-- ----------------------------
INSERT INTO city (city_name, province, longitude, latitude) VALUES
('北京', '北京市', 116.4074, 39.9042),
('上海', '上海市', 121.4737, 31.2304),
('广州', '广东省', 113.2644, 23.1291),
('深圳', '广东省', 114.0579, 22.5431),
('成都', '四川省', 104.0665, 30.5723),
('杭州', '浙江省', 120.1551, 30.2741),
('武汉', '湖北省', 114.3054, 30.5931),
('南京', '江苏省', 118.7969, 32.0603),
('重庆', '重庆市', 106.5516, 29.5630),
('西安', '陕西省', 108.9402, 34.3416),
('天津', '天津市', 117.3616, 39.3434),
('苏州', '江苏省', 120.6194, 31.2990),
('郑州', '河南省', 113.6254, 34.7466),
('长沙', '湖南省', 112.9388, 28.2282),
('沈阳', '辽宁省', 123.4315, 41.8057),
('青岛', '山东省', 120.3826, 36.0671),
('大连', '辽宁省', 121.6147, 38.9140),
('厦门', '福建省', 118.0894, 24.4798),
('昆明', '云南省', 102.8329, 25.0389),
('哈尔滨', '黑龙江省', 126.5358, 45.8038);

-- ----------------------------
-- 初始化监测站点数据
-- ----------------------------
INSERT INTO monitor_station (station_name, station_code, city_id, address, longitude, latitude, status) VALUES
('北京奥体中心站', 'BJ001', 1, '北京市朝阳区奥体中心', 116.3975, 39.9825, 1),
('北京海淀万柳站', 'BJ002', 1, '北京市海淀区万柳', 116.2878, 39.9638, 1),
('上海浦东张江站', 'SH001', 2, '上海市浦东新区张江', 121.5908, 31.2039, 1),
('上海徐汇漕河泾站', 'SH002', 2, '上海市徐汇区漕河泾', 121.4107, 31.1688, 1),
('广州天河站', 'GZ001', 3, '广州市天河区', 113.3250, 23.1365, 1),
('深圳福田站', 'SZ001', 4, '深圳市福田区', 114.0589, 22.5221, 1),
('成都武侯站', 'CD001', 5, '成都市武侯区', 104.0435, 30.6428, 1),
('杭州西湖站', 'HZ001', 6, '杭州市西湖区', 120.1289, 30.2592, 1),
('武汉武昌站', 'WH001', 7, '武汉市武昌区', 114.3526, 30.5595, 1),
('南京玄武湖站', 'NJ001', 8, '南京市玄武区', 118.7949, 32.0759, 1);

-- ----------------------------
-- 初始化空气质量模拟数据(最近30天)
-- ----------------------------
DROP PROCEDURE IF EXISTS generate_air_quality_data;

DELIMITER //
CREATE PROCEDURE generate_air_quality_data()
BEGIN
    DECLARE v_city_id INT DEFAULT 1;
    DECLARE v_day INT DEFAULT 0;
    DECLARE v_hour INT DEFAULT 0;
    DECLARE v_aqi INT;
    DECLARE v_pm25 DOUBLE;
    DECLARE v_pm10 DOUBLE;
    DECLARE v_so2 DOUBLE;
    DECLARE v_no2 DOUBLE;
    DECLARE v_co DOUBLE;
    DECLARE v_o3 DOUBLE;
    DECLARE v_temp DOUBLE;
    DECLARE v_humidity DOUBLE;
    DECLARE v_wind_speed DOUBLE;
    DECLARE v_level VARCHAR(50) CHARACTER SET utf8mb4;
    DECLARE v_pollutant VARCHAR(20) CHARACTER SET utf8mb4;
    DECLARE v_wind_dir VARCHAR(20) CHARACTER SET utf8mb4;
    DECLARE v_monitor_time DATETIME;

    WHILE v_city_id <= 20 DO
        SET v_day = 0;
        WHILE v_day < 30 DO
            SET v_hour = 0;
            WHILE v_hour < 24 DO
                IF v_hour % 6 = 0 THEN
                    SET v_aqi = FLOOR(20 + RAND() * 280 * (1 + (v_city_id % 5) * 0.1));
                    IF v_aqi > 500 THEN SET v_aqi = 500; END IF;

                    SET v_pm25 = ROUND(v_aqi * 0.35 + RAND() * 30, 1);
                    SET v_pm10 = ROUND(v_pm25 * 1.5 + RAND() * 40, 1);
                    SET v_so2 = ROUND(5 + RAND() * 60, 1);
                    SET v_no2 = ROUND(10 + RAND() * 80, 1);
                    SET v_co = ROUND(0.3 + RAND() * 3, 2);
                    SET v_o3 = ROUND(20 + RAND() * 180, 1);
                    SET v_temp = ROUND(5 + RAND() * 25 - v_day * 0.2, 1);
                    SET v_humidity = ROUND(30 + RAND() * 60, 1);
                    SET v_wind_speed = ROUND(0.5 + RAND() * 8, 1);

                    IF v_aqi <= 50 THEN
                        SET v_level = '优';
                        SET v_pollutant = '-';
                    ELSEIF v_aqi <= 100 THEN
                        SET v_level = '良';
                        SET v_pollutant = 'PM2.5';
                    ELSEIF v_aqi <= 150 THEN
                        SET v_level = '轻度污染';
                        SET v_pollutant = 'PM2.5';
                    ELSEIF v_aqi <= 200 THEN
                        SET v_level = '中度污染';
                        SET v_pollutant = 'PM10';
                    ELSEIF v_aqi <= 300 THEN
                        SET v_level = '重度污染';
                        SET v_pollutant = 'PM2.5';
                    ELSE
                        SET v_level = '严重污染';
                        SET v_pollutant = 'PM2.5';
                    END IF;

                    SET v_wind_dir = ELT(FLOOR(1 + RAND() * 8), '北', '东北', '东', '东南', '南', '西南', '西', '西北');

                    SET v_monitor_time = DATE_SUB(NOW(), INTERVAL v_day DAY) + INTERVAL v_hour HOUR;

                    INSERT INTO air_quality_data (city_id, aqi, quality_level, primary_pollutant,
                        pm25, pm10, so2, no2, co, o3, temperature, humidity, wind_speed, wind_direction, monitor_time)
                    VALUES (v_city_id, v_aqi, v_level, v_pollutant,
                        v_pm25, v_pm10, v_so2, v_no2, v_co, v_o3, v_temp, v_humidity, v_wind_speed, v_wind_dir, v_monitor_time);
                END IF;

                SET v_hour = v_hour + 1;
            END WHILE;
            SET v_day = v_day + 1;
        END WHILE;
        SET v_city_id = v_city_id + 1;
    END WHILE;
END //
DELIMITER ;

CALL generate_air_quality_data();
DROP PROCEDURE IF EXISTS generate_air_quality_data;

SELECT CONCAT('init done, air_quality_data count = ', COUNT(*)) AS result FROM air_quality_data;
SELECT COUNT(*) AS city_count FROM city;
SELECT COUNT(*) AS user_count FROM sys_user;
