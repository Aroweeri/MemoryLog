colordiff backups/$(ls -1 backups | tail -n 1) memorylog/auto_memory_log.txt
wc -l memorylog/auto_memory_log.txt backups/$(ls -1 backups | tail -n 1)
