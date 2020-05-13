package langage.semantic.structure;

import langage.semantic.SemanticException;

import java.util.Collection;
import java.util.stream.Collectors;

public class Class {

    private String identifier;
    private String sqlIdentifier;
    private MemberTable members;

    public Class(String identifier) {
        this.identifier = identifier;
        this.sqlIdentifier = identifier;
        this.members = new MemberTable();
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSqlIdentifier() {
        return sqlIdentifier;
    }

    public void setSqlIdentifier(String sqlIdentifier) {
        this.sqlIdentifier = sqlIdentifier;
    }

    public void setMembers(MemberTable memberTable) {
        this.members = memberTable;
    }

    public Member getMember(String identifier) {
        return members.get(identifier);
    }

    public Collection<Member> getMembers() {
        return this.members.getAll();
    }

    public Collection<Member> getPrimaryKeys(){
        return members.getAll().stream().filter(Member::isPrimary).collect(Collectors.toList());
    }

    public Collection<Member> getForeignKeys(){
        return members.getAll().stream().filter(Member::isForeign).collect(Collectors.toList());
    }

    public Member getUniquePrimaryKey() {
        Collection<Member> primaryKeys = getPrimaryKeys();
        if(primaryKeys.size() != 1){
            throw new SemanticException(String.format("Primary Key in %s should be on only one field", identifier));
        }
        return primaryKeys.stream().findFirst().get();
    }
}
