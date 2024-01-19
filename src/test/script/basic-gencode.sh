##! /bin/sh
#
## Auteur : gl25
## Version initiale : 01/01/2024
#
## Encore un test simpliste. On compile un fichier (cond0.deca), on
## lance ima dessus, et on compare le résultat avec la valeur attendue.
#
## Ce genre d'approche est bien sûr généralisable, en conservant le
## résultat attendu dans un fichier pour chaque fichier source.
#
## Se déplacer dans le répertoire du script
#cd "$(dirname "$0")"/../../.. || exit 1
#
## Ajouter le répertoire au PATH
#PATH=./src/test/script/launchers:./src/main/bin:"$PATH"
#
## Répertoire contenant les fichiers à tester
#repertoire_test=./src/test/deca/codegen/valid/provided
#
## Boucle pour tester tous les fichiers du répertoire
#for fichier_source in "$repertoire_test"/*.deca; do
#    fichier_asm="${fichier_source%.deca}.ass"
#    rm -f "$fichier_asm" 2>/dev/null
#    decac "$fichier_source" || exit 1
#
#    if [ ! -f "$fichier_asm" ]; then
#        echo "Fichier $fichier_asm non généré."
#        exit 1
#    fi
#
#    resultat=$(ima "$fichier_asm") || exit 1
#    rm -f "$fichier_asm"
#
#    # Extraire la valeur attendue à partir des commentaires en appelant le script python
#    attendu=$(python3 testCodeGen.py "$fichier_source") || exit 1
#
#
#    if [ -z "$attendu" ]; then
#        echo "Aucune valeur attendue trouvée pour $fichier_source."
#        exit 1
#    fi
#
#
#    # On vérifie que tous les éléments d'attendu correspondent bien au résultat obtenu
#    for element in $attendu;do
#        if echo "$element" | grep -q -w "$resultat"; then
##            echo "attendu = $element"
##            echo "res = $resultat"
#            :
#        else
#            echo "Résultat inattendu de ima pour $fichier_source :"
##            echo "Attendu : $element"
##            echo "Obtenu  : $resultat"
#            exit 1
#        fi
#
#    done
#    echo "Succès attendu de test_codeGen sur $fichier_source"
#done
#
