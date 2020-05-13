package langage.semantic.generator;

import langage.grammar.analysis.DepthFirstAdapter;
import langage.grammar.node.*;
import langage.semantic.structure.Class;
import langage.semantic.structure.ClassTable;
import langage.semantic.structure.Member;
import langage.semantic.structure.Type;
import langage.semantic.structure.TypeEnum;

import java.util.HashMap;
import java.util.Map;

public class DaoGenerator extends DepthFirstAdapter {

    private Map<String, String> classes;
    private ClassTable classTable;

    private Class currentClass;
    private String currentClassTemplate;
    private String packageName;
    private String dtoPackageName;

    private StringBuilder currentImports;

    private StringBuilder currentGetQuery;
    private StringBuilder currentGetAllQuery;
    private StringBuilder currentInsertQuery;
    private StringBuilder currentInsertValues;
    private StringBuilder currentUpdateQuery;
    private StringBuilder currentDeleteQuery;
    private StringBuilder currentWhereClause;

    private StringBuilder currentGet;
    private StringBuilder currentInsert;
    private StringBuilder currentUpdate;
    private StringBuilder currentDelete;

    private StringBuilder currentExtractFromResultSet;

    private int nbKeys;
    private int nbMembers;
    private int currentCount;
    private int currentInsertCount;
    private int currentNormalCount;
    private int currentKeyCount;

    private final static String template = "" +
            "package <package>;\n\n" +
            "import <dtopackage>.<class>DTO;\n" +
            "import <package>.exception.DAOException;\n" +
            "import java.util.Collection;\n" +
            "import java.util.HashSet;\n" +
            "import java.sql.SQLException;\n" +
            "import java.sql.Connection;\n" +
            "import java.sql.PreparedStatement;\n" +
            "import java.sql.ResultSet;\n" +
            "<imports>\n" +
            "public class <class>DAO {\n\n" +
            "    private static final String getQuery = \"<getQuery>\";\n" +
            "    private static final String getAllQuery = \"<getAllQuery>\";\n" +
            "    private static final String insertQuery = \"<insertQuery>\";\n" +
            "    private static final String updateQuery = \"<updateQuery>\";\n" +
            "    private static final String deleteQuery = \"<deleteQuery>\";\n\n" +
            "    public <class>DAO(){}\n\n" +
            "    public <class>DTO get(Connection connection, <class>DTO <lowerclass>){\n" +
            "        <class>DTO result = null;\n" +
            "        try(PreparedStatement statement = connection.prepareStatement(getQuery)){\n<getSettings>" +
            "            ResultSet resultSet = statement.executeQuery();\n" +
            "            if(resultSet.next()) result = extractFromResultSet(resultSet);\n" +
            "        } catch(SQLException exception){\n" +
            "            throw new DAOException(exception);\n" +
            "        }\n" +
            "        return result;\n" +
            "    }\n\n" +
            "    public Collection<<class>DTO> getAll(Connection connection){\n" +
            "        Collection<<class>DTO> result = new HashSet<<class>DTO>();\n" + // 1TODO bien choisir structure de donnée?
            "        try(PreparedStatement statement = connection.prepareStatement(getAllQuery)){\n" +
            "            ResultSet resultSet = statement.executeQuery();\n" +
            "            while(resultSet.next()) result.add(extractFromResultSet(resultSet));\n" +
            "        } catch(SQLException exception){\n" +
            "            throw new DAOException(exception);\n" +
            "        }\n" +
            "        return result;\n" +
            "    }\n\n" +
            "    public boolean insert(Connection connection, <class>DTO <lowerclass>){\n" +
            "        boolean success = false;\n" +
            "        try(PreparedStatement statement = connection.prepareStatement(insertQuery)){\n<insertSettings>" +
            "            int nbRowAdded = statement.executeUpdate();\n" +
            "            if(nbRowAdded == 1) success = true;\n" +
            "        } catch(SQLException exception){\n" +
            "            throw new DAOException(exception);\n" +
            "        }\n" +
            "        return success;\n" +
            "    }\n\n" +
            "    public boolean update(Connection connection, <class>DTO <lowerclass>){\n" +
            "        boolean success = false;\n" +
            "        try(PreparedStatement statement = connection.prepareStatement(updateQuery)){\n<updateSettings>" +
            "            int nbRowUpdated = statement.executeUpdate();\n" +
            "            if(nbRowUpdated == 1) success = true;\n" + // 1TODO quoi faire si plus d'une ligne?
            "        } catch(SQLException exception){\n" +
            "            throw new DAOException(exception);\n" +
            "        }\n" +
            "        return success;\n" +
            "    }\n\n" +
            "    public boolean delete(Connection connection, <class>DTO <lowerclass>){\n" +
            "        boolean success = false;\n" +
            "        try(PreparedStatement statement = connection.prepareStatement(deleteQuery)){\n<deleteSettings>" +
            "            int nbRowDeleted = statement.executeUpdate();\n" +
            "            if(nbRowDeleted == 1) success = true;\n" + // 1TODO quoi faire si plus d'une ligne?
            "        } catch(SQLException exception){\n" +
            "            throw new DAOException(exception);\n" +
            "        }\n" +
            "        return success;\n" +
            "    }\n\n" +
            "    private <class>DTO extractFromResultSet(ResultSet resultSet) throws SQLException {\n" +
            "        <class>DTO <lowerclass> = new <class>DTO();\n<extractSettings>" +
            "        return <lowerclass>;\n" +
            "    }\n" +
            "}\n\n";


