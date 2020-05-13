package langage.semantic.generator;

import langage.grammar.analysis.DepthFirstAdapter;
import langage.grammar.node.*;
import langage.semantic.structure.Class;
import langage.semantic.structure.ClassTable;
import langage.semantic.structure.Member;

import java.util.Collection;
import java.util.LinkedList;

public class SqlGenerator extends DepthFirstAdapter {

    private Class currentClass;

    private ClassTable classTable;
    private StringBuilder script;
    private StringBuilder uniques;
    private StringBuilder indexes;
    private StringBuilder allForeignKeys;

    private SqlGenerator(ClassTable classTable){
        this.script = new StringBuilder();
        this.allForeignKeys = new StringBuilder();
        this.classTable = classTable;
    }

    public static String generate(Node node, ClassTable classTable){
        SqlGenerator generator = new SqlGenerator(classTable);
        node.apply(generator);
        return generator.script.toString();
    }

    @Override
    public void inAClassDeclaration(AClassDeclaration node) {
        this.currentClass = classTable.get(node.getIdentifier());
        this.script.append(String.format("Create Table `%s` (\n", currentClass.getSqlIdentifier()));
        this.uniques = new StringBuilder();
        this.indexes = new StringBuilder();
    }

    @Override
    public void inAMemberClassMember(AMemberClassMember node) {
        String identifier = node.getIdentifier().getText();
        Member member = currentClass.getMember(identifier);
        this.script.append(String.format("    %s,\n", member.toSql()));
    }

    @Override
    public void inAKeyMemberClassMember(AKeyMemberClassMember node) {
        String identifier = node.getIdentifier().getText();
        Member member = currentClass.getMember(identifier);
        this.script.append(String.format("    %s,\n", member.toSql()));
    }

    @Override
    public void inASimpleUniqueConstraint(ASimpleUniqueConstraint node) {
        String identifier = node.getIdentifier().getText();
        String capitalizedIdentifier = String.valueOf(identifier.charAt(0)).toUpperCase() + identifier.substring(1);
        String constraintName = currentClass.getSqlIdentifier() + "_" + capitalizedIdentifier;
        uniques.append(String.format("    CONSTRAINT UC_%s UNIQUE(%s),\n", constraintName, identifier));
    }

    @Override
    public void inAComposedUniqueConstraint(AComposedUniqueConstraint node) {
        LinkedList<String> individualIdentifiers = new LinkedList<>();
        individualIdentifiers.add(node.getFirst().getText());
        node.getTail().stream()
                .map(item -> (AAdditionalIdentifier) item)
                .map(AAdditionalIdentifier::getIdentifier)
                .map(Token::getText)
                .forEach(individualIdentifiers::add);
        StringBuilder constraintName = new StringBuilder().append(currentClass.getSqlIdentifier());
        individualIdentifiers.stream()
                .map(item -> String.valueOf(item.charAt(0)).toUpperCase() + item.substring(1))
                .forEach(item -> constraintName.append("_").append(item));
        StringBuilder columns = new StringBuilder();
        individualIdentifiers.forEach(item -> columns.append(",").append(item));
        columns.deleteCharAt(0);
        uniques.append(String.format("    CONSTRAINT UC_%s UNIQUE(%s),\n", constraintName, columns));
    }

    @Override
    public void inASimpleIndexConstraint(ASimpleIndexConstraint node) {
        String identifier = node.getIdentifier().getText();
        String capitalizedIdentifier = String.valueOf(identifier.charAt(0)).toUpperCase() + identifier.substring(1);
        String constraintName = currentClass.getSqlIdentifier() + "_" + capitalizedIdentifier;
        if(node.getUniqueKeyword() == null) {
            indexes.append(String.format(
                    "Create Index IDX_%s\n    ON %s (%s);\n\n",
                    constraintName, currentClass.getSqlIdentifier(), identifier));
        } else {
            indexes.append(String.format(
                    "Create Unique Index UIDX_%s\n    ON %s (%s);\n\n",
                    constraintName, currentClass.getSqlIdentifier(), identifier));
        }
    }

    @Override
    public void inAComposedIndexConstraint(AComposedIndexConstraint node) {
        LinkedList<String> individualIdentifiers = new LinkedList<>();
        individualIdentifiers.add(node.getFirst().getText());
        node.getTail().stream()
                .map(item -> (AAdditionalIdentifier) item)
                .map(AAdditionalIdentifier::getIdentifier)
                .map(Token::getText)
                .forEach(individualIdentifiers::add);
        StringBuilder constraintName = new StringBuilder().append(currentClass.getSqlIdentifier());
        individualIdentifiers.stream()
                .map(item -> String.valueOf(item.charAt(0)).toUpperCase() + item.substring(1))
                .forEach(item -> constraintName.append("_").append(item));
        StringBuilder columns = new StringBuilder();
        individualIdentifiers.forEach(item -> columns.append(",").append(item));
        columns.deleteCharAt(0);
        if(node.getUniqueKeyword() == null){
            indexes.append(String.format(
                    "Create Index IDX_%s\n    ON %s (%s);\n\n",
                    constraintName, currentClass.getSqlIdentifier(), columns));
        } else {
            indexes.append(String.format(
                    "Create Unique Index UIDX_%s\n    ON `%s` (%s);\n\n",
                    constraintName, currentClass.getSqlIdentifier(), columns));
        }
    }

    @Override
    public void outAClassDeclaration(AClassDeclaration node) {
        Collection<Member> primaryKeys = currentClass.getPrimaryKeys();
        Collection<Member> foreignKeys = currentClass.getForeignKeys();

        if(primaryKeys.size() != 0) {
            StringBuilder listPks = new StringBuilder();
            primaryKeys.forEach(member -> listPks.append(member.getIdentifier()).append(','));
            listPks.deleteCharAt(listPks.length()-1);
            this.script.append(String.format("    CONSTRAINT PK_%s PRIMARY KEY (%s),\n",
                    currentClass.getSqlIdentifier(), listPks.toString()));
        }
        if(foreignKeys.size() != 0) {
            this.allForeignKeys.append("Alter Table `").append(currentClass.getSqlIdentifier()).append("`\n");
            for(Member member : foreignKeys){
                String memberIdentifier = member.getIdentifier();
                String capitalizedMemberIdentifier = String.valueOf(memberIdentifier.charAt(0)).toUpperCase() + memberIdentifier.substring(1);
                String fkName = currentClass.getSqlIdentifier() + "_" + capitalizedMemberIdentifier;
                this.allForeignKeys.append(String.format("    ADD CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES `%s` (%s),\n",
                        fkName,
                        memberIdentifier,
                        member.getClassType().getSqlIdentifier(),
                        member.getClassType().getUniquePrimaryKey().getIdentifier()));
            }
            this.allForeignKeys.replace(this.allForeignKeys.length()-2, this.allForeignKeys.length()-1, ";\n");
        }
        this.script.append(uniques);
        this.script.deleteCharAt(this.script.length()-2); // enlever la virgule..
        this.script.append(");\n\n").append(indexes);
    }

    @Override
    public void outAProgram(AProgram node) {
        this.script.append("-- Foreign keys --\n\n").append(allForeignKeys);
    }
}
