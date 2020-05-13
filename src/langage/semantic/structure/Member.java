package langage.semantic.structure;



public class Member {

    private String identifier;
    private Type type;
    private boolean isForeign;
    private boolean isPrimary;

    public Member(Type type, String identifier, boolean isPrimary) {
        this.identifier = identifier;
        this.type = type;
        this.isForeign = type.getType() == TypeEnum.CLASS;
        this.isPrimary = isPrimary;
    }

    public Type getType() {
        return type;
    }

    public Class getClassType(){
        return type.getClassType();
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isForeign(){
        return this.isForeign;
    }

    public boolean isPrimary(){
        return this.isPrimary;
    }

    public int getSize() {
        return type.getSize();
    }

    public String toSql(){
        StringBuilder sql = new StringBuilder();
        sql.append(identifier);
        sql.append(" ");
        sql.append(type.toSql());
        if(isPrimary || isForeign){
            sql.append(" NOT NULL");
            if(type.getType() == TypeEnum.AUTO){
                sql.append(" AUTO_INCREMENT");
            }
        }
        return sql.toString();
    }

    public String toJava(){
        StringBuilder java = new StringBuilder();
        java.append(type.toJava());
        java.append(" ");
        java.append(identifier);
        return java.toString();
    }

    public String toJdbc(int count, Type.JDBC jdbcMode){
        StringBuilder jdbc = new StringBuilder();
        TypeEnum recursiveType = type.getRecursiveType();
        if(jdbcMode == Type.JDBC.GET){ // ResutltSet.get
            jdbc.append("get").append(type.toJdbc()).append("(\"").append(identifier).append("\")");
            if(recursiveType == TypeEnum.CHAR){
                jdbc.append(".charAt(0)");
            }
        }
        else { // PreparedStatement.set
            String capitalizedIdentifier = String.valueOf(identifier.charAt(0)).toUpperCase() + identifier.substring(1);
            jdbc.append("set").append(type.toJdbc()).append("(").append(count).append(", ");
            if(recursiveType == TypeEnum.DATE){
                jdbc.append("new java.sql.Date(");
            } else if(recursiveType == TypeEnum.TIMESTAMP) {
                jdbc.append("new Timestamp(");
            } else if(recursiveType == TypeEnum.CHAR) {
                jdbc.append("String.valueOf(");
            }
            jdbc.append("<lowerclass>.get")
                    .append(capitalizedIdentifier).append("()");
            if(recursiveType == TypeEnum.DATE || recursiveType == TypeEnum.TIMESTAMP){
                jdbc.append(".getTime())");
            } else if(recursiveType == TypeEnum.CHAR) {
                jdbc.append(")");
            }
            jdbc.append(")");
        }
        return jdbc.toString();
    }
}