    private DaoGenerator(ClassTable classTable){
        this.classes = new HashMap<>();
        this.classTable = classTable;
    }

    public static Map<String, String> generate(Node node, ClassTable classTable){
        DaoGenerator generator = new DaoGenerator(classTable);
        node.apply(generator);
        return generator.classes;
    }

    @Override
    public void outAProgram(AProgram node) {
        final String content =
                "package "+packageName+".exception;\n\n" +
                "public class DAOException extends RuntimeException {\n\n" +
                "    public DAOException(String message) {\n" +
                "        super(message);\n" +
                "    }\n\n" +
                "    public DAOException(String message, Throwable cause) {\n" +
                "        super(message, cause);\n" +
                "    }\n\n" +
                "    public DAOException(Throwable cause) {\n" +
                "        super(cause);\n" +
                "    }\n\n" +
                "}\n\n";
        classes.put(packageName.replaceAll("\\.","/") + "/exception/DAOException.java", content);
    }

    @Override
    public void inASimplePackage(ASimplePackage node) {
        packageName = node.getIdentifier().getText() + ".dao";
        dtoPackageName = node.getIdentifier().getText() + ".dto";
    }

    @Override
    public void inAComposedPackage(AComposedPackage node) {
        packageName = node.getIdentifier().getText() + ".dao";
        dtoPackageName = node.getIdentifier().getText() + ".dto";
    }

    @Override
    public void inAClassDeclaration(AClassDeclaration node) {
        if(packageName == null) packageName = "dao";
        if(dtoPackageName == null) dtoPackageName = "dto";
        this.currentClass = classTable.get(node.getIdentifier());

        this.currentClassTemplate = template;
        this.currentImports = new StringBuilder();

        this.currentGetQuery = new StringBuilder();
        this.currentGetAllQuery = new StringBuilder();
        this.currentInsertQuery = new StringBuilder();
        this.currentInsertValues = new StringBuilder();
        this.currentUpdateQuery = new StringBuilder();
        this.currentDeleteQuery = new StringBuilder();

        this.currentWhereClause = new StringBuilder();

        this.currentGet = new StringBuilder();
        this.currentInsert = new StringBuilder();
        this.currentUpdate = new StringBuilder();
        this.currentDelete = new StringBuilder();

        this.currentExtractFromResultSet = new StringBuilder();

        this.nbKeys = currentClass.getPrimaryKeys().size();
        this.nbMembers = currentClass.getMembers().size();
        this.currentCount = 0;
        this.currentNormalCount = 0;
        this.currentKeyCount = 0;
        this.currentInsertCount = 0;

        // TODO séparer
        this.currentGetQuery.append("SELECT ");
        this.currentGetAllQuery.append("SELECT ");
        this.currentInsertQuery.append("INSERT INTO `").append(currentClass.getSqlIdentifier()).append("` (");
        this.currentInsertValues.append(") VALUES (");
        this.currentUpdateQuery.append("UPDATE `").append(currentClass.getSqlIdentifier()).append("` SET ");
        this.currentDeleteQuery.append("DELETE FROM `").append(currentClass.getSqlIdentifier()).append("` <whereClause>");
        this.currentWhereClause.append("WHERE ");

    }

    @Override
    public void inAMemberClassMember(AMemberClassMember node) {
        String identifier = node.getIdentifier().getText();
        Member member = currentClass.getMember(identifier);

        handleMember(member);

        ++currentNormalCount;
        if(currentNormalCount == 1){
            this.currentUpdateQuery.append(String.format("%s=?", identifier));
        }
        else {
            this.currentUpdateQuery.append(String.format(", %s=?", identifier));
        }
        currentUpdate.append(String.format("            statement.%s;\n", member.toJdbc(currentNormalCount, Type.JDBC.SET)));

    }

