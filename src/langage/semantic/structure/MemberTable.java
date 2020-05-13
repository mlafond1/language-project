package langage.semantic.structure;

import langage.semantic.SemanticException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemberTable {

    private Map<String, Member> members;

    public MemberTable(){
        members = new LinkedHashMap<>();
    }

    public void add(Member member) {
        if(members.containsKey(member.getIdentifier())){
            throw new SemanticException(String.format("Member %s already exists", member.getIdentifier()));
        }
        members.put(member.getIdentifier(), member);
    }

    public boolean contains(String identifier){
        return members.containsKey(identifier);
    }

    public Member get(String identifier){
        if(!members.containsKey(identifier)){
            throw new SemanticException(String.format("Member %s doesn't exist", identifier));
        }
        return members.get(identifier);
    }

    public Collection<Member> getAll() {
        return members.values();
    }
}
