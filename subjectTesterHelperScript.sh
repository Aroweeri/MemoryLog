#!/bin/bash

quiz="$1"

# check valid path
if ! [ -e "$quiz" ] ; then
	echo "No such file " "$quiz"
	exit 1
fi

cp "$quiz" "${quiz}.bak"
java -jar build/jar/SubjectTester.jar run "$quiz"
echo ""

colordiff "${quiz}.bak" "$quiz"
wc -l "$quiz"
wc -l "${quiz}.bak"

echo "Press enter to continue."
read -s
rm "${quiz}.bak"
