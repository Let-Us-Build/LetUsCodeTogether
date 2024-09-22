-- DROP database letuscodetogether;
CREATE DATABASE IF NOT EXISTS letuscodetogether;
use letuscodetogether;
CREATE TABLE users(
user_id INT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(255),
first_name VARCHAR(255),
last_name VARCHAR(255),
email VARCHAR(255),
passcode VARCHAR(255),
created_date timestamp DEFAULT current_timestamp 
);
CREATE TABLE platforms(
platform_id INT AUTO_INCREMENT PRIMARY KEY,
platform_name VARCHAR(255),
platform_url TEXT,
platform_status varchar(10) DEFAULT "Yes",
created_date timestamp DEFAULT current_timestamp
);
CREATE TABLE user_platform_mapping(
user_platform_id INT AUTO_INCREMENT PRIMARY KEY,
user_id INT,
platform_id INT,
username_on_platform VARCHAR(255),
created_date timestamp DEFAULT current_timestamp,
FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
FOREIGN KEY (platform_id) REFERENCES platforms(platform_id) ON DELETE CASCADE,
UNIQUE(user_id, platform_id)
);
CREATE TABLE score(
score_id  INT AUTO_INCREMENT PRIMARY KEY,
user_id INT,
platform_id INT,
no_of_problems_solved INT DEFAULT 0,
no_of_contests INT DEFAULT 0,
ratings INT DEFAULT 0,
points DOUBLE DEFAULT 0,
calculated_total_score DOUBLE DEFAULT 0,
created_date timestamp DEFAULT current_timestamp,
FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
FOREIGN KEY (platform_id) REFERENCES platforms(platform_id) ON DELETE CASCADE,
UNIQUE(user_id, platform_id)
);
CREATE TABLE overall_ranking(
overall_rank_id INT AUTO_INCREMENT PRIMARY KEY,
user_id  INT,
total_score INT DEFAULT 0,
ranking INT,
created_date timestamp DEFAULT current_timestamp,
FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
CREATE TABLE daily_activity(
activity_id INT AUTO_INCREMENT PRIMARY KEY,
user_id INT,
platform_id INT,
problems_solved INT DEFAULT 0,
contests_participated INT DEFAULT 0,
ratings INT DEFAULT 0,
points DOUBLE DEFAULT 0,
calculated_total_score DOUBLE DEFAULT 0,
previous_score DOUBLE DEFAULT 0,
score_difference DOUBLE DEFAULT 0,
streak_in_days INT DEFAULT 0,
overall_streak_in_days INT DEFAULT 0,
created_date timestamp DEFAULT current_timestamp,
created_by INT,
FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE CASCADE,
FOREIGN KEY (platform_id) REFERENCES platforms(platform_id) ON DELETE CASCADE
);