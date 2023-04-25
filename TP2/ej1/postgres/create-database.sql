-- Create the database
CREATE DATABASE archivos;

-- Connect to the database
\c archivos

-- Create the usuario table
CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    ip VARCHAR(255) NOT NULL,
    puerto INTEGER NOT NULL
);

-- Create the archivo table
CREATE TABLE archivo (
    id_usuario INTEGER NOT NULL REFERENCES usuario(id_usuario),
    nombre_archivo VARCHAR(255) NOT NULL,
    PRIMARY KEY (id_usuario, nombre_archivo)
);





