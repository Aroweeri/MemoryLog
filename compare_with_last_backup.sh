memlogPath=$(grep "MEMLOGPATH" config.txt | cut -d= -f2)

colordiff backups/$(ls -1 backups | tail -n 1) "$memlogPath" 
wc -l "$memlogPath"  backups/$(ls -1 backups | tail -n 1)
