# [TheftBuster](https://github.com/Julien-Mer/theftbuster)

TheftBuster est une application android disponible (ou bientôt) sur PlayStore sans aucune publicité ou fonctionnalités payantes, c'est un projet qui sera présenté au Baccalauréat pour la spécialité Informatique et Sciences du Numérique.

Développé à l'aide d'Android Studio, l'application n'est disponible que pour les api 19 (KitKat) ou plus récentes pour un meilleur rapport performances/[compatibilité](https://developer.android.com/about/dashboards/index.html)



## Fonctionnalités
### Fonctionnement
* TheftBuster est capable d'analyser les SMS reçus qui contiennent le suffixe TheftBuster, suivi du code à 8 chiffres, choisi par l'utilisateur, sans que l'utilisateur ne le voit. Et d'analyser l'instruction reçue. 
* L'application mémorise l'état du téléphone et des commandes pour pouvoir être efficace même en cas de redémarrage.
* TheftBuster est capable d'informer l'émetteur de la commande SMS des résultats ou il peut alarmer un téléphone de confiance sur l'état actuel du téléphone (suspicion de vol).

### Commandes SMS
* Siren on/off - Active ou désactive la sirène
* Lock - Vérrouille le téléphone
* AutoLocate - Localise le téléphone selon une intervalle de temps ou une distance parcourue en mètres
* Locate - Localise le téléphone du téléphone
* Password - Change le mot de passe du téléphone
* Call - Appelle le numéro de téléphone renseigné
* Callme - Rapelle le numéro de téléphone envoyant la commande
* Photo - Enverra une photo du voleur dès qu'un visage sera détecté
* Format - Supprime les fichiers de: Pictures, documents, document, DCIM, Movies, Download, Music, Snapchat, CloudDrive, Sounds

### Sécurité
* Impossible d'arrêter la sirène quand elle est activée
* Le Lock empêche toutes actions à l'utilisateur (il peut seulement forcer l'arrêt)
* L'application se lance automatiquement au démarrage
* La mémoire de l'application est chiffrée
* Le code à 8 chiffres permet un possibilité de 100 000 000 de codes 

### Environnement
* Détection du changement de carte SIM + détection de l'identité du propriétaire (Code RIO, identité, identifiant SIM voire numéro de téléphone)
* Les tentatives de déverrouillage infructueuses provoquent un lock

## Contributeurs
Projet BAC ISN 2017-2018
 Partie graphique
 * Clément D.
 * Hugo C.
 Partie code
 * Julien M. (reprise du projet)
