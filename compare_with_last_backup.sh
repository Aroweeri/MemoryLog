java -jar build/jar/MemoryLog.jar show --all > compare
backupPath="backups/$(ls -1 backups | grep basic | tail -n 1)"

colordiff compare "$backupPath"
wc -l compare "$backupPath"
rm compare