    @Override
    public void inAKeyMemberClassMember(AKeyMemberClassMember node) {
        String identifier = node.getIdentifier().getText();
        Member member = currentClass.getMember(identifier);

        handleMember(member);

        ++currentKeyCount;
        if(currentKeyCount == 1){
            this.currentWhereClause.append(String.format("%s=?", identifier));
        }
        else {
            this.currentWhereClause.append(String.format(" And %s=?", identifier));
        }
        currentGet.append(String.format("            statement.%s;\n", member.toJdbc(currentKeyCount, Type.JDBC.SET)));
        currentUpdate.append(String.format("            statement.%s;\n", member.toJdbc(nbMembers-nbKeys+currentKeyCount, Type.JDBC.SET)));
        currentDelete.append(String.format("            statement.%s;\n", member.toJdbc(currentKeyCount, Type.JDBC.SET)));
    }

    private void handleMember(Member member){
        String identifier = member.getIdentifier();
        Type type = member.getType();
        String capitalizedIdentifier = String.valueOf(identifier.charAt(0)).toUpperCase() + identifier.substring(1);

        if(currentImports.length() == 0 && (type.getRecursiveType() == TypeEnum.DATE || type.getRecursiveType() == TypeEnum.TIMESTAMP)){
            currentImports.append("import java.util.Date;\n"); // TODO compare dates?
        }
        if(currentImports.indexOf("Timestamp") == -1 && type.getRecursiveType() == TypeEnum.TIMESTAMP){
            currentImports.append("import java.sql.Timestamp;\n");
        }
        currentInsertCount += type.getType() == TypeEnum.AUTO ? 0 : 1;
        ++currentCount;
        if(currentCount == 1){
            currentGetQuery.append(identifier);
            currentGetAllQuery.append(identifier);
            currentInsertQuery.append(identifier);
            currentInsertValues.append(type.getType() == TypeEnum.AUTO ? "NULL" : "?");
        }
        else {
            currentGetQuery.append(String.format(", %s", identifier));
            currentGetAllQuery.append(String.format(", %s", identifier));
            currentInsertQuery.append(String.format(", %s", identifier));
            currentInsertValues.append(type.getType() == TypeEnum.AUTO ? ", NULL" : ", ?");
        }

        if(type.getType() != TypeEnum.AUTO)
            currentInsert.append(String.format("            statement.%s;\n", member.toJdbc(currentInsertCount, Type.JDBC.SET)));
        if(type.getRecursiveType() == TypeEnum.DATE || type.getRecursiveType() == TypeEnum.TIMESTAMP)
            currentExtractFromResultSet.append(String.format("        <lowerclass>.set%s(new Date(resultSet.%s.getTime()));\n", capitalizedIdentifier, member.toJdbc(0, Type.JDBC.GET)));
        else
            currentExtractFromResultSet.append(String.format("        <lowerclass>.set%s(resultSet.%s);\n", capitalizedIdentifier, member.toJdbc(0, Type.JDBC.GET)));

    }

    @Override
    public  void outAClassDeclaration(AClassDeclaration node) {
        String identifier = currentClass.getIdentifier();
        String lowerIdentifier = String.valueOf(identifier.charAt(0)).toLowerCase() + identifier.substring(1);

        this.currentGetQuery.append(" FROM `").append(currentClass.getSqlIdentifier()).append("` <whereClause>");
        this.currentGetAllQuery.append(" FROM `").append(currentClass.getSqlIdentifier()).append("`");
        this.currentInsertValues.append(")");
        this.currentInsertQuery.append(currentInsertValues);
        this.currentUpdateQuery.append(" <whereClause>");

        // Remplace tous les placeholder
        currentClassTemplate = currentClassTemplate
                .replace("<getQuery>", currentGetQuery).replace("<getAllQuery>", currentGetAllQuery)
                .replace("<insertQuery>", currentInsertQuery).replace("<updateQuery>", currentUpdateQuery)
                .replace("<deleteQuery>", currentDeleteQuery).replaceAll("<whereClause>", currentWhereClause.toString())
                .replace("<getSettings>", currentGet).replace("<insertSettings>", currentInsert)
                .replace("<updateSettings>", currentUpdate).replace("<deleteSettings>", currentDelete)
                .replace("<extractSettings>", currentExtractFromResultSet).replace("<imports>", currentImports)
                .replaceAll("<package>", packageName).replace("<dtopackage>", dtoPackageName)
                .replaceAll("<lowerclass>", lowerIdentifier).replaceAll("<class>", identifier);

        classes.put(packageName.replaceAll("\\.","/") + "/" + currentClass.getIdentifier() + "DAO.java", currentClassTemplate);
    }

}
