colordiff backups/$(ls -1 backups | tail -n 1) src/memorylog/auto_memory_log.txt
wc -l src/memorylog/auto_memory_log.txt backups/$(ls -1 backups | tail -n 1)
