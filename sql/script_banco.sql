CREATE DATABASE IF NOT EXISTS db_agente;
USE db_agente;

CREATE TABLE historico (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           temperatura DECIMAL(5,2) NOT NULL,
                           acao_tomada VARCHAR(50) NOT NULL,
                           data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);