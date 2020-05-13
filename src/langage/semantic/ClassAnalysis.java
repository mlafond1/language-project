package langage.semantic;

import langage.grammar.analysis.DepthFirstAdapter;
import langage.grammar.node.*;
import langage.semantic.structure.*;

import java.util.*;
import java.util.stream.Collectors;

public class ClassAnalysis extends DepthFirstAdapter {

    private String currentClassIdentifier;
    private ClassTable classTable;
    private MemberTable memberTable;

    private Set<String> uniques = new HashSet<>();
    private Set<String> indexes = new HashSet<>();

    private ClassAnalysis(){
        classTable = new ClassTable();
        memberTable = new MemberTable();
    }

    private ClassAnalysis(ClassTable classTable){
        this.classTable = classTable;
    }

    private Member readMember(PType node, String identifier, boolean isPrimary){
        Type type = TypeAnalysis.calculateType(node, this.classTable);
        return new Member(type, identifier, isPrimary);
    }

    public static ClassTable verify(Node node){
        ClassAnalysis verifier = new ClassAnalysis();
        node.apply(verifier);
        return verifier.classTable;
    }

    public static ClassTable verify(Node node, ClassTable classTable){
        ClassAnalysis verifier = new ClassAnalysis(classTable);
        node.apply(verifier);
        return verifier.classTable;
    }

    @Override
    public void inAProgram(AProgram node) {
        node.getClasses().stream()
                .map(elem -> (AClassDeclaration) elem)
                .forEach(elem -> classTable.add(elem.getIdentifier()));
        long modifiedCount = node.getClasses().stream()
                .map(elem -> (AClassDeclaration) elem)
                .filter(elem -> elem.getAsModifier() != null)
                .count();
        long distinctModifiedCount = node.getClasses().stream()
                .map(elem -> (AClassDeclaration) elem)
                .filter(elem -> elem.getAsModifier() != null)
                .map(elem -> (AAsModifier) elem.getAsModifier())
                .map(AAsModifier::getIdentifier).distinct().count();
        if(modifiedCount != distinctModifiedCount)
            throw new SemanticException("Renamed classes enter in conflict with each other..");
    }

    @Override
    public void inASimplePackage(ASimplePackage node) {
        String packageName = node.getIdentifier().getText();
        if(packageName.startsWith(".") || packageName.endsWith(".") || packageName.contains("..")){
            throw new SemanticException(
                    "Package name can't have consecutive dots or at the beginning or end ("+packageName+")",
                    node.getIdentifier());
        }
    }

    @Override
    public void inAComposedPackage(AComposedPackage node) {
        String packageName = node.getIdentifier().getText();
        if(packageName.startsWith(".") || packageName.endsWith(".") || packageName.contains("..")){
            throw new SemanticException(
                    "Package name can't have consecutive dots or at the beginning or end ("+packageName+")",
                    node.getIdentifier());
        }
    }


    @Override
    public void inAClassDeclaration(AClassDeclaration node) {
        currentClassIdentifier = node.getIdentifier().getText();
        memberTable = new MemberTable();
        uniques.clear();
        indexes.clear();
    }

    @Override
    public void inAAsModifier(AAsModifier node) {
        String identifier = node.getIdentifier().getText();
        if(identifier.equals(currentClassIdentifier)) return;
        if(classTable.contains(identifier)){
            throw new SemanticException(String.format(
                    "Cannot rename %s to %s because %s already exists",
                    currentClassIdentifier, identifier, identifier), node.getIdentifier());
        }
        classTable.get(currentClassIdentifier).setSqlIdentifier(identifier);
    }

    @Override
    public void caseAMemberClassMember(AMemberClassMember node) {
        Member member = readMember(node.getType(), node.getIdentifier().getText(), false);
        if(member.getType().getType() == TypeEnum.AUTO){
            Token token = ((AIntBaseType)((ABaseType) node.getType()).getBaseType()).getAutoKeyword();
            throw new SemanticException(String.format("Non key member '%s' can't be auto", member.getIdentifier()), token);
        }
        memberTable.add(member);
    }

    @Override
    public void caseAKeyMemberClassMember(AKeyMemberClassMember node) {
        Member member = readMember(node.getType(), node.getIdentifier().getText(), true);
        memberTable.add(member);
    }

