package br.com.fiap.recrutamento_justo.exception;

/**
 * Exceção lançada quando há erro de autenticação/autorização.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

