# HOW TO
- `git status`
- `git add <file>` *si vous êtes sur de vouloir add tout les fichiers* `git add -A`
- `git commit -m "message"` *titre résumant le commit, corps du commit avec les descriptions des update / fixs*
- `git pull --rebase` *pull le travail des autres. Si il y a des conflits, les régler puis suivre les instructions dans les messages*
- `git push`

Si vous voulez faire un commit mais ne voulez pas ajouter certains fichiers qui ne sont pas finis: `git stash` *après avoir add les fichiers que vous voulez commit*  
Si vous voulez annuler "l'add" d'un fichier: `git rm --cached <file>`  


# Getting started
- Spring MVC : https://spring.io/guides/gs/serving-web-content/
- Hibernate : http://www.springboottutorial.com/hibernate-jpa-tutorial-with-spring-boot-starter-jpa
- Websocket:  https://spring.io/guides/gs/messaging-stomp-websocket/

# Docs
- https://www.thymeleaf.org/

# Pages HTML :
- Index : Listing de toutes les parties en cours + json
- Plateau de jeu
- Login / Register
- Création de partie
- Création de carte

# Plugins IntelliJ
- Lombok

# Protocol
## app (Client -> J2E)
- app/moveMyTank
- app/damageTank
- (app/killTank)
- app/shoot

## client (J2E -> Client)
Subscribe to `/game/game-{gameId}`
- TANKS(tanks)
- NEW_TANK(tankId, tank)
- DELETE_TANK(tankId)

- MOVETANK(tank)
- client/damageToTank
- client/updateScore
- client/newShoot
- client/newTank
- client/disconnectTank

# TODO
- (pas prio) Envoyer les positions des tirs par le serveur
- (pas prio) Faire envoyer les positions des tanks par le serveur plutot que de transmettre les packets
- Zoom/Dezoom automatique sur une partie
- Collision avec la map
- Rendre joli page de création de map
- (pas prio) Faire qqch quand on tue un tank
- Ne pas recreer une map si le mec save 2 fois
- Implémenter la rotation sur la création de map
- Faire un bouger le canon sans que la souris bouge