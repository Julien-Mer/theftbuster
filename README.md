# [TheftBuster](https://github.com/Julien-Mer/theftbuster)

TheftBuster est une application android disponible (ou bientôt) sur PlayStore sans aucune publicité ou fonctionnalités payantes, c'est un projet qui sera présenté au Baccalauréat pour la spécialité Informatique et Sciences du Numérique.

Développé à l'aide d'Android Studio, l'application n'est disponible que pour les api 19 (KitKat) ou plus récentes pour un meilleur rapport performances/[compatibilité](https://developer.android.com/about/dashboards/index.html)



## Fonctionnalités
### Fonctionnement
* TheftBuster est capable d'analyser les SMS reçus qui contiennent le suffixe TheftBuster, suivit du code à 6 chiffres, choisi par l'utilisateur, sans que l'utilisateur ne le voit. Et d'analyser l'instruction reçue. 
* L'application mémorise l'état du téléphone et des commandes pour pouvoir être efficace même en cas de redémarrage.
* TheftBuster est capable d'informer l'émetteur de la commande SMS des résultats ou il peut alarmer un téléphone de confiance sur l'état actuel du téléphone (suspicion de vol).

### Commandes SMS
* Siren on/off - Active ou désactive la sirène
* Lock - Vérrouille le téléphone
* AutoLocate - Localise le téléphone selon une intervalle de temps ou une distance parcourue en mètres
* Locate - Localise le téléphone du téléphone
* Format - Formate le téléphone
* Password - Change le mot de passe du téléphone
* Call - Appelle le numéro de téléphone renseigné
* Photo - Enverra une photo du voleur si l'écran est allumé
* Contacts - Renvoie la liste des contacts du téléphone
* (ROOT) Hide - Les boutons sont désactivés et le téléphone simule l'état éteint

### Sécurité
* Impossible d'arrêter la sirène quand elle est activée
* Le Lock empêche toutes actions à l'utilisateur (il peut seulement forcer l'arrêt)
* L'application se lance automatiquement au démarrage
* La mémoire de l'application est chiffrée
* Le code à 6 chiffres permet un possibilité de 10 000 000 de codes

### Environnement
* Détection du changement de carte SIM + détection de l'identité du propriétaire (Code RIO, identité, identifiant SIM voire numéro de téléphone)
* Les tentatives de déverrouillage infructueuses provoquent un lock



## Contributeurs
	Projet BAC ISN 2017-2018
 * Clément D.
 * Hugo C.
 * Julien M.

    
