del /q ..\mmorpg\src\main\java\mmorpg\server\game\common\conf\*.java
del /q ..\resource\conf\*.json
java -jar resource.jar ..\resource\excel\ ..\mmorpg\src\main\java\mmorpg\server\game\common\conf mmorpg.server.game.common.conf ..\resource\ 3 4
java -jar resource.jar ..\excel\Excel ..\mmorpg\src\main\java\mmorpg\server\game\common\conf mmorpg.server.game.common.conf ..\resource\  3 4
pause