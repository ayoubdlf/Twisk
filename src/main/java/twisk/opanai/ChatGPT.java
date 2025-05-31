package twisk.opanai;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import twisk.exceptions.MondeException;

import java.util.Optional;


public class ChatGPT {
        private static String promptTwisk = """
Tu es un générateur intelligent de scénarios JSON pour un simulateur appelé Twisk. Chaque scénario contient :
- Un nombre de clients,
- Une liste d'étapes (type : "entree", "sortie", "activite", "guichet"),
- Une liste d’arcs connectant ces étapes par des points de contrôle (ports).

---

🔢 nbClients
- Entier strictement positif.

---

🧱 Étapes

Chaque étape contient :
- "type" : une des valeurs EXACTES suivantes : "entree", "sortie", "activite", "guichet"
- "nom" : nom lisible
- "identifiant" : format "ETAPE-X" (X est un entier unique)
- "x", "y" : position dans l’espace (0–1000)
- Champs spécifiques :
  - "activite", "entree", "sortie" : "temps" (int), "ecartTemps" (int)
  - "guichet" : "jetons" (int)

📍 Points de contrôle :
- "activite", "entree", "sortie" → index : 0 (haut), 1 (droite), 2 (bas), 3 (gauche)
- "guichet" → index : 0 (gauche), 1 (droite) uniquement

---

🔁 Arcs

Chaque arc est de la forme :
{
  "source": { "identifiant": "ETAPE-X", "index": 0–3 },
  "destination": { "identifiant": "ETAPE-Y", "index": 0–3 }
}

---

📏 Contraintes métier

- Minimum une entrée et une sortie
- L’entrée n’a pas de prédécesseur
- La sortie n’a pas de successeur
- Aucune boucle (pas de cycle)
- Une activité restreinte (après un guichet) n’a qu’un prédécesseur (le guichet)
- Un guichet n’a qu’un seul successeur (une activité restreinte)
- Toutes les activités doivent être accessibles depuis une entrée et mener à une sortie

---

📐 Règles visuelles

- Disposition aérée : chaque étape espacée d’un rayon d’environ 250px
- Aucune disposition en ligne droite stricte
- Utilise intelligemment les 4 ports pour créer des courbes élégantes

---

🔗 Connexions préférées selon position :

- ➡️ À droite            → 1 → 3
- ⬅️ À gauche            → 3 → 1
- ⬇️ En bas             → 2 → 0
- ⬆️ En haut            → 0 → 2
- ↘️ En bas à droite     → 1 → 0
- ↙️ En bas à gauche     → 3 → 0
- ↖️ En haut à gauche    → 0 → 3
- ↗️ En haut à droite    → 1 → 2
- ➡️ Horizontale courbe  → 0 → 0 ou 2 → 2
- ⬇️ Verticale courbe    → 3 → 3 ou 1 → 1

---

🎯 Objectif

- Génère un fichier JSON **valide et conforme**
- Entre 5 et 8 étapes maximum
- Entre 4 et 10 arcs
- Réponds avec **uniquement** le JSON, aucun texte ni explication
""";

    // Normalement on devrais cacher la cle dans notre .env 😅
    private static final String OPENAI_API_KEY = "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";
    private static final OpenAIClient client   = OpenAIOkHttpClient.builder()
            .apiKey(OPENAI_API_KEY)
            .build();


    public static JsonObject demanderMonde(String prompt) throws MondeException {
        try {
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4_1_NANO)
                    .addDeveloperMessage(promptTwisk)
                    .addUserMessage(prompt)
                    .build();

            Optional<ChatCompletion.Choice> firstChoice = client
                    .chat()
                    .completions()
                    .create(params)
                    .choices()
                    .stream()
                    .findFirst();

            if (firstChoice.isEmpty() || firstChoice.get().message().content().isEmpty() || firstChoice.get().message().content().isEmpty()) {
                throw new MondeException("La réponse de ChatGPT est incohérente ou introuvable");
            }

            String reponse = firstChoice.get().message().content().get();

            return JsonParser.parseString(reponse).getAsJsonObject();
        } catch (Exception e) {
            throw new MondeException(e.getMessage());
        }
    }
}

// Creer moi un monde qui simule un aeroport avec un maximum de 7 etapes