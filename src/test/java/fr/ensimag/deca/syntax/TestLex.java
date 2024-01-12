package fr.ensimag.deca.syntax;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestLex {

    @Test
    public void testCommandLineOnFiles() {
        String folderPath = "../../../../../deca/syntax/ownTests/valid/";

        // On récupère tous les fichiers du dossier
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // Pour chaque fichier, exécutez la commande et vérifiez que la sortie est bien Test_lex [OK]
                    testCommandOnFile(file);
                }
            }
        }
    }

    private void testCommandOnFile(File file) {
        try {
            String command = "python3 ../../../../../../../lexer_test.py " + file.getAbsolutePath();

            // On exécute la commande
            Process process = Runtime.getRuntime().exec(command);

            // On récupère la sortie
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Attendez la fin du processus
            process.waitFor();

            // Vérifiez si la sortie est "Test_lex [OK]"
            assertEquals("Test_lex [OK]", output.toString().trim());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Erreur lors de l'exécution de la commande sur le fichier " + file.getName());
        }
    }
}