    @Override
    public void inASimpleUniqueConstraint(ASimpleUniqueConstraint node) {
        String identifier = node.getIdentifier().getText();
        if(uniques.contains(identifier))
            throw new SemanticException(
                    "Unique constraint already exist for " + identifier,
                    node.getIdentifier());
        if(!memberTable.contains(identifier))
            throw new SemanticException(
                    String.format("Member %s doesn't exist or is declared after unique constraint", identifier),
                    node.getIdentifier());
        uniques.add(identifier);
    }

    @Override
    public void inAComposedUniqueConstraint(AComposedUniqueConstraint node) {
        Set<String> individualIdentifiers = new HashSet<>();
        int size = node.getTail().size() + 1;
        individualIdentifiers.add(node.getFirst().getText());
        node.getTail().stream()
                .map(item -> (AAdditionalIdentifier) item)
                .map(AAdditionalIdentifier::getIdentifier)
                .map(Token::getText).distinct()
                .forEach(individualIdentifiers::add);
        // Check doublons
        if(individualIdentifiers.size() != size){
            throw new SemanticException("Unique constraint has a column used more than once", node.getUniqueKeyword());
        }
        // Check membre non existant
        Collection<String> nonIncludedMembers = individualIdentifiers.stream().filter(item -> !memberTable.contains(item)).collect(Collectors.toList());
        if(nonIncludedMembers.size() > 0){
            StringBuilder nonIncludedMembersString = new StringBuilder();
            individualIdentifiers.forEach(item -> nonIncludedMembersString.append(",").append(item));
            nonIncludedMembersString.deleteCharAt(0);
            throw new SemanticException(
                    String.format("Members %s don't exist or are declared after unique constraint", nonIncludedMembersString),
                    node.getUniqueKeyword());
        }
        // Check déjà présent
        StringBuilder constraintIdentifier = new StringBuilder();
        individualIdentifiers.forEach(item -> constraintIdentifier.append(",").append(item));
        constraintIdentifier.deleteCharAt(0);
        if(uniques.contains(constraintIdentifier.toString()))
            throw new SemanticException("Unique constraint already exist for " + constraintIdentifier);
        uniques.add(constraintIdentifier.toString());
    }

    @Override
    public void inASimpleIndexConstraint(ASimpleIndexConstraint node) {
        String identifier = node.getIdentifier().getText();
        if(indexes.contains(identifier))
            throw new SemanticException(
                    "index declaration already exist for " + identifier,
                    node.getIdentifier());
        if(!memberTable.contains(identifier))
            throw new SemanticException(
                    String.format("Member %s doesn't exist or is declared after index declaration", identifier),
                    node.getIdentifier());
        indexes.add(identifier);
    }

    @Override
    public void inAComposedIndexConstraint(AComposedIndexConstraint node) {
        Set<String> individualIdentifiers = new LinkedHashSet<>(); // Préserve l'ordre d'ajout
        int size = node.getTail().size() + 1;
        individualIdentifiers.add(node.getFirst().getText());
        node.getTail().stream()
                .map(item -> (AAdditionalIdentifier) item)
                .map(AAdditionalIdentifier::getIdentifier)
                .map(Token::getText).distinct()
                .forEachOrdered(individualIdentifiers::add);
        // Check doublons
        if(individualIdentifiers.size() != size){
            throw new SemanticException("Index declaration has a column used more than once", node.getIndexKeyword());
        }
        // Check membre non existant
        Collection<String> nonIncludedMembers = individualIdentifiers.stream().filter(item -> !memberTable.contains(item)).collect(Collectors.toList());
        if(nonIncludedMembers.size() > 0){
            StringBuilder nonIncludedMembersString = new StringBuilder();
            individualIdentifiers.forEach(item -> nonIncludedMembersString.append(",").append(item));
            nonIncludedMembersString.deleteCharAt(0);
            throw new SemanticException(
                    String.format("Members %s don't exist or are declared after index declaration", nonIncludedMembersString),
                    node.getIndexKeyword());
        }
        // Check déjà présent
        StringBuilder constraintIdentifier = new StringBuilder();
        individualIdentifiers.forEach(item -> constraintIdentifier.append(",").append(item));
        constraintIdentifier.deleteCharAt(0);
        if(indexes.contains(constraintIdentifier.toString()))
            throw new SemanticException("index declaration already exist for " + constraintIdentifier);
        indexes.add(constraintIdentifier.toString());
    }

    @Override
    public void outAClassDeclaration(AClassDeclaration node) {
        classTable.get(currentClassIdentifier).setMembers(memberTable);
    }
}
