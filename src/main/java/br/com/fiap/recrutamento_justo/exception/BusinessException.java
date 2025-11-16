package br.com.fiap.recrutamento_justo.exception;

/**
 * Exceção lançada quando há conflito de negócio (ex: candidato já aplicou para vaga).
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

