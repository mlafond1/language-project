package langage.semantic;

import langage.grammar.node.Token;

public class SemanticException extends RuntimeException {

    public SemanticException(String message) {
        super(message);
    }

    public SemanticException(String message, Throwable cause) {
        super(message, cause);
    }

    public SemanticException(String message, Token token) {
        super(message + " at line " + token.getLine() + " position " + token.getPos());
    }

    public SemanticException(String message, Token token, Throwable cause) {
        super(message + " at line " + token.getLine() + " position " + token.getPos(), cause);
    }

}
