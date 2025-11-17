CREATE DATABASE db_produtos;

CREATE TABLE IF NOT EXISTS db_produtos.tb_produtodao (
  id INT NOT NULL AUTO_INCREMENT,
  produto VARCHAR(100) NOT NULL,
  preco DOUBLE NOT NULL,
  unidade VARCHAR(50) NOT NULL,
  categoria VARCHAR(50) NOT NULL,
  quantidade INT NOT NULL,
  estoqueminimo INT NOT NULL DEFAULT 25,
  estoquemaximo INT NOT NULL DEFAULT 100,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS db_produtos.tb_categoriadao (
  id INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  embalagem ENUM('plastico', 'lata', 'vidro') NOT NULL,
  tamanho ENUM('pequeno', 'medio', 'grande') NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS db_produtos.tb_movimentacao (
  id INT PRIMARY KEY AUTO_INCREMENT,
  id_produto INT NOT NULL,
  quantidade INT NOT NULL,
  tipo ENUM('ENTRADA', 'SAIDA') NOT NULL,
  data_hora DATETIME NOT NULL,
  observacao VARCHAR(255)
);
