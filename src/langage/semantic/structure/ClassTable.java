package langage.semantic.structure;

import langage.grammar.node.TIdentifier;
import langage.semantic.SemanticException;

import java.util.HashMap;
import java.util.Map;


public class ClassTable {

    private Map<String, Class> classes;

    public ClassTable(){
        classes = new HashMap<>();
    }

    public void add(TIdentifier classToken) {
        String identifier = classToken.getText();
        if(contains(identifier)){
            throw new SemanticException(String.format("Class %s already exists.", identifier));
        }
        classes.put(identifier, new Class(identifier));
    }

    public boolean contains(String identifier){
        return classes.containsKey(identifier);
    }

    public Class get(TIdentifier classToken){
        String identifier = classToken.getText();
        if(!contains(identifier)){
            throw new SemanticException(String.format("Class %s doesn't exist.", identifier));
        }
        return classes.get(identifier);
    }


    public Class get(String identifier) {
        if(!contains(identifier)){
            throw new SemanticException(String.format("Class %s doesn't exist.", identifier));
        }
        return classes.get(identifier);
    }
}
