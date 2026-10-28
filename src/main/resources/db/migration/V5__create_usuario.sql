
-- ============================================
-- V5 - CRIACAO DA TABELA DE USUARIOS
-- Projeto EcoEnergia - FIAP
-- Autenticacao HTTP Basic com Oracle
-- ============================================

-- 1. TABELA DE USUARIOS
-- Armazena os usuarios autorizados a acessar a API.

CREATE TABLE tb_esg_usuario (
                                nm_usuario       VARCHAR2(80) NOT NULL,
                                ds_senha_hash    VARCHAR2(255) NOT NULL,
                                st_ativo         CHAR(1) DEFAULT 'S' NOT NULL
);


-- ============================================
-- 2. CHAVE PRIMARIA
-- ============================================

-- O nome do usuario deve ser unico.
-- Nao permite dois usuarios com o mesmo login.

ALTER TABLE tb_esg_usuario
    ADD CONSTRAINT pk_tb_esg_usuario
        PRIMARY KEY (nm_usuario);


-- ============================================
-- 3. RESTRICAO DE STATUS
-- ============================================

-- S = Usuario ativo
-- N = Usuario inativo

ALTER TABLE tb_esg_usuario
    ADD CONSTRAINT ck_tb_esg_usuario_ativo
        CHECK (st_ativo IN ('S', 'N'));


-- ============================================
-- 4. COMENTARIOS DA TABELA
-- ============================================

COMMENT ON TABLE tb_esg_usuario IS
'Usuarios autorizados a acessar a API EcoEnergia.';

COMMENT ON COLUMN tb_esg_usuario.nm_usuario IS
'Nome unico utilizado na autenticacao HTTP Basic.';

COMMENT ON COLUMN tb_esg_usuario.ds_senha_hash IS
'Hash seguro da senha do usuario.';

COMMENT ON COLUMN tb_esg_usuario.st_ativo IS
'Status do usuario: S=Ativo, N=Inativo.';
