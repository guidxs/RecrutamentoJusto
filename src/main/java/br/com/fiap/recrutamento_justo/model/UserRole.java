package br.com.fiap.recrutamento_justo.model;

/**
 * Enum que define os perfis de usuário no sistema.
 * CANDIDATO: Pessoa que se candidata às vagas.
 * RH: Recrutador que gerencia vagas e avalia candidatos.
 * ADMIN: Administrador do sistema.
 */
public enum UserRole {
    CANDIDATO("ROLE_CANDIDATO"),
    RH("ROLE_RH"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

