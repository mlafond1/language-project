package langage.semantic.generator;

import langage.grammar.analysis.DepthFirstAdapter;
import langage.grammar.node.*;
import langage.semantic.structure.*;
import langage.semantic.structure.Class;

import java.util.*;

public class DtoGenerator extends DepthFirstAdapter {

    private Map<String, String> classes;
    private ClassTable classTable;

    private Class currentClass;
    private String currentClassTemplate;
    private String packageName;

    private StringBuilder currentImports;
    private StringBuilder currentMembers;
    private StringBuilder currentGetters;
    private StringBuilder currentSetters;

    private final static String template = "" +
            "package <package>;\n\n" +
            "<imports>\n" +
            "public class <class>DTO {\n\n" +
            "<members>\n" +
            "<constructors>\n" +
            "<setters>" +
            "<getters>\n" +
            "}\n\n";

    private DtoGenerator(ClassTable classTable){
        this.classes = new HashMap<>();
        this.classTable = classTable;
    }

    public static Map<String, String> generate(Node node, ClassTable classTable){
        DtoGenerator generator = new DtoGenerator(classTable);
        node.apply(generator);
        return generator.classes;
    }

    @Override
    public void inASimplePackage(ASimplePackage node) {
        packageName = node.getIdentifier().getText() + ".dto";
    }

    @Override
    public void inAComposedPackage(AComposedPackage node) {
        packageName = node.getIdentifier().getText() + ".dto";
    }

    @Override
    public void inAClassDeclaration(AClassDeclaration node) {
        if(packageName == null) packageName = "dto";
        this.currentClass = classTable.get(node.getIdentifier());
        this.currentImports = new StringBuilder();
        this.currentMembers = new StringBuilder();
        this.currentGetters = new StringBuilder();
        this.currentSetters = new StringBuilder();

        this.currentClassTemplate = template;
    }

    @Override
    public void inAMemberClassMember(AMemberClassMember node) {
        String identifier = node.getIdentifier().getText();
        Member member = currentClass.getMember(identifier);
        handleMember(member);
    }

    @Override
    public void inAKeyMemberClassMember(AKeyMemberClassMember node) {
        String identifier = node.getIdentifier().getText();
        Member member = currentClass.getMember(identifier);
        handleMember(member);
    }

    private void handleMember(Member member){
        String identifier = member.getIdentifier();
        currentMembers.append(String.format("    private %s;\n", member.toJava()));

        Type type = member.getType();
        String capitalizedIdentifier = String.valueOf(identifier.charAt(0)).toUpperCase() + identifier.substring(1);

        if(currentImports.length() == 0 && (type.getRecursiveType() == TypeEnum.DATE || type.getRecursiveType() == TypeEnum.TIMESTAMP)){
            currentImports.append("import java.util.Date;\n");
        }

        currentSetters.append(String.format("\n    public void set%s(%s){\n",
                capitalizedIdentifier, member.toJava()
        ));
        currentSetters.append(String.format("        this.%s = %s;\n", identifier, identifier));
        currentSetters.append("    }\n");

        currentGetters.append(String.format("\n    public %s get%s(){\n",
                type.toJava(), capitalizedIdentifier
        ));
        currentGetters.append(String.format("        return this.%s;\n", identifier));
        currentGetters.append("    }\n");
    }

    @Override
    public  void outAClassDeclaration(AClassDeclaration node) {
        Set<String> constructors = new LinkedHashSet<>();
        constructors.add(String.format("    public %sDTO(){}", currentClass.getIdentifier()));
        constructors.add(createConstructor(currentClass.getPrimaryKeys()));
        constructors.add(createConstructor(currentClass.getMembers()));

        StringBuilder currentConstructors = new StringBuilder();
        for(String constructor : constructors){
            currentConstructors.append("\n").append(constructor).append("\n");
        }

        currentClassTemplate = currentClassTemplate
                .replace("<getters>", currentGetters).replace("<setters>", currentSetters)
                .replace("<constructors>", currentConstructors).replace("<members>", currentMembers)
                .replace("<imports>", currentImports).replace("<class>", currentClass.getIdentifier())
                .replace("<package>", packageName);

        classes.put(packageName.replaceAll("\\.","/") + "/" + currentClass.getIdentifier() + "DTO.java", currentClassTemplate);
    }

    private String createConstructor(Collection<Member> members) {
        StringBuilder constructor = new StringBuilder();
        StringBuilder params = new StringBuilder();
        StringBuilder assigns = new StringBuilder();

        constructor.append(String.format("    public %sDTO(", currentClass.getIdentifier()));
        for(Member member : members){
            params.append(member.toJava()).append(", ");
            assigns.append(String.format("        this.%s = %s;\n", member.getIdentifier(), member.getIdentifier()));
        }
        params.deleteCharAt(params.length()-1); // enlever l'espace
        params.deleteCharAt(params.length()-1); // enlever la virgule
        constructor.append(params).append(") {\n").append(assigns).append("    }");
        return constructor.toString();
    }
}
