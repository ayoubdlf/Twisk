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


/**
 * Classe utilitaire permettant de générer un scénario JSON conforme au simulateur Twisk.
 */
public class ChatGPT {
        private static String promptTwisk = """
Tu es un générateur intelligent de scénarios JSON pour un simulateur appelé Twisk. Chaque scénario contient :
- Un nombre de clients,
- Une liste d'étapes (états du graphe),
- Une liste d'arcs (connexions entre les étapes).

---

🎯 Objectif
Tu dois générer un fichier JSON VALIDE, strictement conforme aux règles suivantes.

---

🔢 nbClients
- Entier strictement positif strictement inferieur à 50.

---

🧱 Étapes

Chaque étape contient :
- "type": obligatoirement une des valeurs suivantes : "entree", "sortie", "activite", "guichet"
- "nom": nom lisible (ex: "Activite 1", "Guichet 2")
- "identifiant": unique, format "ETAPE-X"
- "x", "y": position dans un plan 2D de 0 à 1100
- selon le type :
  - "entree", "activite", "sortie" → champs "temps", "ecartTemps" (avec contrainte : 0 < ecartTemps < temps < 100)
  - "guichet" → champ "jetons"

📍 Points de contrôle :
- "activite", "entree", "sortie" → index : 0 (haut), 1 (droite), 2 (bas), 3 (gauche)
- "guichet" → index : 0 (gauche), 1 (droite) uniquement

---

🔁 Arcs

Chaque arc est de la forme :
{
  "source": { "identifiant": "ETAPE-X", "index": 0-3 },
  "destination": { "identifiant": "ETAPE-Y", "index": 0-3 }
}

- Une étape ne peut pas avoir deux fois le même successeur. Chaque lien source-destination doit être unique.
- Par exemple, si ETAPE-A a déjà un arc vers ETAPE-B, elle ne peut pas en avoir un deuxième vers ETAPE-B.

---

📏 Contraintes métier

1. Présence obligatoire :
   - Au moins une seule et unique entrée
   - Au moins une seule et unique sortie

2. Accessibilité logique :
   - Toute activité doit être accessible depuis une entrée
   - Toute activité doit mener à une sortie

3. Guichets et Activités restreintes :
   - Un guichet a exactement UNE ACTIVITE UNIQUEMENT comme successeur
   - Un guichet a exactement UNE activité restreinte comme successeur
   - Une activité restreinte ne peut avoir QU'UN SEUL prédécesseur, le guichet
   - L'activité restreinte peut être aussi une sortie
   - L'activité restreinte NE PEUT PAS être une entrée
   - Un guichet ne peut pas être une sortie

4. Structure du graphe :
   - Aucun cycle autorisé (graphe acyclique)
   - Une activité peut être aussi une entrée, une sortie, ou les deux

---

📐 Positionnement des étapes (x, y)

- Les étapes connectées doivent être proches sur le plan
- Respecter un espacement horizontal ou vertical de ~250 pixels
- Exemple :
  - Si ETAPE-A est connectée à ETAPE-B, alors positionne ETAPE-B à une de ces positions:
     - (ETAPE-A.x + 250, ETAPE-A.y)
     - (ETAPE-A.x - 250, ETAPE-A.y)
     - (ETAPE-A.x      , ETAPE-A.y + 250)
     - (ETAPE-A.x      , ETAPE-A.y - 250)
     - (ETAPE-A.x + 250, ETAPE-A.y - 250)
     - (ETAPE-A.x - 250, ETAPE-A.y + 250)
     - (ETAPE-A.x - 250, ETAPE-A.y - 250)
     - (ETAPE-A.x + 250, ETAPE-A.y + 250)

---

✅ À faire

- Génère un fichier JSON complet et conforme à ces règles
- Inclure au minimum : 1 entrée, 1 sortie, 1 guichet, 2 activités
- Crée entre 5 et 10 arcs
- Réponds uniquement avec le fichier JSON (aucun texte, aucun commentaire)
""";



    // Normalement on devrais cacher la cle dans notre .env 😅
    private static final String OPENAI_API_KEY = "TODO_OPENAI_API_KEY";
    private static final OpenAIClient client   = OpenAIOkHttpClient.builder()
            .apiKey(OPENAI_API_KEY)
            .build();


    /**
     * Envoie une requête à ChatGPT avec le prompt fourni pour générer un monde Twisk en JSON.
     *
     * @param prompt Le prompt personnalisé ajouté au prompt dévélopeur.
     * @return Un objet JSON représentant un monde Twisk valide.
     * @throws MondeException si une erreur survient lors de la requête ou du traitement de la réponse.
     */
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