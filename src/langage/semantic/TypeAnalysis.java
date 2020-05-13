package langage.semantic;

import langage.grammar.analysis.DepthFirstAdapter;
import langage.grammar.node.*;
import langage.semantic.structure.Class;
import langage.semantic.structure.ClassTable;
import langage.semantic.structure.Type;
import langage.semantic.structure.TypeEnum;

public class TypeAnalysis extends DepthFirstAdapter {

    private ClassTable classTable;
    private Class classType;
    private TypeEnum resultType;
    private int size;

    private TypeAnalysis(ClassTable classTable){
        this.classTable = classTable;
        size = -1;
    }

    public static Type calculateType(Node node, ClassTable classTable){
        TypeAnalysis analysis = new TypeAnalysis(classTable);
        node.apply(analysis);
        return new Type(analysis.classType, analysis.resultType, analysis.size);
    }

    @Override
    public void inASize(ASize node) {
        String sizeStr = node.getNumber().getText();
        try{
            size = Integer.parseInt(sizeStr);
        } catch (NumberFormatException e){
            throw new SemanticException(String.format("Invalid size : %s", sizeStr), node.getNumber(), e);
        }
    }

    @Override
    public void inAClassType(AClassType node) {
        String identifier = node.getIdentifier().getText();
        if(!classTable.contains(identifier)){
            throw new SemanticException(String.format("Class %s doesn't exist.", identifier), node.getIdentifier());
        }
        classType = classTable.get(identifier);
        resultType = TypeEnum.CLASS;
    }

    @Override
    public void inAIntBaseType(AIntBaseType node) {
        resultType = TypeEnum.INT;
        if(node.getAutoKeyword() != null){
            resultType = TypeEnum.AUTO;
        }
    }

    @Override
    public void inABoolBaseType(ABoolBaseType node) {
        resultType = TypeEnum.BOOL;
    }

    @Override
    public void inAFloatBaseType(AFloatBaseType node) {
        resultType = TypeEnum.FLOAT;
    }

    @Override
    public void inADoubleBaseType(ADoubleBaseType node) {
        resultType = TypeEnum.DOUBLE;
    }

    @Override
    public void inACharBaseType(ACharBaseType node) {
        size = 1;
        resultType = TypeEnum.CHAR;
    }

    @Override
    public void inAStringBaseType(AStringBaseType node) {
        size = 64; // Taille par défaut
        resultType = TypeEnum.STRING;
    }

    @Override
    public void inADateBaseType(ADateBaseType node) {
        resultType = TypeEnum.DATE;
    }

    @Override
    public void inATimestampBaseType(ATimestampBaseType node) {
        resultType = TypeEnum.TIMESTAMP;
    }
}
