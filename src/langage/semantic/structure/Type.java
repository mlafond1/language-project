package langage.semantic.structure;

import langage.semantic.SemanticException;

public class Type {

    public enum JDBC{ SET, GET };

    private Class classType;
    private TypeEnum type;
    private int size;

    public Type(TypeEnum type, int size){
        this.type = type;
        this.size = size;
    }

    public Type(Class classType, TypeEnum type, int size){
        this.classType = classType;
        this.type = type;
        this.size = size;
    }

    public Class getClassType() {
        return classType;
    }

    public TypeEnum getRecursiveType(){
        Type result = this;
        int count = 0;
        while (result.type == TypeEnum.CLASS) {
            result = result.classType.getUniquePrimaryKey().getType();
            if(++count > 128) // Il ne devrait jamais y avoir un lien de dépendance de 128 de profondeur..
                throw new SemanticException("Cycle class dependencies for " + this.classType.getIdentifier());
        }
        return result.type;
    }

    public TypeEnum getType(){
        return this.type;
    }

    public int getRecursiveSize(){
        Type result = this;
        int count = 0;
        while (result.type == TypeEnum.CLASS) {
            result = result.classType.getUniquePrimaryKey().getType();
            if(++count > 128) // Il ne devrait jamais y avoir un lien de dépendance de 128 de profondeur..
                throw new SemanticException("Cycle class dependencies for " + this.classType.getIdentifier());
        }
        return result.size;
    }

    public int getSize() {
        return size;
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        TypeEnum currentType = getRecursiveType();
        int currentSize = getRecursiveSize();
        switch (currentType){
            case AUTO:
            case INT: sql.append("int"); break;
            case FLOAT: sql.append("float(24)"); break;
            case DOUBLE: sql.append("float(53)"); break;
            case STRING: sql.append("varchar"); break;
            case BOOL: sql.append("bool"); break;
            case CHAR: sql.append("char"); break;
            case DATE: sql.append("date"); break;
            case TIMESTAMP: sql.append("timestamp"); break;
        }
        if(currentSize>0){
            sql.append("(");
            sql.append(currentSize);
            sql.append(")");
        }
        return sql.toString();
    }

    public String toJava() {
        StringBuilder java = new StringBuilder();
        TypeEnum currentType = getRecursiveType();
        switch (currentType){
            case AUTO:
            case INT: java.append("int"); break;
            case FLOAT: java.append("float"); break;
            case DOUBLE: java.append("double"); break;
            case STRING: java.append("String"); break;
            case BOOL: java.append("boolean"); break;
            case CHAR: java.append("char"); break;
            case DATE: java.append("Date"); break;
            case TIMESTAMP: java.append("Date"); break;
        }
        return java.toString();
    }

    public String toJdbc() {
        StringBuilder jdbc = new StringBuilder();
        TypeEnum currentType = getRecursiveType();
        switch (currentType){
            case AUTO:
            case INT: jdbc.append("Int"); break;
            case FLOAT: jdbc.append("Float"); break;
            case DOUBLE: jdbc.append("Double"); break;
            case STRING: jdbc.append("String"); break;
            case BOOL: jdbc.append("Boolean"); break;
            case CHAR: jdbc.append("String"); break;
            case DATE: jdbc.append("Date"); break;
            case TIMESTAMP: jdbc.append("Timestamp"); break;
        }
        return jdbc.toString();
    }
}