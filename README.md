# language-project
## Comment exécuter
Pour exécuter le projet, il faut d’abord générer le package grammar avec sablecc.
```
java -jar sablecc.jar -d src grammar/langage.sablecc
```

Ensuite, il faut compiler le projet (dans répertoire bin).
```
javac -d bin -cp src src/langage/Main.java
```

Ensuite, il faut copier les fichiers .dat vers le dossier des classes compilées.

Windows: `xcopy /S src\*.dat bin`

Linux :
`cd src; find -name \*.dat -exec cp --parents -v {} ../bin \;; cd..`

Finalement il faut exécuter le projet en spécifiant un fichier en argument.
```
java -cp bin langage.Main tests/input-example1.lang
```
Les fichiers générés seront dans un nouveau répertoire appelé generated.
