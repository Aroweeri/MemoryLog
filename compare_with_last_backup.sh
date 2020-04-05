java -jar build/jar/MemoryLog.jar show --all | cut -b 6- > compare
backupFolder="$(grep BACKUPSPATH config.txt | cut -d= -f2)"
backupPath="${backupFolder}/$(ls -1 $backupFolder | grep basic | tail -n 1)"

colordiff "$backupPath" compare
wc -l "$backupPath" compare
rm compare
